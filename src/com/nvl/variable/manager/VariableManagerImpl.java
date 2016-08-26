package com.nvl.variable.manager;

import com.nvl.variable.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableManagerImpl implements VariableManager {
	private Map<String, Variable> allVariables;

	public VariableManagerImpl() {
		allVariables = new HashMap<>();
	}

	public void addVariable(Variable variable) {
		allVariables.put(variable.getName(), variable);
	}

	public void removeVariable(String name) {
		allVariables.remove(name);
	}

	public void updateVariable(Variable variable) {
		Variable variableToUpdate = allVariables.get(variable.getName());
		variableToUpdate.setValue(variable.getValue());
	}

	@Override
	public Set<Variable> variables() {
		return new HashSet<>(allVariables.values());
	}
}
