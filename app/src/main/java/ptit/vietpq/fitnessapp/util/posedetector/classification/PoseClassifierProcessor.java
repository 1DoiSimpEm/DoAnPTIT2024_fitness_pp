/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ptit.vietpq.fitnessapp.util.posedetector.classification;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;

import androidx.annotation.WorkerThread;

import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import ptit.vietpq.fitnessapp.util.posedetector.ExerciseInfo;
import timber.log.Timber;

/**
 * Accepts a stream of {@link Pose} for classification and Rep counting.
 */
public class PoseClassifierProcessor {
    private static final String TAG = "PoseClassifierProcessor";
    private static final String POSE_SAMPLES_FILE = "pose/fitness_pose_samples.csv";

    // Specify classes for which we want rep counting.
    // These are the labels in the given {@code POSE_SAMPLES_FILE}. You can set your own class labels
    // for your pose samples.
    private static final String PUSHUPS_CLASS = "pushups_down";
    private static final String SQUATS_CLASS = "squats_down";
    private static final String[] POSE_CLASSES = {
            PUSHUPS_CLASS, SQUATS_CLASS
    };

    private final boolean isStreamMode;

    private EMASmoothing emaSmoothing;
    private List<RepetitionCounter> repCounters;
    private PoseClassifier poseClassifier;
    private String lastRepResult;

