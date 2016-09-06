package org.nvl.core.input.split;

import java.util.ArrayList;

public class SplitString {
    private String[] splitInput;
    private int position;

    private final char DELIMITER = ' ';
    private final char STRING_SURROUNDER = '\'';

    public SplitString(String input) {
        splitInput = split(input);
        position = 0;
    }

    private String[] split(String input) {
        ArrayList<String> result = new ArrayList<>();

        int start = 0;
        int end = 0;

        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) == STRING_SURROUNDER) {
                ++i;

                while (input.charAt(i) != STRING_SURROUNDER) {
                    ++i;
                }
            }

            if (input.charAt(i) == DELIMITER) {
                if (end >= start) {
                    result.add(input.substring(start, end + 1));
                }

                start = i + 1;
            } else {
                end = i;
            }
        }

        end = input.length() - 1;
        if (end >= start) {
            result.add(input.substring(start, end + 1));
        }

        return result.toArray(new String[result.size()]);
    }

    public int getPosition() {
        return position;
    }

    public String getNthElement(int n) {
        if (n >= splitInput.length) {
            throw new RuntimeException("It's empty! ");
        }

        return splitInput[n];
    }

    public String[] getSplitInput() {
        return splitInput;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCurrentElement() {
        return getNthElement(position);
    }

    public void setNthElement(int n, String value) {
        if (n >= splitInput.length) {
            throw new RuntimeException("It's empty! ");
        }

        splitInput[n] = value;
    }

    public void setCurrentElement(String value) {
        setNthElement(position, value);
    }

    public boolean isEmpty() {
        return position == splitInput.length;
    }

    public void nextPosition() {
        ++position;
    }
}
