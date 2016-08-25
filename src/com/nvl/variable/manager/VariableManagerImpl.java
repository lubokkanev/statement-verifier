package com.nvl.variable.manager;

import com.nvl.parser.value.EvaluatedResult;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.variable.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableManagerImpl implements VariableManager {
    private Map<String, Variable> allVariables;
    private VariableTypeParser variableTypeParser;

    public VariableManagerImpl(VariableTypeParser variableTypeParser) {
        this.variableTypeParser = variableTypeParser;
        allVariables = new HashMap<>();
    }

    public void addVariable(String name, String value) {
        EvaluatedResult result = variableTypeParser.parse(value);
        // TODO: Lubo - like the other one, remove the parser from this class
        // TODO: Lubo - maybe change the manager with just a map
        Variable newVariable = new Variable(name, result.getType(), result.getValue());
        allVariables.put(name, newVariable);
    }

    public void removeVariable(String name) {
        allVariables.remove(name);
    }

    public void updateVariable(String name, String value) {
        Variable variable = allVariables.get(name);
        variable.setValue(value);
    }

    @Override
    public Set<Variable> variables() {
        return new HashSet<>(allVariables.values());
    }
}
