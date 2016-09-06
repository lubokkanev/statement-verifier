package org.nvl.core.variable.manager;

import org.nvl.core.variable.EvaluatedVariable;

import java.util.Set;

/**
 * Manages the variables in the current environment
 */
public interface VariableManager {
    void addVariable(EvaluatedVariable variable);

    void removeVariable(String name);

    void updateVariable(EvaluatedVariable variable);

    boolean containsVariable(String name);

    EvaluatedVariable getVariable(String name);

    Set<EvaluatedVariable> variables();
}
