package ptit.vietpq.fitnessapp.util.posedetector;

import java.util.Map;

public class ExerciseInfo {
    private final int currentReps;
    private final float formAccuracy;
    private final String exerciseType;
    private final Map<String, Float> jointAngles;
    private final String formFeedback;

    public ExerciseInfo(int currentReps, float formAccuracy, String exerciseType, 
                       Map<String, Float> jointAngles, String formFeedback) {
        this.currentReps = currentReps;
        this.formAccuracy = formAccuracy;
        this.exerciseType = exerciseType;
        this.jointAngles = jointAngles;
        this.formFeedback = formFeedback;
    }

    public int getCurrentReps() { return currentReps; }
    public float getFormAccuracy() { return formAccuracy; }
    public String getExerciseType() { return exerciseType; }
    public Map<String, Float> getJointAngles() { return jointAngles; }
    public String getFormFeedback() { return formFeedback; }
}