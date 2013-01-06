package com.pinalka.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.pinalka.domain.Item;
import com.pinalka.domain.PublicItem;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;

/**
 * Provide common operations on subject
 *
 * @author gman
 */
public final class SubjectsUtil {

    private static final Comparator<Subject> SUBJECTS_ORDER = new SubjectsOrderComparator();
    private static final String EMPTY_STRING = "";
    private static final Integer INITIAL_PRIORITY = Integer.valueOf(50);
    private static final Boolean INITIAL_PUBLICITY = Boolean.FALSE;

    private SubjectsUtil() {
    }

    /**
     * Return copy of public subjects
     *
     * @param subjects to filter
     * @return copy of the list with modifications
     */
    public static List<Subject> getPublic(final Iterable<Subject> subjects) {
        final List<Subject> subjectsCopy = new ArrayList<Subject>();
        for (final Subject subject : subjects) {
            if (subject.getPublicSubject()) {
                subjectsCopy.add(subject);
            }
        }
        return subjectsCopy;
    }

    /**
     * Return copy of ordered subjects
     *
     * @param subjects to order
     * @return ordered copy of the list
     */
    public static List<Subject> getOrdered(final List<Subject> subjects) {
        final List<Subject> subjectsCopy = new ArrayList<Subject>(subjects);
        Collections.sort(subjectsCopy, SUBJECTS_ORDER);
        return subjectsCopy;
    }

    /**
     * Converts Subject to PublicSubject
     *
     * @param subject to be converted
     * @return PublicSubject representation of the subject
     */
    public static PublicSubject toPublicSubject(final Subject subject) {
        final PublicSubject publicSubject = new PublicSubject();

        publicSubject.setLabel(subject.getLabel());
        publicSubject.setTeacher(subject.getTeacher());
        publicSubject.setDescription(subject.getDescription());
        publicSubject.setRate(Integer.valueOf(1));
        for (final Item item : subject.getItems()) {
            final PublicItem publicItem = new PublicItem();
            publicItem.setLabel(item.getLabel());
            publicItem.setDescription(item.getDescription());
            publicItem.setDeadline(item.getDeadline());
            publicSubject.getItems().add(publicItem);
        }

        return publicSubject;
    }

    /**
     * Convert PublicSubject to Subject
     *
     * @param publicSubject to be converted
     * @return Subject representation of the PublicSubject
     */
    public static Subject toSubject(final PublicSubject publicSubject) {
        final Subject subject = new Subject();

        subject.setLabel(publicSubject.getLabel());
        subject.setDescription(publicSubject.getDescription());
        subject.setTeacher(publicSubject.getTeacher());
        subject.setPublicSubject(Boolean.TRUE);
        subject.setPriority(INITIAL_PRIORITY);
        for (final PublicItem publicItem : publicSubject.getItems()) {
            final Item item = new Item();
            item.setLabel(publicItem.getLabel());
            item.setDescription(publicItem.getDescription());
            item.setDeadline(publicItem.getDeadline());
            item.setState(State.NOT_STARTED);
            subject.getItems().add(item);
        }

        return subject;
    }

    /**
     * Update receiver fields with diff fields if diff is not null and not equal to the receiver.
     *
     * @param receiver object to apply diff
     * @param diff     to apply
     * @return subjects that must be removed from the database
     */
    public static List<Item> merge(final Subject receiver, final Subject diff) {
        if (diff.getLabel() != null && !diff.getLabel().equals(receiver.getLabel())) {
            receiver.setLabel(diff.getLabel());
        }
        if (diff.getDescription() != null && !diff.getDescription().equals(receiver.getLabel())) {
            receiver.setDescription(diff.getDescription());
        }
        if (diff.getTeacher() != null && !diff.getTeacher().equals(receiver.getTeacher())) {
            receiver.setTeacher(diff.getTeacher());
        }
        if (diff.getPublicSubject() != null && !diff.getPublicSubject().equals(receiver.getPublicSubject())) {
            receiver.setPublicSubject(diff.getPublicSubject());
        }
        if (diff.getPriority() != null && !diff.getPriority().equals(receiver.getPriority())) {
            receiver.setPriority(diff.getPriority());
        }

        for(final Item diffItem : diff.getItems()){
            if(receiver.getItems().contains(diffItem)) {
                for(final Item receiverItem : receiver.getItems()){
                    if(receiverItem.equals(diffItem)){
                        merge(receiverItem, diffItem);
                        break;
                    }
                }
            } else {
                if(diffItem.getLabel() != null && !diffItem.getLabel().isEmpty()){
                    receiver.getItems().add(fillNullFields(diffItem));
                }
            }
        }
        final List<Item> toRemove = new ArrayList<Item>();
        for(int i=receiver.getItems().size()-1; i>=0; i--){
            if(!diff.getItems().contains(receiver.getItems().get(i))){
                toRemove.add(receiver.getItems().remove(i));
            }
        }
        return toRemove;
    }
    
