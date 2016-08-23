package com.nvl.variable.manager;

public interface VariableManager {
    void addVariable(String name, String value);

    void removeVariable(String name);

    void updateVariable(String name, String value);
}
