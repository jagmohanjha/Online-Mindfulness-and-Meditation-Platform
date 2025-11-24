package com.guvi.mindfulness.model;

/**
 * Base class that captures shared attributes for any mindfulness content.
 * Demonstrates inheritance + encapsulation which will be used by course/session models.
 */
public abstract class MindfulnessActivity implements MindfulnessPractice {

    private long id;
    private String title;
    private String description;

    protected MindfulnessActivity() {
    }

    protected MindfulnessActivity(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Allows subclasses to define their own category without coupling callers to specific types.
     */
    public abstract String getCategory();

    @Override
    public String toString() {
        return "MindfulnessActivity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

