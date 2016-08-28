package com.nvl.verifier.determiner;

import com.nvl.variable.manager.VariableManager;

public class SimpleInputTypeDeterminer implements InputTypeDeterminer {
    private VariableManager variableManager;

    public SimpleInputTypeDeterminer(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public InputType determineType(String input) {
        if (isDefinition(input)) {
            String variableName = input.substring(0, input.indexOf('=')).trim();
            return determineVariableDefinition(variableName);
        } else {
            return InputType.STATEMENT;
        }
    }

    private InputType determineVariableDefinition(String variableName) {
        if (variableManager.containsVariable(variableName)) {
            return InputType.EXISTING_VARIABLE;
        } else {
            return InputType.NEW_VARIABLE;
        }
    }

    private boolean isDefinition(String input) {
        return input.contains("=") && !input.contains("==");
    }
}
