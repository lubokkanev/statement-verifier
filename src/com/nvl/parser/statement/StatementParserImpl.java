package com.nvl.parser.statement;

import com.nvl.variable.manager.VariableManager;

public class StatementParserImpl implements StatementParser {
    private VariableManager variableManager;

    public StatementParserImpl(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean evaluateStatement(String statement) {
        // TODO: Niki / Vicky
        return false;
    }
}
