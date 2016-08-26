package com.nvl.verifier;

import com.nvl.variable.Variable;

import java.util.Set;

public interface InputProcessor {
    void addVariable(String variableDefinition);

    void updateVariable(String variableDefinition);

    boolean verifyStatement(String statement);

    Set<Variable> variables();
}
