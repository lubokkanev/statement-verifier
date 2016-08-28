package com.nvl.variable.manager;

import com.nvl.variable.EvaluatedVariable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableManagerImpl implements VariableManager {
    private Map<String, EvaluatedVariable> allVariables;

    public VariableManagerImpl() {
        allVariables = new HashMap<>();
    }

    public void addVariable(EvaluatedVariable variable) {
        allVariables.put(variable.getName(), variable);
    }

    public void removeVariable(String name) {
        allVariables.remove(name);
    }

    public void updateVariable(EvaluatedVariable variable) {
        EvaluatedVariable variableToUpdate = allVariables.get(variable.getName());
        variableToUpdate.setValue(variable.getValue());
        variableToUpdate.setType(variable.getType());
    }

    @Override
    public boolean containsVariable(String name) {
        return allVariables.containsKey(name);
    }

    @Override
    public EvaluatedVariable getVariable(String name) {
        return allVariables.get(name);
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return new HashSet<>(allVariables.values());
    }
}
