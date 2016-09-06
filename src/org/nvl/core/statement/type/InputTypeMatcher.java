package org.nvl.core.statement.type;

import org.nvl.core.variable.manager.VariableManager;

public class InputTypeMatcher {
    private VariableManager variableManager;

    public InputTypeMatcher(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    public boolean sidesTypeMatches(String leftSide, String rightSide) {
        boolean leftSideHasArrays = leftSide.contains("{");
        boolean leftSideHasStrings = leftSide.contains("'");
        boolean rightSideHasArrays = rightSide.contains("{");
        boolean rightSideHasStrings = rightSide.contains("'");

        return leftSideHasArrays == rightSideHasArrays && leftSideHasStrings == rightSideHasStrings && !(leftSideHasArrays && leftSideHasStrings);
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
        String[] specialDividers = new String[] {"<=", ">=", "==", "!="};
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
