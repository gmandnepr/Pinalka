package com.pinalka.util;

import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author gman
 */
public class UserUtilTest {

    @Test
    public void testAnonymousUser() {
        User user = mock(User.class);
        when(user.getRole()).thenReturn(UserRole.ROLE_ANONYMOUS);

        assertTrue(UserUtils.isAnonymousUser(user));

        user = mock(User.class);
        when(user.getRole()).thenReturn(UserRole.ROLE_USER);

        assertFalse(UserUtils.isAnonymousUser(user));

        user = mock(User.class);
        when(user.getRole()).thenReturn(UserRole.ROLE_ADMIN);

        assertFalse(UserUtils.isAnonymousUser(user));
    }

    @Test
    public void testNullFields() throws IllegalAccessException {
        final User user = new User();

        UserUtils.fillNullFields(user);

        assertNull(user.getId());
        assertNotNull(user.getPass());
        assertNotNull(user.getRole());
        assertNotNull(user.getEmail());
        assertNotNull(user.getVisibilityPolicy());
        assertNotNull(user.getLocale());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getLastActionDate());
    }

    @Test
    public void testPassEncryption() {
        final User user = new User();
        user.setPass("pass");
        UserUtils.encodePassword(user);
        assertEquals("1a1dc91c907325c69271ddf0c944bc72", user.getPass());
    }

    @Test
    public void testNullSafeAnonymousUser() throws IllegalAccessException {
        final User user = UserUtils.createAnonymousUser();
        assertEquals(Long.valueOf(-1L), user.getId());
        assertNotNull(user.getPass());
        assertNotNull(user.getRole());
        assertNotNull(user.getEmail());
        assertNotNull(user.getVisibilityPolicy());
        assertNotNull(user.getLocale());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getLastActionDate());
    }

    @Test
    public void testSame() {
        final User user1 = mock(User.class);
        when(user1.getId()).thenReturn(Long.valueOf(1l));
        final User user2 = mock(User.class);
        when(user2.getId()).thenReturn(Long.valueOf(1l));
        final User user3 = mock(User.class);
        when(user3.getId()).thenReturn(Long.valueOf(2l));

        assertTrue(UserUtils.isSame(user1, user1));
        assertTrue(UserUtils.isSame(user1, user2));
        assertFalse(UserUtils.isSame(user1, user3));
    }

    @Test
    public void testAccessibility() {
        final User anonymous = UserUtils.createAnonymousUser();
        final User user1 = mock(User.class);
        when(user1.getId()).thenReturn(Long.valueOf(1l));
        when(user1.getRole()).thenReturn(UserRole.ROLE_USER);
        final User user2 = mock(User.class);
        when(user2.getId()).thenReturn(Long.valueOf(2l));
        when(user2.getRole()).thenReturn(UserRole.ROLE_USER);
        final User admin = mock(User.class);
        when(admin.getId()).thenReturn(Long.valueOf(3l));
        when(admin.getRole()).thenReturn(UserRole.ROLE_ADMIN);

        assertFalse(UserUtils.isAccessible(anonymous, admin));
        assertFalse(UserUtils.isAccessible(anonymous, user1));
        assertFalse(UserUtils.isAccessible(user1, anonymous));
        assertFalse(UserUtils.isAccessible(user1, user2));
        assertTrue(UserUtils.isAccessible(user1, user1));
        assertTrue(UserUtils.isAccessible(user1, admin));
    }
    
    @Test
    public void testMerge() {
        final User user = new User();
        final User diff = new User();

        diff.setRole(UserRole.ROLE_ADMIN);
        diff.setEmail("a@a.aa");

        UserUtils.merge(user, diff, "pass");

        assertEquals(UserRole.ROLE_ADMIN, user.getRole());
        assertEquals("a@a.aa", user.getEmail());
        assertEquals("1a1dc91c907325c69271ddf0c944bc72", user.getPass());
    }
}
