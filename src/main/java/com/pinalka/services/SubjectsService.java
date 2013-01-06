package com.pinalka.services;

import java.util.Collections;
import java.util.List;

import com.pinalka.dao.ItemDAO;
import com.pinalka.dao.PublicSubjectDAO;
import com.pinalka.dao.SubjectDAO;
import com.pinalka.dao.UserDAO;
import com.pinalka.domain.Item;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import com.pinalka.util.SubjectsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provide methods to manage subjects
 *
 * @author gman
 */
@Service
public class SubjectsService {

    @Autowired
    private PublicSubjectDAO publicSubjectDAO;

    @Autowired
    private SubjectDAO subjectDAO;

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private UserDAO userDAO;

    /**
     * Return sorted copy of the visible public subjects
     *
     * @param user is the owner of the subjects
     * @return sorted list of visible public subjects
     */
    public List<Subject> getUserSortedSubjectsByPolicy(final User user) {
        switch (user.getVisibilityPolicy()) {
            case EVERY_SUBJECT_VISIBLE:
                return SubjectsUtil.getOrdered(
                                user.getSubjects());
            case PUBLIC_SUBJECT_VISIBLE:
                return SubjectsUtil.getOrdered(
                                SubjectsUtil.getPublic(
                                        user.getSubjects()));
            case NOTHING_VISIBLE:
            default:
                return Collections.emptyList();
        }
    }
    
    /**
     * Return sorted copy of the visible subjects
     * 
     * @param user is the owner of the subjects
     * @return sorted list of visible subjects
     */
    public List<Subject> getUserSortedSubjects(final User user) {
        return SubjectsUtil.getOrdered(
                        user.getSubjects());
    }

    /**
     * Increase priority of the subject (it will be higher in the list)
     * 
     * @param user subject owner
     * @param id subject id
     */
    @Transactional
    public void increasePriority(final User user, final Long id) {
        final Subject subject = getSubjectById(user, id);
        if(subject != null) {
            subject.setPriority(subject.getPriority()+1);
            subjectDAO.update(subject);
        }
    }

    /**
     * Increase priority of the subject (it will be higher in the list)
     *
     * @param user subject owner
     * @param id subject id
     */
    @Transactional
    public void decreasePriority(final User user, final Long id) {
        final Subject subject = getSubjectById(user, id);
        if(subject != null) {
            subject.setPriority(subject.getPriority()-1);
            subjectDAO.update(subject);
        }        
    }

    /**
     * Copies PublicSubject to the user schema
     *
     * @param user subject owner
     * @param publicSubjectId id of the public subject to copy down
     */
    @Transactional
    public void addNewPublicSubject(final User user, final Long publicSubjectId) {
        final PublicSubject publicSubject = publicSubjectDAO.getById(publicSubjectId);
        if(publicSubject != null){
            final Subject subject = SubjectsUtil.toSubject(publicSubject);
//            user.getPublicSubjects().add(publicSubject);
//            publicSubject.getUsers().add(user);

            user.getSubjects().add(subject);
            userDAO.update(user);

            publicSubject.setRate(publicSubject.getRate()+1);
            publicSubjectDAO.update(publicSubject);
        }
    }

    /**
     * Adds new Subject to the user schema
     *
     * @param user subject owner
     * @param subject subject to add
     */
    @Transactional
    public void addNewOwnSubject(final User user, final Subject subject) {

        SubjectsUtil.fillNullFields(subject);
        user.getSubjects().add(subject);
        userDAO.update(user);

        if(subject.getPublicSubject()){
            final PublicSubject publicSubject = SubjectsUtil.toPublicSubject(subject);

//            final User user1 = userDAO.getById(user.getId());//get instance
//            publicSubject.getUsers().add(user1);

            publicSubjectDAO.create(publicSubject);
        }
    }

    /**
     * Modify user subject
     *
     * @param user subject owner
     * @param id id of the subject to modify
     * @param diff object that contains difference
     */
    @Transactional
    public void editSubject(final User user, final Long id, final Subject diff) {

        final Subject subject = getSubjectById(user, id);
        if(subject != null){
            final List<Item> itemsToRemove = SubjectsUtil.merge(subject, diff);
            SubjectsUtil.fillNullFields(subject);
            subjectDAO.update(subject);//here we remove links but not items
            if(subject.getPublicSubject()){
                publicSubjectDAO.create(SubjectsUtil.toPublicSubject(subject));
            }
            for(final Item itemToRemove : itemsToRemove) {
                itemDAO.deleteById(itemToRemove.getId());//hear we remove items
            }
        }
    }

    /**
     * Removes subject from the user schema
     *
     * @param user subject owner
     * @param id subject id
     */
    @Transactional
    public void removeSubject(final User user, final Long id) {
        
        final Subject subject = getSubjectById(user, id);
        if(subject != null) {
            user.getSubjects().remove(subject);
            userDAO.update(user);//here we delete link but not subject
//            final Subject s1 = subjectDAO.getById(id);
//            subjectDAO.delete(s1);
            subjectDAO.deleteById(id);//here we remove subject itself
        }
    }

    // SUBJECT AND ITEM MANIPULATION
    
    /**
     * Return subject of the user by id
     * 
     * @param user is the owner of the subject
     * @param id is the id of the subject
     * @return the subject or null if not found
     */
    public Subject getSubjectById(final User user, final Long id) {
        for(final Subject subject : user.getSubjects()){
            if(subject.getId().equals(id)){
                return subject;
            }
        }
        return null;
    }

    /**
     * Return item of the user by subject and item id
     *
     * @param user is the owner of the subject
     * @param subjectId is the id of the subject
     * @param itemId is the id of the item
     * @return the subject or null if not found
     */
    public Item getItemById(final User user, final Long subjectId, final Long itemId) {
        final Subject subject = getSubjectById(user, subjectId);
        if(subject != null){
            for(final Item item : subject.getItems()){
                if(item.getId().equals(itemId)){
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Update state of the item of the subject
     *
     * @param user owner of the item
     * @param subjectId id of the subject of the item
     * @param itemId id of the item
     * @param newState new state
     */
    @Transactional
    public void updateState(final User user, final Long subjectId, final Long itemId, final State newState) {
        final Item item = getItemById(user, subjectId, itemId);
        if(item != null){
            item.setState(newState);
            itemDAO.update(item);
        }
    }
}
