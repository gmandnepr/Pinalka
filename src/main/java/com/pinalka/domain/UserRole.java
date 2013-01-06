package com.pinalka.domain;

/**
 * Define roles of the user of the system
 *
 * @author gman
 */
public enum UserRole {

    /**
     * For not logged users
     */
    ROLE_ANONYMOUS("anonymous"),
    /**
     * Default user
     */
    ROLE_USER("user"),
    /**
     * User that can manage other users
     */
    ROLE_ADMIN("admin");

    /**
     * Prefix of the full key in the property file
     */
    private static final String PREFIX = "profile.role.";
    /**
     * Key of the items of this state
     */
    private final String key;

    /**
     * Default constructor with key
     *
     * @param key in the property file
     */
    private UserRole(String key) {
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
