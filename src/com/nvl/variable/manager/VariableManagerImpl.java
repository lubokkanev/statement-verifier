package com.nvl.variable.manager;

import com.nvl.parser.value.EvaluatedResult;
import com.nvl.parser.value.ValueParser;
import com.nvl.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class VariableManagerImpl implements VariableManager {
    private Map<String, Variable> allVariables;
    private ValueParser valueParser;

    public VariableManagerImpl(ValueParser valueParser) {
        this.valueParser = valueParser;
        allVariables = new HashMap<>();
    }

    public void addVariable(String name, String value) {
        EvaluatedResult result = valueParser.parse(value);
        allVariables.put(name, new Variable(name, result.getType(), result.getValue()));
    }

    public void removeVariable(String name) {
        allVariables.remove(name);
    }

    public void updateVariable(String name, String value) {
        Variable variable = allVariables.get(name);
        variable.setValue(value);
    }
}
