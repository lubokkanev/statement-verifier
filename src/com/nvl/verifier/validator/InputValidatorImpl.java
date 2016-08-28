package com.nvl.verifier.validator;

import com.nvl.variable.manager.VariableManager;

public class InputValidatorImpl implements InputValidator {
    private VariableManager variableManager;

    public InputValidatorImpl(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean isValid(String input) {
        // TODO: Vicky
        return true;
    }
}
