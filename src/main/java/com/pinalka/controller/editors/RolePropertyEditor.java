package com.pinalka.controller.editors;

import java.beans.PropertyEditorSupport;

import com.pinalka.domain.UserRole;
import com.pinalka.services.LocalizationService;

/**
 * Property editor for the {@link UserRole}
 * 
 * @author gman
 */
public class RolePropertyEditor extends PropertyEditorSupport {

    private final LocalizationService localizationService;

    /**
     * Constructor
     * @param localizationService to take names
     */
    public RolePropertyEditor(final LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public String getAsText() {
        final UserRole role = (UserRole) getValue();
        return localizationService.getMessage(role.getFullKey());
    }

    @Override
    public void setAsText(String text) {
        setValue(UserRole.valueOf(text));
    }
}
