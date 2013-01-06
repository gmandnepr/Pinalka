package com.pinalka.domain;

/**
 * Possible states of the item
 *
 * @author gman
 */
public enum State {

    /**
     * Item has not started yet
     */
    NOT_STARTED("not_started", 0.0),
    /**
     * Item has been started, but not finished yet
     */
    IN_PROGRESS("in_progress", 0.5),
    /**
     * Item has been done successfully
     */
    DONE("done", 1.0),
    /**
     * Item has been done unsuccessfully and need to be redone
     */
    REOPEN("reopen", 0.25);

    /**
     * Prefix of the full key in the property file
     */
    private static final String PREFIX = "schema.item.state.";
    /**
     * Key of the items of this state
     */
    private final String key;
    /**
     * Done ration while counting total done
     */
    private final double doneRatio;

    /**
     * Default constructor with key and done ratio
     *
     * @param key in the property file
     * @param doneRatio is the done ration
     */
    private State(final String key, final double doneRatio) {
        this.key = key;
        this.doneRatio = doneRatio;
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

    /**
     * Return the done ratio of the item
     *
     * @return done ratio of the subject
     */
    public double getDoneRatio() {
        return doneRatio;
    }
}
