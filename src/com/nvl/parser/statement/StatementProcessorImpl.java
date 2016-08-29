package com.nvl.parser.statement;

import com.nvl.variable.manager.VariableManager;

public class StatementProcessorImpl implements StatementProcessor {
    private VariableManager variableManager;

    private static final String INVALID_INPUT_MESSAGE = "You caught a bug! Invalid input. ";

    public StatementProcessorImpl(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean verifyStatement(String statement) {
        StringBuilder valueStatement = new StringBuilder(statement);

        for (int i = 0; i < valueStatement.length(); ++i) {
            char character = valueStatement.charAt(i);

            if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {
                String variable = String.valueOf(character);

                if (!variableManager.containsVariable(variable)) {
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
                valueStatement.replace(i, i + 1, variableManager.getVariable(variable).getValue());
            }
        }

        return correct(valueStatement);
    }

    private boolean correct(StringBuilder builder) {
        // TODO: Niki / Lubo
        return true;
    }
}
