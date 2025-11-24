package com.guvi.mindfulness.model;

import java.time.LocalDateTime;

/**
 * Represents a single meditation or mindfulness practice session recorded by the learner.
 */
public class MindfulnessSession extends MindfulnessActivity {

    private long userId;
    private String difficulty;
    private String category;
    private LocalDateTime scheduledAt;
    private int durationMinutes;
    private String reflectionNotes;

    public MindfulnessSession() {
    }

    public MindfulnessSession(long id, long userId, String title, String description,
                              String difficulty, String category, LocalDateTime scheduledAt,
                              int durationMinutes, String reflectionNotes) {
        super(id, title, description);
        this.userId = userId;
        this.difficulty = difficulty;
        this.category = category;
        this.scheduledAt = scheduledAt;
        this.durationMinutes = durationMinutes;
        this.reflectionNotes = reflectionNotes;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getReflectionNotes() {
        return reflectionNotes;
    }

    public void setReflectionNotes(String reflectionNotes) {
        this.reflectionNotes = reflectionNotes;
    }

    @Override
    public String getPracticeType() {
        return "SESSION";
    }
}

