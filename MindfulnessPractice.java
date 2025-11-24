package com.guvi.mindfulness.model;

/**
 * Simple contract used to demonstrate interface based polymorphism.
 * Any mindfulness content that can be delivered to a learner must provide
 * the content type and recommended duration.
 */
public interface MindfulnessPractice {

    String getPracticeType();

    int getDurationMinutes();
}

