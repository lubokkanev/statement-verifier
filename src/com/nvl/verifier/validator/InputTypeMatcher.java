package com.nvl.verifier.validator;

import com.nvl.variable.manager.VariableManager;

public class InputTypeMatcher {
    private VariableManager variableManager;

    public InputTypeMatcher(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public boolean sidesTypeMatches(String input) {
        String[] sideSplit = sideSplitInput(input);

        if (sideSplit == null) {
            return false;
        } else if (sideSplit.length != 2) {
            return true;
        }

        replaceWithValue(sideSplit);

        boolean arrayProblem = sideSplit[0].contains("{") && !sideSplit[1].contains("{") || !sideSplit[0].contains("{") && sideSplit[1].contains("{");
        boolean stringProblem = sideSplit[0].contains("'") && !sideSplit[1].contains("'") || !sideSplit[0].contains("'") && sideSplit[1].contains("'");

        return !arrayProblem && !stringProblem;
    }

    private void replaceWithValue(String[] sideSplit) {
        String[][] sideSplits = new String[][] {sideSplit[0].split(" "), sideSplit[1].split(" ")};
        String[] specialCharacters = new String[] {"{", "'"};

        for (int j = 0; j < sideSplit.length; ++j) {
            for (int i = 0; i < sideSplits[j].length; i++) {
                String element = sideSplits[j][i];

                if (variableManager.containsVariable(element)) {
                    String value = variableManager.getVariable(element).getValue();

                    for (String character : specialCharacters) {
                        if (value.contains(character)) {
                            sideSplit[j] += character;
                            break;
                        }
                    }
                }
            }
        }
    }

    private String[] sideSplitInput(String input) {
        String[] specialDividers = new String[] {"<=", ">=", "=="};
        String[] sideSplit = input.split("[<>]");
        boolean foundDivider = false;

        for (String divider : specialDividers) {
            if (input.contains(divider)) {
                if (foundDivider) {
                    return null;
                }

                sideSplit = input.split(divider);
                foundDivider = true;
            }
        }

        return sideSplit;
    }
}
