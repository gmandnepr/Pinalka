package com.pinalka.controller;

import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.SubjectsService;
import com.pinalka.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author gman
 */
@RunWith(MockitoJUnitRunner.class)
public class SchemaActionsControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SubjectsService subjectsService;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @Mock
    private User user;

    @Mock
    private User owner;

    @InjectMocks
    private SchemaActionsController schemaActionsController;

    @Before
    public void setUpTest() {
        when(user.getId()).thenReturn(Long.valueOf(25L));
        when(user.getName()).thenReturn("user");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getUserByName("user")).thenReturn(user);

        when(owner.getId()).thenReturn(Long.valueOf(26L));
        when(owner.getName()).thenReturn("owner");
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(userService.getUserByName("owner")).thenReturn(owner);
    }

    @Test
    public void testAddPublicAccessible() {
        final Long id = Long.valueOf(10L);
        
        assertEquals("redirect:/schema/user", schemaActionsController.addNewPublicSubjectAction("user", id));

        verify(subjectsService).addNewPublicSubject(user, id);
    }

    @Test
    public void testAddPublicSubjectNotAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema", schemaActionsController.addNewPublicSubjectAction("owner", id));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testAddOwnSubjectAccessible() {
        final Subject subject = mock(Subject.class);

        assertEquals("redirect:/schema/user", schemaActionsController.addNewOwnSubjectAction("user", subject));

        verify(subjectsService).addNewOwnSubject(user, subject);
    }

    @Test
    public void testAddOwnSubjectNotAccessible() {
        final Subject subject = mock(Subject.class);

        assertEquals("redirect:/schema", schemaActionsController.addNewOwnSubjectAction("owner", subject));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testEditSubjectAccessibleNoErrors() {
        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(Long.valueOf(10L));
        when(result.hasErrors()).thenReturn(false);
        
        assertEquals("redirect:/schema/user", schemaActionsController.editSubjectAction("user", subject, result));

        verify(subjectsService).editSubject(user, Long.valueOf(10L), subject);
    }

    @Test
    public void testEditSubjectAccessibleErrors() {
        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(Long.valueOf(10L));
        when(result.hasErrors()).thenReturn(true);

        assertEquals("redirect:/schema/user", schemaActionsController.editSubjectAction("user", subject, result));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testEditSubjectNotAccessible() {
        final Subject subject = mock(Subject.class);

        assertEquals("redirect:/schema", schemaActionsController.editSubjectAction("owner", subject, result));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testRemoveSubjectAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema/user", schemaActionsController.removeSubjectAction("user", id));

        verify(subjectsService).removeSubject(user, id);
    }

    @Test
    public void testRemoveNotAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema", schemaActionsController.removeSubjectAction("owner", id));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testIncPrioritySubjectAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema/user", schemaActionsController.increasePriority("user", id));

        verify(subjectsService).increasePriority(user, id);
    }

    @Test
    public void testIncPriorityNotAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema", schemaActionsController.increasePriority("owner", id));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testDecPrioritySubjectAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema/user", schemaActionsController.decreasePriority("user", id));

        verify(subjectsService).decreasePriority(user, id);
    }

    @Test
    public void testDecPriorityNotAccessible() {
        final Long id = Long.valueOf(10L);

        assertEquals("redirect:/schema", schemaActionsController.decreasePriority("owner", id));

        verifyZeroInteractions(subjectsService);
    }

    @Test
    public void testEditStateAccessible() {
        final Long sid = Long.valueOf(10L);
        final Long iid = Long.valueOf(25L);

        assertEquals("redirect:/schema/user", schemaActionsController.changeStatus("user", sid, iid, State.DONE.toString()));

        verify(subjectsService).updateState(user, sid, iid, State.DONE);
    }

    @Test
    public void testEditStateNotAccessible() {
        final Long sid = Long.valueOf(10L);
        final Long iid = Long.valueOf(25L);

        assertEquals("redirect:/schema", schemaActionsController.changeStatus("owner", sid, iid, State.DONE.toString()));

        verifyZeroInteractions(subjectsService);
    }
}
