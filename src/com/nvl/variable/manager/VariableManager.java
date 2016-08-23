package com.nvl.variable.manager;

import com.nvl.variable.Variable;

import java.util.Set;

public interface VariableManager {
    void addVariable(String name, String value);

    void removeVariable(String name);

    void updateVariable(String name, String value);

    Set<Variable> variables();
}
