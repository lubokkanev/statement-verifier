package com.nvl.parser.statement;

import com.nvl.variable.manager.VariableManager;

public class StatementProcessorImpl implements StatementProcessor {
    private VariableManager variableManager;

    public StatementProcessorImpl(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean verifyStatement(String statement) {
        // TODO: Niki / Vicky
        return false;
    }
}