    /**
     * Update receiver fields with diff fields if diff is not null and not equal to the receiver.
     *
     * @param receiver object to apply diff
     * @param diff     to apply
     * @return subjects that must be removed from the database
     */
    public static void merge(final Item receiver, final Item diff) {
        if(diff.getLabel() != null && !diff.getLabel().equals(receiver.getLabel())){
            receiver.setLabel(diff.getLabel());
        }
        if(diff.getDescription() != null && !diff.getDescription().equals(receiver.getDescription())){
            receiver.setDescription(diff.getDescription());
        }
        if(diff.getDeadline() != null && !diff.getDeadline().equals(receiver.getDeadline())){
            receiver.setDeadline(pruneDate(diff.getDeadline()));//TODO does not merge properly
        }
        if(diff.getState() != null && diff.getState() != receiver.getState()){
            receiver.setState(diff.getState());
        }
    }

    /**
     * Replaces null values in the fields with the default values
     *
     * @param subject to update fields
     */
    public static Subject fillNullFields(final Subject subject) {
        if(subject.getLabel() == null) {
            subject.setLabel(EMPTY_STRING);
        }
        if(subject.getDescription() == null){
            subject.setDescription(EMPTY_STRING);
        }
        if(subject.getTeacher() == null){
            subject.setTeacher(EMPTY_STRING);
        }
        if (subject.getPublicSubject() == null) {
            subject.setPublicSubject(INITIAL_PUBLICITY);
        }
        if (subject.getPriority() == null) {
            subject.setPriority(INITIAL_PRIORITY);
        }
        for(final Item item : subject.getItems()) {
            fillNullFields(item);
        }
        return subject;
    }
    
    /**
     * Replaces null values in the fields with the default values
     *
     * @param item to update fields
     */
    public static Item fillNullFields(final Item item) {
        if(item.getLabel() == null){
            item.setLabel(EMPTY_STRING);
        }
        if(item.getDescription() == null){
            item.setDescription(EMPTY_STRING);
        }
        if(item.getDeadline() == null){
            item.setDeadline(pruneDate(null));
        }
        if(item.getState() == null){
            item.setState(State.NOT_STARTED);
        }
        return item;
    }

    /**
     * Creates subject with not null fields
     *
     * @return null safe object
     */
    public static Subject createSubject() {
        final Subject subject = new Subject();
        subject.setLabel(EMPTY_STRING);
        subject.setDescription(EMPTY_STRING);
        subject.setTeacher(EMPTY_STRING);
        subject.setPublicSubject(INITIAL_PUBLICITY);
        subject.setPriority(INITIAL_PRIORITY);
        return subject;
    }

    /**
     * Prunes given date
     *
     * @param date is the date to prune
     * @return puned date
     */
    public static Date pruneDate(Date date) {
        final long hourTimeMillis = 3600 * 1000;
        final Calendar calendar = Calendar.getInstance();
        if (date == null || Math.abs(date.compareTo(calendar.getTime())) < hourTimeMillis) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isLate(Item item) {
        return item.getState() != State.DONE && item.getDeadline() != null && item.getDeadline().before(new Date());
    }
}
