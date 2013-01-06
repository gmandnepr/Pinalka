package com.pinalka.services;

import com.pinalka.dao.UserDAO;
import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import com.pinalka.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provide user services
 *
 * @author gman
 */
@Service
public class UserService {
    
    @Autowired
    private UserDAO userDAO;

    /**
     * Return user by his/her id
     *
     * @param id of the user to find
     * @return the user
     */
    @Transactional(readOnly = true)
    public User getUserById(final Long id){
        return userDAO.getById(id);
    }

    /**
     * Return user by his/her name
     *
     * @param name of the user to find
     * @return the user
     */
    @Transactional(readOnly = true)
    public User getUserByName(final String name) {
        return userDAO.getByName(name);
    }

    /**
     * Obtain currently logged in user
     *
     * @return currently logged in user or default if no logged
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        final String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDAO.getByName(name);
    }

    /**
     * Check if name has already been used by someone
     *
     * @param name to check
     * @return {@code true} if name is occupied and {@code false} otherwise
     */
    @Transactional(readOnly = true)
    public boolean isNameOccupied(final String name) {
        return userDAO.isNameOccupied(name);
    }

    /**
     * Register user
     *
     * @param user user object to register
     * @param pass the password of the user
     */
    @Transactional
    public void register(final User user, final String pass) {
        user.setPass(UserUtils.md5(pass));
        user.setRole(UserRole.ROLE_USER);
        UserUtils.fillNullFields(user);
        userDAO.create(user);
    }

    /**
     * Updates details of the user
     *
     * @param user user to update details
     * @param diff is the difference to the properties
     * @param pass new password of the user (will be set if not null and not empty)
     */
    @Transactional
    public void updateDetail(final User user, final User diff, final String pass) {
        UserUtils.merge(user, diff, pass);
        userDAO.update(user);
    }
}
