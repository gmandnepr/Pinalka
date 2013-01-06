package com.pinalka.services;

import com.pinalka.dao.UserDAO;
import com.pinalka.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetById() {

        userService.getUserById(10L);

        verify(userDAO).getById(10L);
    }

    @Test
    public void testGetByName() {

        userService.getUserByName("name");

        verify(userDAO).getByName("name");
    }

    @Test
    public void testNameOccupied() {
        
        userService.isNameOccupied("name");
        
        verify(userDAO).isNameOccupied("name");
    }
    
    @Test
    public void testRegistration() {
        User user = spy(new User());

        userService.register(user, "pass");

        verify(user).setPass(anyString());
        verify(userDAO).create(user);
    }
    
    @Test
    public void testUserUpdate() {
        
        final User user = new User();
        user.setId(Long.valueOf(25L));
        
        final User diff = new User();//no diff
        
        userService.updateDetail(user, diff, "pass");
        
        verify(userDAO).update(user);
        assertEquals("1a1dc91c907325c69271ddf0c944bc72", user.getPass());
    }

}
