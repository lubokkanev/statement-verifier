package com.nvl.verifier.processor;

import com.nvl.variable.EvaluatedVariable;

import java.util.Set;

/**
 * Processes the given requests
 */
public interface RequestProcessor {
    void addVariable(String variableDefinition);

    void updateVariable(String variableDefinition);

    boolean verifyStatement(String statement);

    Set<EvaluatedVariable> variables();
}
