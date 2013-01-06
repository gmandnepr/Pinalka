package com.pinalka.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * This class will provide localized messages
 * 
 * @author gman
 */
@Service
public class LocalizationService implements MessageSourceAware {

    private static final Locale DEFAULT_LOCALE = Locale.US;
    
    private MessageSource messageSource;
    
    @Override
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Obtain localized message by the key
     *
     * @param key message key
     * @param objs optional objects to populate message
     * @return localized messages
     */
    public String getMessage(final String key, final Object... objs) {
        return messageSource.getMessage(key, objs, DEFAULT_LOCALE);
    }

    /**
     * Populate model wit common page information such as title, copyright and menu links
     *
     * @param model to populate
     * @param user to determine links visibility
     * @param titleKey title
     */
    public void fillCommon(final Model model, final User user, final String titleKey) {
        model.addAttribute("title", getMessage(titleKey));
        model.addAttribute("menuLinks", getMenuLinks(user));
        model.addAttribute("copyright", getMessage("copyright"));
    }

    /**
     * Provide links for the main menu
     *
     * @param user to determine links visibility
     * @return list with links
     */
    private List<Link> getMenuLinks(final User user) {
        final boolean isAuthorized = user.getRole() == UserRole.ROLE_USER ||
                user.getRole() == UserRole.ROLE_ADMIN;
        final List<Link> links = new ArrayList<Link>();
        links.add(new Link("/index", getMessage("page.index")));
        if (isAuthorized) {
            links.add(new Link(String.format("/schema/%s", user.getName()), getMessage("page.schema")));
            links.add(new Link(String.format("/profile/%s/edit",user.getName()), getMessage("page.profile")));
        }
        links.add(new Link("/about", getMessage("page.about")));
//        links.add(new Link("/idea", getMessage("page.idea")));
        if (isAuthorized) {
            links.add(new Link("/logout", getMessage("page.logout", user.getName())));
        } else {
            links.add(new Link("/register", getMessage("page.register")));
            links.add(new Link("/login", getMessage("page.login")));
        }
        return links;
    }

    /**
     * This class represents link
     *
     * @author gman
     */
    public static class Link {
        private final String href;
        private final String label;

        public Link(final String href, final String label) {
            this.href = href;
            this.label = label;
        }

        /**
         * Obtain link
         *
         * @return the link
         */
        public String getHref() {
            return href;
        }

        /**
         * Obtain label of the link
         *
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Link link = (Link) o;

            return href!=null && href.equals(link.href);
        }

        @Override
        public int hashCode() {
            return href != null ? href.hashCode() : 0;
        }
    }
}
