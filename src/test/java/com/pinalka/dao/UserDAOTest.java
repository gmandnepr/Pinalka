package com.pinalka.dao;

import java.util.List;

import com.pinalka.IntegrationTest;
import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserDAOTest extends IntegrationTest {

    @Autowired
    private UserDAO userDAO;

    @Before
    public void setUpClass() {
        reInitDataFromTheDump();
    }

    @Test
    public void testFindById() {
        final User user = userDAO.getById(1L);

        assertEquals(user.getId(), Long.valueOf(1L));
        assertEquals(user.getName(), "user");
    }
    
    @Test
    public void testFindByInvalidId() {
        
        final User user = userDAO.getById(25L);
        
        assertNull(user);
    }
        
    @Test
    public void testFindByName() {
        final User user = userDAO.getByName("user");

        assertEquals(user.getId(), Long.valueOf(1L));
        assertEquals(user.getName(), "user");
    }

    @Test
    public void testFindByInvalidName() {
        final User user = userDAO.getByName("noSuchUser");
        
        assertEquals(Long.valueOf(-1L), user.getId());
        assertEquals(UserRole.ROLE_ANONYMOUS ,user.getRole());
    }
    
    @Test
    public void testList() {
        final List<User> users = userDAO.getList();

        assertEquals(users.size(), 2);
        assertEquals(users.get(0).getName(), "user");
        assertEquals(users.get(1).getName(), "admin");
    }

    @Test
    public void testManipulations() {
        final User u = new User();
        final int size1 = userDAO.getList().size();
        userDAO.create(u);
        final int size2 = userDAO.getList().size();
        userDAO.delete(u);
        final int size3 = userDAO.getList().size();

        assertEquals(size1, size3);
        assertEquals(size1+1, size2);
    }
    
    @Test
    public void testNameOccupied() {
        
        assertTrue(userDAO.isNameOccupied("admin"));
        assertFalse(userDAO.isNameOccupied("new name"));
    }
}
