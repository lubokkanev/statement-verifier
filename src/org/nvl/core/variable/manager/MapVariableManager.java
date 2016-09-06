package org.nvl.core.variable.manager;

import org.nvl.core.variable.EvaluatedVariable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapVariableManager implements VariableManager {
    private final String INVALID_INPUT_MESSAGE = "You found a bug! Invalid input. ";
    private Map<String, EvaluatedVariable> allVariables;

    public MapVariableManager(HashMap<String, EvaluatedVariable> hashMap) {
        allVariables = hashMap;
    }

    public void addVariable(EvaluatedVariable variable) {
        allVariables.put(variable.getName(), variable);
    }

    public void removeVariable(String name) {
        allVariables.remove(name);
    }

    public void updateVariable(EvaluatedVariable variable) {
        if (!allVariables.containsKey(variable.getName())) {
            throw new RuntimeException(INVALID_INPUT_MESSAGE);
        }

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
