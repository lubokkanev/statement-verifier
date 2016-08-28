package com.nvl.variable.manager;

import com.nvl.variable.EvaluatedVariable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ListVariableManager implements VariableManager {
    private final String INVALID_INPUT_MESSAGE = "You found a bug! Invalid input. ";
    private List<EvaluatedVariable> variables;

    public ListVariableManager(List<EvaluatedVariable> variables) {
        this.variables = variables;
    }

    public void addVariable(EvaluatedVariable variable) {
        variables.add(variable);
    }

    public void removeVariable(String name) {
        variables.remove(new EvaluatedVariable(null, null, name));
    }

    public void updateVariable(EvaluatedVariable variable) {
        if (!containsVariable(variable.getName())) {
            throw new RuntimeException(INVALID_INPUT_MESSAGE);
        }

        EvaluatedVariable variableToUpdate = getVariable(variable.getName());

        variableToUpdate.setValue(variable.getValue());
        variableToUpdate.setType(variable.getType());
    }

    @Override
    public boolean containsVariable(String name) {
        EvaluatedVariable variable = getVariable(name);

        return variable != null;
    }

    @Override
    public EvaluatedVariable getVariable(String name) {
        EvaluatedVariable variable = null;
        for (int i = 0; i < variables.size(); ++i) {
            if (variables.get(i).getName().equals(name)) {
                variable = variables.get(i);
            }
        }

        return variable;
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return new TreeSet<>(variables);
    }
}
