package org.nvl.core.responder;

public class DividedInput {
    private String leftSide;
    private String rightSide;
    private String operation;

    public DividedInput(String leftSide, String rightSide, String operation) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operation = operation;
    }

    public DividedInput() {
        leftSide = rightSide = operation = null;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    public void setRightSide(String rightSide) {
        this.rightSide = rightSide;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return leftSide + " " + operation + " " + rightSide;
    }
}
