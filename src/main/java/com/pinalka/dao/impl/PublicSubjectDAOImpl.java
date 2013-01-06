package com.pinalka.dao.impl;

import com.pinalka.dao.PublicSubjectDAO;
import com.pinalka.domain.PublicSubject;
import org.springframework.stereotype.Repository;

/**
 * DAO implementation for the PublicSubject class
 *
 * @author gman
 */
@Repository
public class PublicSubjectDAOImpl extends GenericDAOImpl<PublicSubject> implements PublicSubjectDAO {

    public PublicSubjectDAOImpl() {
        super("select ps from com.pinalka.domain.PublicSubject ps order by rate desc");
    }
}
