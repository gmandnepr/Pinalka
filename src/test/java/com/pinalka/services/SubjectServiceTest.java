package com.pinalka.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pinalka.dao.ItemDAO;
import com.pinalka.dao.PublicSubjectDAO;
import com.pinalka.dao.SubjectDAO;
import com.pinalka.dao.UserDAO;
import com.pinalka.domain.Item;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.VisibilityPolicy;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.mockito.InjectMocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private SubjectDAO subjectDAO;

    @Mock
    private PublicSubjectDAO publicSubjectDAO;

    @Mock
    private User user;

    @InjectMocks
    private SubjectsService subjectsService;

    private Subject createSubject(final long id, final String label, final boolean isPublic, final int priority) {
        final Subject subject = new Subject();
        subject.setId(id);
        subject.setLabel(label);
        subject.setDescription("no");
        subject.setTeacher("no");    
        subject.setPublicSubject(isPublic);
        subject.setPriority(priority);
        return subject;       
    }
    
    @Before
    public void setUpClass() {
        when(user.getSubjects()).thenReturn(new ArrayList<Subject>(Arrays.<Subject>asList(
                createSubject(0, "subject1", true, 100),
                createSubject(1, "subject2", true, 100),
                createSubject(2, "subject3", true, 100),
                createSubject(3, "subject4", true, 150),
                createSubject(4, "subject5", true, 25),
                createSubject(5, "subject6", false, 99),
                createSubject(6, "subject7", false, 18),
                createSubject(7, "subject8", true, 20),
                createSubject(8, "subject9", true, 15)
        )));
    }
    
    @Test
    public void testOrderedSubjectsOutput() {
        when(user.getVisibilityPolicy()).thenReturn(VisibilityPolicy.EVERY_SUBJECT_VISIBLE);

        final List<Subject> subjects = subjectsService.getUserSortedSubjectsByPolicy(user);

        for(int i=0; i<subjects.size()-1; i++){
            assertTrue(subjects.get(i).getPriority() >= subjects.get(i+1).getPriority());
        }
    }

    @Test
    public void testPublicOrderedOutput() {
        when(user.getVisibilityPolicy()).thenReturn(VisibilityPolicy.PUBLIC_SUBJECT_VISIBLE);

        final List<Subject> subjects = subjectsService.getUserSortedSubjectsByPolicy(user);

        for(int i=0; i<subjects.size()-1; i++){
            assertTrue(subjects.get(i).getPublicSubject());
            assertTrue(subjects.get(i).getPriority() >= subjects.get(i+1).getPriority());
        }
        assertTrue(subjects.get(subjects.size()-1).getPublicSubject());
    }

    @Test
    public void testEmptyOutput() {
        when(user.getVisibilityPolicy()).thenReturn(VisibilityPolicy.NOTHING_VISIBLE);

        final List<Subject> subjects = subjectsService.getUserSortedSubjectsByPolicy(user);

        assertTrue(subjects.isEmpty());
    }

    @Test
    public void testVisibleSortedSubjects() {
        
        final List<Subject> subjects = subjectsService.getUserSortedSubjects(user);

        for(int i=0; i<subjects.size()-1; i++){
            assertTrue(subjects.get(i).getPriority() >= subjects.get(i+1).getPriority());
        }
    }

    @Test
    public void testIncreasePriority() {
        
        subjectsService.increasePriority(user, 5L);
        
        assertEquals(Integer.valueOf(100), user.getSubjects().get(5).getPriority());
        verify(subjectDAO).update(user.getSubjects().get(5));
    }

    @Test
    public void testDecreasePriority() {

        subjectsService.decreasePriority(user, 5L);

        assertEquals(Integer.valueOf(98), user.getSubjects().get(5).getPriority());
        verify(subjectDAO).update(user.getSubjects().get(5));
    }

    @Test
    public void testAddPublicSubject() {
        
        final PublicSubject ps = new PublicSubject();
        ps.setRate(1);
        when(publicSubjectDAO.getById(5L)).thenReturn(ps);

        subjectsService.addNewPublicSubject(user, 5L);

        verify(publicSubjectDAO).getById(5L);
        verify(userDAO).update(user);
        verify(publicSubjectDAO).update(ps);//verify increase of popularity
    }
    
    @Test
    public void testAddNonExistingPublicSubject() {
        
        when(publicSubjectDAO.getById(25L)).thenReturn(null);
        
        subjectsService.addNewPublicSubject(user, 25L);
        
        verify(publicSubjectDAO).getById(25L);
        verifyZeroInteractions(userDAO);
        verify(publicSubjectDAO, times(0)).update(null);
    }
    
    @Test
    public void testAddSubject() {
        
        subjectsService.addNewOwnSubject(user, new Subject());

        verify(userDAO).update(user);
        verifyZeroInteractions(publicSubjectDAO);
    }
    
    @Test
    public void testAddSubjectAndMakeItPublic() {
        
        final Subject subject = new Subject();
        subject.setPublicSubject(Boolean.TRUE);
        
        subjectsService.addNewOwnSubject(user, subject);

        verify(userDAO).update(user);
        verify(publicSubjectDAO).create(any(PublicSubject.class));
    }

    @Test
    public void testEditSubject() {

        subjectsService.editSubject(user, 5L, new Subject());
        
        verify(subjectDAO).update(user.getSubjects().get(5));
        verifyZeroInteractions(publicSubjectDAO);
    }
    
    @Test
    public void testEditPublicSubject() {
        
        user.getSubjects().get(5).setPublicSubject(true);

        subjectsService.editSubject(user, 5L, new Subject());

        verify(subjectDAO).update(user.getSubjects().get(5));
        verify(publicSubjectDAO).create(any(PublicSubject.class));
    }

    @Test
    public void testEditNonExisting() {

        subjectsService.editSubject(user, 20L, new Subject());

        verifyZeroInteractions(subjectDAO);
    }

    @Test
    public void testRemoveSubject() {

        subjectsService.removeSubject(user, 5L);

        final Subject s = new Subject();
        s.setId(5L);
        assertFalse(user.getSubjects().contains(s));

        verify(userDAO).update(user);
    }

    @Test
    public void testRemoveNotExisting() {

        subjectsService.removeSubject(user, 20L);

        verifyZeroInteractions(userDAO);
    }

    @Test
    public void testGetSubjectById() {
        
        final Subject s1 = subjectsService.getSubjectById(user, 1L);
        assertSame(user.getSubjects().get(1), s1);
        
        final Subject s2 = subjectsService.getSubjectById(user, 10L);
        assertNull(s2);
    }
    
    @Test
    public void testGetItemById() {
        
        final Item item = new Item();
        item.setId(10L);
        user.getSubjects().get(5).getItems().add(item);
        
        final Item i1 = subjectsService.getItemById(user, 5L, 10L);
        assertSame(user.getSubjects().get(5).getItems().get(0), i1);
        
        final Item i2 = subjectsService.getItemById(user, 5L, 11L);
        assertNull(i2);
    }
    
    @Test
    public void testUpdateState() {

        final Item item = spy(new Item());
        item.setId(10L);
        user.getSubjects().get(5).getItems().add(item);

        subjectsService.updateState(user, 5L, 10L, State.DONE);

        verify(itemDAO).update(item);
        verify(item).setState(State.DONE);
    }
    
}
