package com.nvl.verifier.validator;

import com.nvl.variable.manager.VariableManager;

public class GrammarInputValidator implements InputValidator {
    private VariableManager variableManager;

    public GrammarInputValidator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean isValid(String input) {
        // TODO: Vicky
        return true;
    }
}
