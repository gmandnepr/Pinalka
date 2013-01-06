package com.pinalka.controller;

import junit.framework.TestCase;

import com.pinalka.domain.User;
import com.pinalka.services.LocalizationService;
import com.pinalka.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author gman
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest extends TestCase {

    @Mock
    private UserService userService;

    @Mock
    private LocalizationService localizationService;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private User user;

    @Mock
    private Model model;

    @Before
    public void setUpTest() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getName()).thenReturn("name");
    }

    @Test
    public void testLogin() {

        assertEquals("/login", loginController.login(model));
        verify(model).addAttribute("isFail", false);
    }

    @Test
    public void testLoginAfterFail() {

        assertEquals("/login", loginController.loginFail(model));
        verify(model).addAttribute("isFail", true);
    }
}
