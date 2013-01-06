package com.pinalka.controller.editors;

import java.beans.PropertyEditorSupport;

import com.pinalka.domain.VisibilityPolicy;
import com.pinalka.services.LocalizationService;

/**
 * Property editor for the {@link VisibilityPolicy}
 *
 * @author gman
 */
public class VisibilityPolicyPropertyEditor extends PropertyEditorSupport {

    private final LocalizationService localizationService;

    /**
     * Constructor
     * @param localizationService to take names
     */
    public VisibilityPolicyPropertyEditor(final LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public String getAsText() {
        final VisibilityPolicy visibilityPolicy = (VisibilityPolicy) getValue();
        return localizationService.getMessage(visibilityPolicy.getFullKey());
    }

    @Override
    public void setAsText(final String text) {
        setValue(VisibilityPolicy.valueOf(text));
    }
}
