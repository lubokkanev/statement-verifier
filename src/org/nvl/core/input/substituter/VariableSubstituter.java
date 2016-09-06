package org.nvl.core.input.substituter;

import org.nvl.core.input.split.SplitString;
import org.nvl.core.responder.DividedInput;
import org.nvl.core.variable.VariableType;
import org.nvl.core.variable.manager.VariableManager;

public class VariableSubstituter {
    private VariableManager variableManager;

    public VariableSubstituter(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public DividedInput substitute(DividedInput input) {
        DividedInput result = new DividedInput(input.getLeftSide(), null, input.getOperation());

        result.setRightSide(substituteVariables(input.getRightSide()));
        if (!input.getOperation().equals("=")) {
            result.setLeftSide(substituteVariables(input.getLeftSide()));
        }

        return result;
    }

    private String substituteVariables(String side) {
        SplitString splitString = new SplitString(side);

        while (!splitString.isEmpty()) {
            String element = splitString.getCurrentElement();

            if (variableManager.containsVariable(element)) {
                String value = variableManager.getVariable(element).getValue();
                splitString.setCurrentElement(value);
            }

            splitString.nextPosition();
        }

        return concatenate(splitString.getSplitInput());
    }

    private String concatenate(String[] splitInput) {
        StringBuilder result = new StringBuilder();
        result.append(splitInput[0]);

        for (int i = 1; i < splitInput.length; ++i) {
            result.append(" ").append(splitInput[i]);
        }

        return result.toString();
    }

    private boolean isVariableOfType(String currentElement, VariableType neededType) {
        boolean isVariable = variableManager.containsVariable(currentElement);
        return isVariable && variableManager.getVariable(currentElement).getType() == neededType;
    }
}
