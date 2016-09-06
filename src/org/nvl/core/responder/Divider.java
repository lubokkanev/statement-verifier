package org.nvl.core.responder;

import org.nvl.MessageConstants;

public class Divider {
    public DividedInput divide(String input) {
        String[] split = sideSplitInput(input);
        return new DividedInput(split[0], split[2], split[1]);
    }

    private String[] sideSplitInput(String input) {
        String[] coupledDividers = new String[] {"<=", ">=", "==", "!="};
        String[] singleDividers = new String[] {"<", ">", "="};
        String[] sideSplit;

        try {
            sideSplit = splitBy(input, singleDividers);
        } catch (RuntimeException e) {
            sideSplit = splitBy(input, coupledDividers);
        }

        return sideSplit;
    }

    private String[] splitBy(String input, String[] delimiters) {
        String[] result = null;
        boolean foundDivider = false;
        String actualDelimiter = "";

        for (String delimiter : delimiters) {
            if (input.contains(delimiter)) {
                if (foundDivider) {
                    throw new RuntimeException(MessageConstants.INVALID_INPUT_MESSAGE);
                }

                actualDelimiter = delimiter;
                result = input.split(" " + delimiter + " ");
                foundDivider = true;
            }
        }

        if (!foundDivider || result.length != 2) {
            throw new RuntimeException(MessageConstants.INVALID_INPUT_MESSAGE);
        }

        return new String[] {result[0], actualDelimiter, result[1]};
    }
}
