package com.pinalka.dao.impl;

import javax.persistence.NoResultException;

import com.pinalka.dao.UserDAO;
import com.pinalka.domain.User;
import com.pinalka.util.UserUtils;
import org.springframework.stereotype.Repository;

/**
 * DAO implementation for the User class
 *
 * @author gman
 */
@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {

    /**
     * Constructor for the DAO
     */
    public UserDAOImpl() {
        super("select u from com.pinalka.domain.User u");
    }

    @Override
    public User getByName(final String name) {
        try {
            return (User) em.createQuery("select u from com.pinalka.domain.User u where name = :name").
                    setParameter("name", name).
                    getSingleResult();
        } catch (final NoResultException e) {
            return UserUtils.createAnonymousUser();
        }
    }

    @Override
    public boolean isNameOccupied(String name) {
        return !em.createQuery("select u from com.pinalka.domain.User u where name = :name").
                setParameter("name", name).getResultList().isEmpty();
    }
}
