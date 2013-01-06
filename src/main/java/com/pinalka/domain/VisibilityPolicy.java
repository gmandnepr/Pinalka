package com.pinalka.domain;

/**
 * Determines visibility level of user subjects to another users
 *
 * @author gman
 */
public enum VisibilityPolicy {

    //TODO think about unregistered users access
    //EVERY_SUBJECT_VISIBLE_TO_EVERYONE
    //PUBLIC_SUBJECT_VISIBLE_TO_EVERYONE
    /**
     * Other users can see whole list of subjects
     */
    EVERY_SUBJECT_VISIBLE("every_subject_visible"),
    /**
     * Other users can see only public subjects
     */
    PUBLIC_SUBJECT_VISIBLE("public_subject_visible"),
    /**
     * None can see subjects
     */
    NOTHING_VISIBLE("nothing_visible");

    /**
     * Prefix of the full key in the property file
     */
    private static final String PREFIX = "profile.visibility.policy.";
    /**
     * Key of the items of this state
     */
    private final String key;

    /**
     * Default constructor with key
     *
     * @param key in the property file
     */
    private VisibilityPolicy(String key) {
        this.key = key;
    }

    /**
     * Return short key in the property file
     *
     * @return short key in the property file
     */
    public String getKey() {
        return key;
    }

    /**
     * Return full key in the property file
     *
     * @return full key in the property file
     */
    public String getFullKey() {
        return PREFIX + key;
    }
}
