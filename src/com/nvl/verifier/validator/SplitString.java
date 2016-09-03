package com.nvl.verifier.validator;

// new

public class SplitString {
    private String[] splitInput;
    private int position;

    public SplitString(String input) {
        splitInput = input.split(" ");
        position = 0;
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

    public boolean isEmpty() {
        return position == splitInput.length;
    }

    public void nextPosition() {
        ++position;
    }
}
