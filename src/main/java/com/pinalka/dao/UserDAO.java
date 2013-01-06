package com.pinalka.dao;

import com.pinalka.domain.User;

/**
 * DAO for the User class
 *
 * @author gman
 */
public interface UserDAO extends GenericDAO<User> {

    /**
     * Finds user by the name
     *
     * @param name is the name of the user
     * @return user by his/her name
     */
    User getByName(final String name);

    /**
     * Check is there user with the given name
     *
     * @param name to check avalability
     * @return {@Code true} if there is user with such name in the database, {@Code false} otherwice
     */
    boolean isNameOccupied(final String name);
}
