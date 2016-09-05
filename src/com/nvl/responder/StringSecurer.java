package com.nvl.responder;

public class StringSecurer {
    private final static String UNSAFE_DELIMITER = " ";
    private final static String SAFE_DELIMITER = "_";
    private final static String STRING_SURROUNDER = "'";
    private StringBuilder input;

    public StringSecurer(String input) {
        this.input = new StringBuilder(input);

        start = input.indexOf(STRING_SURROUNDER);
        end = start + 1 + input.substring(start + 1).indexOf(STRING_SURROUNDER);
    }

    private int start;
    private int end;

    String proofString() {
        while (start != -1) {
            String substring = input.substring(start, end);
            String newSubstring = substring.replaceAll(UNSAFE_DELIMITER, SAFE_DELIMITER);
            input.replace(start, end, newSubstring);
            computeEndPoints();
        }

        return input.toString();
    }

    private void computeEndPoints() {
        int index = input.substring(end + 1).indexOf(STRING_SURROUNDER);

        if (index == -1) {
            start = -1;
        } else {
            start = end + 1 + index;
            end = start + 1 + input.substring(start + 1).indexOf(STRING_SURROUNDER);
        }
    }
}