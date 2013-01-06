package com.pinalka.services;

import java.util.List;
import java.util.Map;

import com.pinalka.ContextAwareTest;
import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalizationServiceTest extends ContextAwareTest {

    @Autowired
    private LocalizationService localizationService;

    @Test
    public void testGetMessage() {
        assertEquals("name", localizationService.getMessage("profile.name"));
    }

    @Test
    public void testFillCommonForAnonymous() {
        
        final Model model = new ExtendedModelMap();
        final User user = new User();
        user.setName("user");
        user.setRole(UserRole.ROLE_ANONYMOUS);
        
        localizationService.fillCommon(model, user, "page.index");
        
        final Map<String, Object> map = model.asMap();        
        
        assertTrue(map.containsKey("title"));
        assertTrue(map.containsKey("menuLinks"));
        assertTrue(map.containsKey("copyright"));

        final List<LocalizationService.Link> links = (List<LocalizationService.Link>) map.get("menuLinks");

        assertTrue(links.contains(new LocalizationService.Link("/index","")));
        assertTrue(links.contains(new LocalizationService.Link("/about","")));
        assertTrue(links.contains(new LocalizationService.Link("/register","")));
        assertTrue(links.contains(new LocalizationService.Link("/login","")));
        assertFalse(links.contains(new LocalizationService.Link("/schema", "")));
        assertFalse(links.contains(new LocalizationService.Link("/profile/user/edit", "")));
    }

    @Test
    public void testFillCommonForRegistered() {

        final Model model = new ExtendedModelMap();
        final User user = new User();
        user.setName("user");
        user.setRole(UserRole.ROLE_USER);

        localizationService.fillCommon(model, user, "page.index");

        final Map<String, Object> map = model.asMap();

        assertTrue(map.containsKey("title"));
        assertTrue(map.containsKey("menuLinks"));
        assertTrue(map.containsKey("copyright"));

        final List<LocalizationService.Link> links = (List<LocalizationService.Link>) map.get("menuLinks");

        assertTrue(links.contains(new LocalizationService.Link("/index","")));
        assertTrue(links.contains(new LocalizationService.Link("/about","")));
        assertFalse(links.contains(new LocalizationService.Link("/register","")));
        assertTrue(links.contains(new LocalizationService.Link("/logout","")));
        assertTrue(links.contains(new LocalizationService.Link("/schema/user", "")));
        assertTrue(links.contains(new LocalizationService.Link("/profile/user/edit", "")));
    }
}
