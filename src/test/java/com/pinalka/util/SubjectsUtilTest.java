package com.pinalka.util;


import java.util.Date;
import java.util.List;

import com.pinalka.domain.Item;
import com.pinalka.domain.PublicItem;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gman
 */
public class SubjectsUtilTest {

    @Test
    public void testNullFields() throws IllegalAccessException {
        final Subject subject = new Subject();

        SubjectsUtil.fillNullFields(subject);

        assertNull(subject.getId());
        assertNotNull(subject.getLabel());
        assertNotNull(subject.getDescription());
        assertNotNull(subject.getTeacher());
        assertNotNull(subject.getPublicSubject());
        assertNotNull(subject.getPriority());
    }

    @Test
    public void testCreateSubject() {
        final Subject subject = SubjectsUtil.createSubject();

        assertNull(subject.getId());
        assertNotNull(subject.getLabel());
        assertNotNull(subject.getDescription());
        assertNotNull(subject.getTeacher());
        assertNotNull(subject.getPublicSubject());
        assertNotNull(subject.getPriority());
    }

    @Test
    public void testToSubject() {
        final PublicSubject publicSubject = new PublicSubject();
        publicSubject.setLabel("label");
        publicSubject.setDescription("description");
        publicSubject.setTeacher("teacher");
        final PublicItem publicItem1 = new PublicItem();
        publicItem1.setLabel("label1");
        publicItem1.setDescription("description1");
        publicItem1.setDeadline(new Date());
        publicSubject.getItems().add(publicItem1);
        final PublicItem publicItem2 = new PublicItem();
        publicItem2.setLabel("label2");
        publicItem2.setDescription("description2");
        publicItem2.setDeadline(new Date());
        publicSubject.getItems().add(publicItem2);

        final Subject subject = SubjectsUtil.toSubject(publicSubject);

        assertEquals(publicSubject.getLabel(), subject.getLabel());
        assertEquals(publicSubject.getDescription(), subject.getDescription());
        assertEquals(publicSubject.getTeacher(), subject.getTeacher());
        assertTrue(subject.getPublicSubject());
        assertNotNull(subject.getPriority());
        for(int i=0; i<subject.getItems().size(); i++) {
            assertEquals(publicSubject.getItems().get(i).getLabel(), subject.getItems().get(i).getLabel());
            assertEquals(publicSubject.getItems().get(i).getDescription(), subject.getItems().get(i).getDescription());
            assertEquals(publicSubject.getItems().get(i).getDeadline(), subject.getItems().get(i).getDeadline());
            assertEquals(State.NOT_STARTED, subject.getItems().get(i).getState());
        }
    }

    
    @Test
    public void testToPublicSubject() {
        final Subject subject = new Subject();
        subject.setLabel("label");
        subject.setDescription("description");
        subject.setTeacher("teacher");
        final Item item1 = new Item();
        item1.setLabel("label1");
        item1.setDescription("description1");
        item1.setDeadline(new Date());
        subject.getItems().add(item1);
        final Item item2 = new Item();
        item2.setLabel("label2");
        item2.setDescription("description2");
        item2.setDeadline(new Date());
        subject.getItems().add(item2);

        final PublicSubject publicSubject = SubjectsUtil.toPublicSubject(subject);

        assertEquals(subject.getLabel(), publicSubject.getLabel());
        assertEquals(subject.getDescription(), publicSubject.getDescription());
        assertEquals(subject.getTeacher(), publicSubject.getTeacher());
        for(int i=0; i<subject.getItems().size(); i++) {
            assertEquals(subject.getItems().get(i).getLabel(), publicSubject.getItems().get(i).getLabel());
            assertEquals(subject.getItems().get(i).getDescription(), publicSubject.getItems().get(i).getDescription());
            assertEquals(subject.getItems().get(i).getDeadline(), publicSubject.getItems().get(i).getDeadline());
        }
    }

    @Test
    public void testMerge() {
        final Date date = new Date();
        final Subject subject = new Subject();
        subject.setLabel("label");
        subject.setDescription("description");
        subject.setTeacher("teacher");
        final Item item1 = new Item();
        item1.setId(Long.valueOf(122L));
        item1.setLabel("label1");
        item1.setDescription("description1");
        item1.setDeadline(date);
        item1.setState(State.NOT_STARTED);
        subject.getItems().add(item1);
        final Item item2 = new Item();
        item2.setId(Long.valueOf(123L));
        item2.setLabel("label2");
        item2.setDescription("description2");
        item2.setDeadline(date);
        item2.setState(State.NOT_STARTED);
        subject.getItems().add(item2);
        
        final Subject diff = new Subject();
        diff.setLabel("newLabel");
        //change existing
        final Item diffItem1 = new Item();
        diffItem1.setId(Long.valueOf(122L));
        diffItem1.setLabel("newLabel");
        diffItem1.setDescription("description1");
        diffItem1.setDeadline(date);
        diffItem1.setState(State.DONE);
        diff.getItems().add(diffItem1);
        //dell next one
        //and add one new
        final Item diffItem2 = new Item();
        diffItem2.setLabel("label3");
        diffItem2.setDescription("description3");
        diffItem2.setDeadline(date);
        diffItem2.setState(State.REOPEN);
        diff.getItems().add(diffItem2);

        final List<Item> toRemove = SubjectsUtil.merge(subject, diff);

        assertEquals(item2, toRemove.get(0));

        assertEquals("newLabel", subject.getLabel());
        assertEquals("description", subject.getDescription());
        assertEquals("teacher", subject.getTeacher());
        
        assertEquals("newLabel", subject.getItems().get(0).getLabel());
        assertEquals("description1", subject.getItems().get(0).getDescription());
        assertEquals(date, subject.getItems().get(0).getDeadline());
        assertEquals(State.DONE, subject.getItems().get(0).getState());

        assertEquals("label3", subject.getItems().get(1).getLabel());
        assertEquals("description3", subject.getItems().get(1).getDescription());
        assertEquals(date, subject.getItems().get(1).getDeadline());
        assertEquals(State.REOPEN, subject.getItems().get(1).getState());
    }
}
