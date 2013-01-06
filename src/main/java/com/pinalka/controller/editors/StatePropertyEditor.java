package com.pinalka.controller.editors;

import java.beans.PropertyEditorSupport;

import com.pinalka.domain.State;
import com.pinalka.services.LocalizationService;

/**
 * Property editor for the {@link State} enum
 *
 * @author gman
 */
public class StatePropertyEditor extends PropertyEditorSupport {

    private final LocalizationService localizationService;

    /**
     * Constructor
     * @param localizationService to take names
     */
    public StatePropertyEditor(final LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public String getAsText() {
        final State state = (State) getValue();
        return localizationService.getMessage(state.getFullKey());
    }

    @Override
    public void setAsText(final String text) {
        setValue(State.valueOf(text));
    }
}
