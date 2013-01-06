package com.pinalka.controller;

import java.util.Collections;
import java.util.List;

import com.pinalka.domain.Item;
import com.pinalka.domain.PublicSubject;
import com.pinalka.domain.State;
import com.pinalka.domain.Subject;
import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.PublicSubjectService;
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
public class SchemaControllerTest {
    
    @Mock
    private UserService userService;

    @Mock
    private SubjectsService subjectsService;

    @Mock
    private PublicSubjectService publicSubjectService;

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
    private SchemaController schemaController;
    
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
    public void testMySchema() {
        final List<Subject> subjects = Collections.emptyList();
        
        assertEquals("/schema", schemaController.mySchema(model));

        verify(model).addAttribute("ownSchema", true);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subjects", subjects);
    }

    @Test
    public void testMySchemaViaPublic() {
        final List<Subject> subjects = Collections.emptyList();

        assertEquals("/schema", schemaController.publicSchema(model, "user"));

        verify(model).addAttribute("ownSchema", true);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subjects", subjects);
    }

    @Test
    public void testPublicSchema() {
        final List<Subject> subjects = Collections.emptyList();

        assertEquals("/schema", schemaController.publicSchema(model, "owner"));

        verify(model).addAttribute("ownSchema", false);
        verify(model).addAttribute("user", owner);
        verify(model).addAttribute("subjects", subjects);
    }

    @Test
    public void testAddPublicSubjectAccessible() {
        final List<PublicSubject> subjects = Collections.emptyList();
        
        assertEquals("/subjectList", schemaController.addNewPublicSubject(model, "user"));

        verify(publicSubjectService).getPublicSubjects();
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subjects", subjects);
    }

    @Test
    public void testAddPublicSubjectNotAccessible() {

        assertEquals("redirect:/schema", schemaController.addNewPublicSubject(model, "owner"));

        verifyZeroInteractions(publicSubjectService);
    }

    @Test
    public void testAddRecommendedSubjectAccessible() {
        final List<PublicSubject> subjects = Collections.emptyList();

        assertEquals("/subjectList", schemaController.addNewRecommendedSubject(model, "user"));

        verify(publicSubjectService).getRecommendedPublicSubjects(user);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subjects", subjects);
    }

    @Test
    public void testAddRecommendedSubjectNotAccessible() {

        assertEquals("redirect:/schema", schemaController.addNewRecommendedSubject(model, "owner"));

        verifyZeroInteractions(publicSubjectService);
    }

    @Test
    public void testAddOwnSubjectAccessible() {

        assertEquals("/modifySubject", schemaController.addNewOwnSubject(model, "user"));

        verify(model).addAttribute("actionUrl", "/schema/user/action/add/own");
        verify(model).addAttribute("user", user);
        verify(model).addAttribute(eq("subject"), any(Subject.class));
    }

    @Test
    public void testAddOwnSubjectNotAccessible() {

        assertEquals("redirect:/schema", schemaController.addNewOwnSubject(model, "owner"));
    }
    
    @Test
    public void testEditSubjectAccessible() {
        final Subject subject = mock(Subject.class);
        
        when(subjectsService.getSubjectById(user, Long.valueOf(10L))).thenReturn(subject);
        
        assertEquals("/modifySubject", schemaController.editSubject(model, "user", Long.valueOf(10L)));

        verify(subjectsService).getSubjectById(user, Long.valueOf(10L));
        verify(model).addAttribute("actionUrl", "/schema/user/action/edit");
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subject", subject);
    }
    
    @Test
    public void testEditSubjectNotAccessible() {

        assertEquals("redirect:/schema", schemaController.editSubject(model, "owner", Long.valueOf(10L)));
    }
    
    @Test
    public void testEditStateAccessible() {
        final Subject subject = mock(Subject.class);
        final Item item = mock(Item.class);
        when(item.getState()).thenReturn(State.DONE);
        when(subjectsService.getSubjectById(user, Long.valueOf(10L))).thenReturn(subject);
        when(subjectsService.getItemById(user, Long.valueOf(10L), Long.valueOf(42L))).thenReturn(item);

        assertEquals("/changeState", schemaController.changeState(model, "user", Long.valueOf(10L), Long.valueOf(42L)));

        verify(subjectsService).getItemById(user, Long.valueOf(10L), Long.valueOf(42L));
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("subject", subject);
        verify(model).addAttribute("item", item);
    }
    
    @Test
    public void testEditStateNotAccessible() {

        assertEquals("redirect:/schema", schemaController.changeState(model, "owner", Long.valueOf(10L), Long.valueOf(42L)));
    }
}
