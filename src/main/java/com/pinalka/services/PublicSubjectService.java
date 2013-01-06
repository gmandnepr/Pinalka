package com.pinalka.services;

import java.util.List;

import com.pinalka.dao.PublicSubjectDAO;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provide methods to interact with public subjects
 *
 * @author gman
 */
@Service
public class PublicSubjectService {

    @Autowired
    private PublicSubjectDAO publicSubjectDAO;

    /**
     * Return list of all public subjects
     *
     * @return list of public subjects
     */
    @Transactional(readOnly = true)
    public List<PublicSubject> getPublicSubjects() {
        return publicSubjectDAO.getList();
    }

    /**
     * Return list of public subjects that user may want to add dependently on prediction
     *
     * @param user user to receive recommendations
     * @return list of public subjects
     */
    @Transactional(readOnly = true)
    public List<PublicSubject> getRecommendedPublicSubjects(final User user) {
        return publicSubjectDAO.getList();
    }
}
