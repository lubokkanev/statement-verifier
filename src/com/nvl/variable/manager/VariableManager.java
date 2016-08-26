package com.nvl.variable.manager;

import com.nvl.variable.Variable;

import java.util.Set;

public interface VariableManager {
    void addVariable(Variable variable);

    void removeVariable(String name);

    void updateVariable(Variable variable);

    Set<Variable> variables();
}
