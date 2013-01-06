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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author gman
 */
@RunWith(MockitoJUnitRunner.class)
public class AboutControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private Model model;

    @Mock
    private User user;

    @InjectMocks
    private AboutController aboutController;

    @Test
    public void testAbout() {
        when(userService.getCurrentUser()).thenReturn(user);

        assertEquals("/about", aboutController.about(model));

        verify(model).addAttribute(eq("text"), anyString());
    }
}
