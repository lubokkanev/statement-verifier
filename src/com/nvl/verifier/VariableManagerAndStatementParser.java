package com.nvl.verifier;

import com.nvl.variable.Variable;

import java.util.Set;

public interface VariableManagerAndStatementParser {
    void addVariable(String variableDefinition);

    void updateVariable(String variableDefinition);

    boolean evaluateStatement(String statement);

    Set<Variable> variables();
}
