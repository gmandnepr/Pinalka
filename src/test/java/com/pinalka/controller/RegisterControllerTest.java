package com.pinalka.controller;

import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author gman
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @Mock
    private User user;

    @InjectMocks
    private RegisterController registerController;

    @Test
    public void testShowRegistrationForm() {

        assertEquals("/register", registerController.register(model));
    }
    
    @Test
    public void testRegisterActionOk() {
        when(user.getName()).thenReturn("name");
        when(result.hasErrors()).thenReturn(false);
        when(userService.isNameOccupied("name")).thenReturn(false);
        
        assertEquals("redirect:/login", registerController.registerAction("pass123", "pass123", user, result, model));
    }

    @Test
    public void testRegistrationActionFail() {
        when(result.hasErrors()).thenReturn(true);

        assertEquals("/register", registerController.registerAction("pass123", "pass", user, result, model));

        verify(result).addError(any(FieldError.class));
    }
}
