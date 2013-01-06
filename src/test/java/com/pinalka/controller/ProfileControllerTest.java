package com.pinalka.controller;

import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import com.pinalka.services.LocalizationService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author gman
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LocalizationService localizationService;

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private User user;

    @Mock
    private User owner;

    @Mock
    private Model model;

    @Before
    public void setUpTest() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getName()).thenReturn("name");
    }

    @Test
    public void testMyProfile() {
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);

        assertEquals("/profile", profileController.myProfile(model));

        verify(model).addAttribute("user", user);
    }
    
    @Test
    public void testProfileByMyName() {
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);
        when(userService.getUserByName("user")).thenReturn(user);
                
        assertEquals("/profile", profileController.publicProfile(model, "user"));

        verify(model).addAttribute("user", user);
    }

    @Test
    public void testProfileByOthersName() {
        when(user.getRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(userService.getUserByName("owner")).thenReturn(owner);

        assertEquals("/profile", profileController.publicProfile(model, "owner"));

        verify(model).addAttribute("user", owner);
    }

    @Test
    public void profileByInvalidName() {
        when(user.getRole()).thenReturn(UserRole.ROLE_ANONYMOUS);
        when(userService.getUserByName("user")).thenReturn(user);

        assertEquals("redirect:/profile", profileController.publicProfile(model, "user"));
    }

    @Test
    public void testUserEditOwnProfile() {
        final String name = "name";
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);
        when(user.getId()).thenReturn(Long.valueOf(1L));
        when(userService.getUserByName(name)).thenReturn(owner);
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(owner.getId()).thenReturn(Long.valueOf(1L));
        when(owner.getName()).thenReturn(name);

        assertEquals("/modifyProfile", profileController.editProfile(model, name));
        
        verify(model).addAttribute("user", owner);
        verify(model).addAttribute("actionUrl", "/profile/name/action/edit");
        verify(model).addAttribute("isAdmin", false);
    }

    @Test
    public void testUserEditOtherProfile() {
        final String name = "name";
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);
        when(user.getId()).thenReturn(Long.valueOf(1L));
        when(userService.getUserByName(name)).thenReturn(owner);
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(owner.getId()).thenReturn(Long.valueOf(2L));

        assertEquals("redirect:/profile", profileController.editProfile(model, name));
    }

    @Test
    public void testAdminEditOtherProfile() {
        final String name = "name";
        when(user.getRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(user.getId()).thenReturn(Long.valueOf(1L));
        when(userService.getUserByName(name)).thenReturn(owner);
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(owner.getId()).thenReturn(Long.valueOf(2L));
        when(owner.getName()).thenReturn(name);

        assertEquals("/modifyProfile", profileController.editProfile(model, name));

        verify(model).addAttribute("user", owner);
        verify(model).addAttribute("actionUrl", "/profile/name/action/edit");
        verify(model).addAttribute("isAdmin", true);        
    }
    
    @Test
    public void testEditProfile() {
        when(user.getRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(user.getId()).thenReturn(Long.valueOf(25L));
        when(user.getName()).thenReturn("admin");
        when(userService.getCurrentUser()).thenReturn(user);
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(owner.getId()).thenReturn(Long.valueOf(26L));
        when(owner.getName()).thenReturn("user");
        when(userService.getUserByName("user")).thenReturn(owner);
        
        assertEquals("redirect:/profile/user", profileController.editProfileAction("user", "", "", new User(), mock(BindingResult.class), model));
    }
    
    @Test
    public void testEditProfileInvalidAction() {
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);
        when(user.getId()).thenReturn(Long.valueOf(25L));
        when(user.getName()).thenReturn("hacker");
        when(userService.getCurrentUser()).thenReturn(user);
        when(owner.getRole()).thenReturn(UserRole.ROLE_USER);
        when(owner.getId()).thenReturn(Long.valueOf(26L));
        when(owner.getName()).thenReturn("user");
        when(userService.getUserByName("user")).thenReturn(owner);

        assertEquals("redirect:/profile", profileController.editProfileAction("user", "", "", new User(), mock(BindingResult.class), model));
    }
}