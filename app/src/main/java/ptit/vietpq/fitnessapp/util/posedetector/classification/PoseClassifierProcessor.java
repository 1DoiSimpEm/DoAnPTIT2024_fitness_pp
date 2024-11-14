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
import android.util.Log;

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

import ptit.vietpq.fitnessapp.presentation.exercise.ExerciseStats;
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
    public ExerciseStats getExerciseStats(Pose pose) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        ClassificationResult classification = poseClassifier.classify(pose);
        int reps = 0;
        float confidence = 0f;

        // Update {@link RepetitionCounter}s if {@code isStreamMode}.
        if (isStreamMode) {
            classification = emaSmoothing.getSmoothedResult(classification);

            // Return early without updating repCounter if no pose found.
            if (pose.getAllPoseLandmarks().isEmpty()) {
                if (!lastRepResult.isEmpty()) {
                    String[] parts = lastRepResult.split(" : ");
                    if (parts.length == 2) {
                        try {
                            reps = Integer.parseInt(parts[1].split(" ")[0]);
                        } catch (NumberFormatException e) {
                            Timber.tag(TAG).e(e, "Error parsing last rep result");
                        }
                    }
                }
                return new ExerciseStats(reps, confidence);
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
                    reps = repsAfter;
                    break;
                }
            }
        }

        // Add maxConfidence class of current frame to result if pose is found.
        if (!pose.getAllPoseLandmarks().isEmpty()) {
            String maxConfidenceClass = classification.getMaxConfidenceClass();
            confidence = (classification.getClassConfidence(maxConfidenceClass) / poseClassifier.confidenceRange()) * 100;
        }

        return new ExerciseStats(reps, confidence);
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
                    formFeedback = generatePushupFeedback(jointAngles);
                } else if (maxConfidenceClass.equals(SQUATS_CLASS)) {
                    jointAngles = calculateSquatAngles(pose);
                    formFeedback = generateSquatFeedback(jointAngles);
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

        // Calculate elbow angle
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);

        float elbowAngle = calculateAngle(leftShoulder, leftElbow, leftWrist);
        angles.put("elbow_angle", elbowAngle);

        // Calculate back angle (relative to ground)
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        float backAngle = calculateAngleToGround(leftShoulder, leftHip);
        angles.put("back_angle", backAngle);

        return angles;
    }

    private Map<String, Float> calculateSquatAngles(Pose pose) {
        Map<String, Float> angles = new HashMap<>();

        // Calculate knee angle
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);

        float kneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
        angles.put("knee_angle", kneeAngle);

        // Calculate hip angle
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        float hipAngle = calculateAngle(leftShoulder, leftHip, leftKnee);
        angles.put("hip_angle", hipAngle);

        return angles;
    }

    private String generatePushupFeedback(Map<String, Float> angles) {
        StringBuilder feedback = new StringBuilder();

        float elbowAngle = angles.get("elbow_angle");
        float backAngle = angles.get("back_angle");

        if (elbowAngle < 90) {
            feedback.append("Go lower in your pushup. ");
        } else if (elbowAngle > 160) {
            feedback.append("Push up more to fully extend arms. ");
        }

        if (backAngle < 170) {
            feedback.append("Keep your back straight. ");
        }

        return feedback.length() > 0 ? feedback.toString() : "Good form!";
    }

    private String generateSquatFeedback(Map<String, Float> angles) {
        StringBuilder feedback = new StringBuilder();

        float kneeAngle = angles.get("knee_angle");
        float hipAngle = angles.get("hip_angle");

        if (kneeAngle > 90) {
            feedback.append("Squat lower. ");
        } else if (kneeAngle < 45) {
            feedback.append("Don't squat too low. ");
        }

        if (hipAngle < 90) {
            feedback.append("Keep your back more upright. ");
        }

        return feedback.length() > 0 ? feedback.toString() : "Good form!";
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