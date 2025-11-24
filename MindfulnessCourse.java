package com.guvi.mindfulness.model;

/**
 * Represents curated mindfulness courses that learners can enroll in.
 * Extends {@link MindfulnessActivity} showcasing inheritance and adds extra behaviour.
 */
public class MindfulnessCourse extends MindfulnessActivity {

    private String instructor;
    private String level;
    private int durationMinutes;

    public MindfulnessCourse() {
    }

    public MindfulnessCourse(long id, String title, String description, String instructor, String level, int durationMinutes) {
        super(id, title, description);
        this.instructor = instructor;
        this.level = level;
        this.durationMinutes = durationMinutes;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String getPracticeType() {
        return "COURSE";
    }

    @Override
    public String getCategory() {
        return level + " Course";
    }
}

