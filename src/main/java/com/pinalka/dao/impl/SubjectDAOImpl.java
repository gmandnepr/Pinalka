package com.pinalka.dao.impl;

import com.pinalka.dao.SubjectDAO;
import com.pinalka.domain.Subject;
import org.springframework.stereotype.Repository;

/**
 * DAO implementation for the Subject class
 *
 * @author gman
 */
@Repository
public class SubjectDAOImpl extends GenericDAOImpl<Subject> implements SubjectDAO {

    /**
     * Constructor for the DAO
     */
    public SubjectDAOImpl() {
        super("select s from com.pinalka.domain.Subject s");
    }
}