    @WorkerThread
    public PoseClassifierProcessor(Context context, boolean isStreamMode) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        this.isStreamMode = isStreamMode;
        if (isStreamMode) {
            emaSmoothing = new EMASmoothing();
            repCounters = new ArrayList<>();
            lastRepResult = "";
        }
        loadPoseSamples(context);
    }

    private void loadPoseSamples(Context context) {
        List<PoseSample> poseSamples = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
            String csvLine = reader.readLine();
            while (csvLine != null) {
                // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
                PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
                if (poseSample != null) {
                    poseSamples.add(poseSample);
                }
                csvLine = reader.readLine();
            }
        } catch (IOException e) {
            Timber.tag(TAG).e("Error when loading pose samples.\n" + e);
        }
        poseClassifier = new PoseClassifier(poseSamples);
        if (isStreamMode) {
            for (String className : POSE_CLASSES) {
                repCounters.add(new RepetitionCounter(className));
            }
        }
    }

    /**
     * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
     * classification results.
     *
     * <p>Currently it returns up to 2 strings as following:
     * 0: PoseClass : X reps
     * 1: PoseClass : [0.0-1.0] confidence
     */
    @WorkerThread
    public List<String> getPoseResult(Pose pose) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        List<String> result = new ArrayList<>();
        ClassificationResult classification = poseClassifier.classify(pose);

        // Update {@link RepetitionCounter}s if {@code isStreamMode}.
        if (isStreamMode) {
            // Feed pose to smoothing even if no pose found.
            classification = emaSmoothing.getSmoothedResult(classification);

            // Return early without updating repCounter if no pose found.
            if (pose.getAllPoseLandmarks().isEmpty()) {
                result.add(lastRepResult);
                return result;
            }

            for (RepetitionCounter repCounter : repCounters) {
                int repsBefore = repCounter.getNumRepeats();
                int repsAfter = repCounter.addClassificationResult(classification);
                if (repsAfter > repsBefore) {
                    // Play a fun beep when rep counter updates.
                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    lastRepResult = String.format(
                            Locale.US, "%s : %d reps", repCounter.getClassName(), repsAfter);
                    break;
                }
            }
            result.add(lastRepResult);
        }

        // Add maxConfidence class of current frame to result if pose is found.
        if (!pose.getAllPoseLandmarks().isEmpty()) {
            String maxConfidenceClass = classification.getMaxConfidenceClass();
            String maxConfidenceClassResult = String.format(
                    Locale.US,
                    "%s : %.2f confidence",
                    maxConfidenceClass,
                    classification.getClassConfidence(maxConfidenceClass)
                            / poseClassifier.confidenceRange());
            result.add(maxConfidenceClassResult);
        }

        return result;
    }


    @WorkerThread
    public ExerciseInfo getExerciseInfo(Pose pose) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        ClassificationResult classification = poseClassifier.classify(pose);

        int reps = 0;
        float formAccuracy = 0f;
        String exerciseType = "";
        Map<String, Float> jointAngles = new HashMap<>();
        String formFeedback = "";
        String language = Locale.getDefault().getLanguage();
        if (isStreamMode) {
            classification = emaSmoothing.getSmoothedResult(classification);

            if (!pose.getAllPoseLandmarks().isEmpty()) {
                // Get the current exercise type
                String maxConfidenceClass = classification.getMaxConfidenceClass();
                exerciseType = maxConfidenceClass;
                formAccuracy = (classification.getClassConfidence(maxConfidenceClass) /
                        poseClassifier.confidenceRange()) * 100;

                // Calculate important joint angles based on exercise type
                if (maxConfidenceClass.equals(PUSHUPS_CLASS)) {
                    jointAngles = calculatePushupAngles(pose);
                    formFeedback = generatePushupFeedback(jointAngles,language);
                } else if (maxConfidenceClass.equals(SQUATS_CLASS)) {
                    jointAngles = calculateSquatAngles(pose);
                    formFeedback = generateSquatFeedback(jointAngles,language);
                }
            }

            // Get current reps
            for (RepetitionCounter repCounter : repCounters) {
                if (repCounter.getClassName().equals(exerciseType)) {
                    reps = repCounter.getNumRepeats();
                    break;
                }
            }
        }

        return new ExerciseInfo(reps, formAccuracy, exerciseType, jointAngles, formFeedback);
    }

    private Map<String, Float> calculatePushupAngles(Pose pose) {
        Map<String, Float> angles = new HashMap<>();

        // Calculate both left and right side angles for more robust detection
        float leftElbowAngle = calculateAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        );
        float rightElbowAngle = calculateAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
                pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        );

        // Average of both sides for more accurate representation
        float elbowAngle = (leftElbowAngle + rightElbowAngle) / 2;
        angles.put("elbow_angle", elbowAngle);

        // Calculate back angle using both hip and shoulder landmarks
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

        float backAngleLeft = calculateAngleToGround(leftShoulder, leftHip);
        float backAngleRight = calculateAngleToGround(rightShoulder, rightHip);
        float backAngle = (backAngleLeft + backAngleRight) / 2;
        angles.put("back_angle", backAngle);

        // Additional body alignment metrics
        float shoulderAlignment = calculateHorizontalAlignment(leftShoulder, rightShoulder);
        angles.put("shoulder_alignment", shoulderAlignment);

        return angles;
    }

    private Map<String, Float> calculateSquatAngles(Pose pose) {
        Map<String, Float> angles = new HashMap<>();

        // Calculate both left and right side angles
        float leftKneeAngle = calculateAngle(
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
                pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        );
        float rightKneeAngle = calculateAngle(
                pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
        );

        float kneeAngle = (leftKneeAngle + rightKneeAngle) / 2;
        angles.put("knee_angle", kneeAngle);

        // Hip angle calculation
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

        float leftHipAngle = calculateAngle(leftShoulder, leftHip, leftHip);
        float rightHipAngle = calculateAngle(rightShoulder, rightHip, rightHip);
        float hipAngle = (leftHipAngle + rightHipAngle) / 2;
        angles.put("hip_angle", hipAngle);

        // Depth and balance metrics
        float horizontalBalance = calculateHorizontalBalance(pose);
        angles.put("horizontal_balance", horizontalBalance);

        return angles;
    }

    private String generatePushupFeedback(Map<String, Float> angles, String language) {
        StringBuilder feedback = new StringBuilder();
        boolean isVietnamese = "vi".equalsIgnoreCase(language);

        Map<String, String> messages = isVietnamese ?
                Map.of(
                        "elbow_deep", "Hãy xuống sâu hơn: Gập khuỷu tay nhiều hơn ở điểm thấp nhất. ",
                        "elbow_extend", "Duỗi thẳng: Duỗi thẳng tay hoàn toàn ở điểm cao nhất. ",
                        "back_straight", "Giữ lưng thẳng. Duy trì tư thế plank cứng. ",
                        "shoulder_level", "Giữ vai ngang bằng. Phân bố trọng lượng đều. ",
                        "perfect", "Tư thế chống đẩy hoàn hảo!"
                ) :
                Map.of(
                        "elbow_deep", "Go deeper: Bend your elbows more at the bottom of the pushup. ",
                        "elbow_extend", "Full extension: Straighten your arms completely at the top. ",
                        "back_straight", "Keep your back straight. Maintain a rigid plank-like position. ",
                        "shoulder_level", "Level your shoulders. Maintain even weight distribution. ",
                        "perfect", "Excellent pushup form!"
                );

        float elbowAngle = angles.get("elbow_angle");
        float backAngle = angles.get("back_angle");
        float shoulderAlignment = angles.get("shoulder_alignment");

        if (elbowAngle < 70) {
            feedback.append(messages.get("elbow_deep"));
        } else if (elbowAngle > 170) {
            feedback.append(messages.get("elbow_extend"));
        }

        if (backAngle < 160) {
            feedback.append(messages.get("back_straight"));
        }

        if (Math.abs(shoulderAlignment) > 5) {
            feedback.append(messages.get("shoulder_level"));
        }

        return feedback.length() > 0 ? feedback.toString() : messages.get("perfect");
    }

    private String generateSquatFeedback(Map<String, Float> angles, String language) {
        StringBuilder feedback = new StringBuilder();
        boolean isVietnamese = "vi".equalsIgnoreCase(language);

        Map<String, String> messages = isVietnamese ?
                Map.of(
                        "squat_deeper", "Ngồi xuống sâu hơn: Đùi nên song song với mặt đất. ",
                        "squat_high", "Đừng xuống quá thấp: Tránh xuống quá sâu để bảo vệ đầu gối. ",
                        "torso_upright", "Giữ thân trên thẳng đứng hơn. Ngực hướng lên, lưng thẳng. ",
                        "balance", "Cân bằng trọng lượng. Phân bố đều qua hai chân. ",
                        "perfect", "Kỹ thuật squat hoàn hảo!"
                ) :
                Map.of(
                        "squat_deeper", "Squat deeper: Aim to get thighs parallel to ground. ",
                        "squat_high", "Don't overextend: Avoid going too low to prevent knee strain. ",
                        "torso_upright", "Keep your torso more upright. Chest up, back straight. ",
                        "balance", "Balance your weight evenly. Distribute weight through both feet. ",
                        "perfect", "Perfect squat technique!"
                );

        float kneeAngle = angles.get("knee_angle");
        float hipAngle = angles.get("hip_angle");
        float horizontalBalance = angles.get("horizontal_balance");

        if (kneeAngle > 110) {
            feedback.append(messages.get("squat_deeper"));
        } else if (kneeAngle < 60) {
            feedback.append(messages.get("squat_high"));
        }

        if (hipAngle < 80) {
            feedback.append(messages.get("torso_upright"));
        }

        if (Math.abs(horizontalBalance) > 10) {
            feedback.append(messages.get("balance"));
        }

        return feedback.length() > 0 ? feedback.toString() : messages.get("perfect");
    }

    private float calculateHorizontalAlignment(PoseLandmark left, PoseLandmark right) {
        // Calculate horizontal misalignment between two landmarks
        return right.getPosition().x - left.getPosition().x;
    }

    private float calculateHorizontalBalance(Pose pose) {
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        // Return the difference in x-position to measure balance
        return rightAnkle.getPosition().x - leftAnkle.getPosition().x;
    }

    private float calculateAngle(PoseLandmark first, PoseLandmark middle, PoseLandmark last) {
        double angle = Math.toDegrees(
                Math.atan2(last.getPosition().y - middle.getPosition().y,
                        last.getPosition().x - middle.getPosition().x) -
                        Math.atan2(first.getPosition().y - middle.getPosition().y,
                                first.getPosition().x - middle.getPosition().x));

        angle = Math.abs(angle);
        if (angle > 180) {
            angle = 360.0 - angle;
        }
        return (float) angle;
    }

    private float calculateAngleToGround(PoseLandmark top, PoseLandmark bottom) {
        double angle = Math.toDegrees(
                Math.atan2(bottom.getPosition().y - top.getPosition().y,
                        bottom.getPosition().x - top.getPosition().x));
        return (float) Math.abs(angle);
    }

}