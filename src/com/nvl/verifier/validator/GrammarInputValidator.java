package com.nvl.verifier.validator;

import com.nvl.variable.VariableType;
import com.nvl.variable.manager.VariableManager;

public class GrammarInputValidator implements InputValidator {
    private VariableManager variableManager;

    private SplitString splitString; // new

    public GrammarInputValidator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    // everything is separated by space; '&&', '||' and '==' are not
    public boolean isValid(String input) {
        if (!matchingTypes(input)) {
            return false;
        }

        splitString = new SplitString(input);

        if (splitString.getSplitInput().length < 3) {
            return false;
        }

        if (splitString.getSplitInput().length == 3 && splitString.getNthElement(1).equals("=")) {
            if (!variableManager.containsVariable(splitString.getNthElement(0))) {
                return isValidRightSide(splitString.getNthElement(2));
            } else {
                VariableType type = variableManager.getVariable(splitString.getNthElement(0)).getType();
                return compareType(type, splitString.getNthElement(2));
            }
        }

        while (!isEmpty() && splitString.getNthElement(splitString.getPosition()).equals("(")) // skipping brackets
        {
            splitString.setPosition(splitString.getPosition() + 1);
        }

        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        splitString.setPosition(0); // going back to the beginning

        boolean isVariable = variableManager.containsVariable(current);

        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(current).getType();
        }

        if (isNumber(current, isVariable, type) || isString(current, isVariable, type) || isArray(current, isVariable, type)) {
            if (parseNotBool(current)) {
                parseComparison();
            }
        } else if (isBoolean(current, isVariable, type)) {
            if (parseBoolExpression()) {
                parseBoolOperation();
            }
        }

        return isEmpty();
    }

    private boolean matchingTypes(String input) {
        String[] split = input.split("[=<>]");

        if (input.contains("==")) {
            split = input.split("==");
        }

        if (split.length != 2) {
            return true;
        }

        if (split[0].contains("{") && !split[1].contains("{") || split[0].contains("'") && !split[1].contains("'")) {
            return false;
        }

        return true;
    }

    private boolean isBoolean(String current, boolean isVariable, VariableType type) {
        return current.equalsIgnoreCase("FALSE") || current.equalsIgnoreCase("TRUE") || current.equals("!") || (isVariable && type == VariableType.BOOLEAN);
    }

    private boolean isNumber(String current, boolean isVariable, VariableType type) {
        return current.matches("\\d+") || (isVariable && type == VariableType.NUMBER);
    }

    private boolean parseNotBool(String current) {
        int startingPosition = splitString.getPosition();

        while (current.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            current = splitString.getNthElement(splitString.getPosition());
        }

        splitString.setPosition(startingPosition);

        boolean isVariable = variableManager.containsVariable(current);
        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(current).getType();
        }

        if (isNumber(current, isVariable, type)) {
            return parseIntExpression();
        } else if (isString(current, isVariable, type)) {
            return parseStringExpression();
        } else if (isArray(current, isVariable, type)) {
            return parseArrayExpression();
        }

        return false;
    }

    private boolean parseArrayExpression() {
        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());

        if (current.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);

            if (parseArrayExpression() && current.equals(")")) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseArrayOperation();
            }
        }

        return false;
    }

    private boolean parseArrayOperation() {
        if (isEmpty()) {
            return true;
        }

        String current = splitString.getNthElement(splitString.getPosition());

        if (current.equals("+") || current.equals("*")) {
            return parseArrayOrNumber();
        }

        return true;
    }

    private boolean parseArrayOrNumber() {
        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());

        boolean isVariable = variableManager.containsVariable(current);

        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(current).getType();
        }

        if (isArray(current, isVariable, type)) {
            return parseArrayExpression();
        } else if (isNumber(current, isVariable, type)) {
            return parseIntExpression();
        }

        return false;
    }

    private boolean isString(String current, boolean isVariable, VariableType type) {
        return current.matches("'\\w+'") || (isVariable && type == VariableType.STRING);
    }

    private boolean isArray(String current, boolean isVariable, VariableType currentType) {
        return current.matches("\\{\\d+(,\\d+)*\\}") || isVariable && currentType == VariableType.ARRAY;
    }

    private boolean isValidRightSide(String stringToCheck) {
        return stringToCheck.matches("'\\w+'") || stringToCheck.matches("\\d+") || stringToCheck.equalsIgnoreCase("FALSE") || stringToCheck.equalsIgnoreCase("TRUE") ||
                stringToCheck.matches("\\{\\d+(,\\d+)*\\}");
    }

    private boolean compareType(VariableType type, String string) {
        if (type == VariableType.STRING && string.matches("'\\w+'")) {
            return true;
        } else if (type == VariableType.BOOLEAN && (string.equals("FALSE") || string.equals("TRUE"))) {
            return true;
        } else if (type == VariableType.NUMBER && string.matches("\\d+")) {
            return true;
        } else if (type == VariableType.ARRAY && string.matches("\\{\\d+(,\\d+)*\\}")) {
            return true;
        }

        return false;
    }

    private boolean isEmpty() {
        return splitString.getPosition() == splitString.getSplitInput().length;
    }

    private boolean parseComparison() // debuuug
    {
        if (isEmpty()) {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());

        if (!isEmpty() && isComparisonSymbol(current)) {
            splitString.setPosition(splitString.getPosition() + 1);
            current = splitString.getNthElement(splitString.getPosition());

            boolean isVariable = variableManager.containsVariable(current);
            VariableType type = null;
            if (isVariable) {
                type = variableManager.getVariable(current).getType();
            }

            return parseNotBool(current);
        }

        return false;
    }

    private boolean isComparisonSymbol(String current) {
        return current.equals("==") || current.equals(">") || current.equals("<") || current.equals("<=") || current.equals(">=") || current.equals("!=");
    }

    private boolean parseIntExpression() // deeebuuuug
    {
        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        if (current.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            if (parseIntExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) // !!
            {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseIntOperation();
            }
        } else {
            boolean isVariable = variableManager.containsVariable(current);
            VariableType type = null;

            if (isVariable) {
                type = variableManager.getVariable(current).getType();
            }

            if (isNumber(current, isVariable, type)) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseIntOperation();
            }
        }

        return false;
    }

    private boolean parseIntOperation() {
        if (isEmpty()) {
            return true;
        }

        String current = splitString.getNthElement(splitString.getPosition());

        if (current.equals("*")) {
            splitString.setPosition(splitString.getPosition() + 1);
            current = splitString.getNthElement(splitString.getPosition());

            return parseNotBool(current);
        } else if (current.equals("+")) {
            splitString.setPosition(splitString.getPosition() + 1);
            current = splitString.getNthElement(splitString.getPosition());

            return parseArrayOrNumber();
        }

        return true;
    }

    private boolean parseStringExpression() {
        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        if (current.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            if (parseStringExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) // !!
            {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseStringOperation();
            }
        } else {
            boolean isVariable = variableManager.containsVariable(current);
            VariableType type = null;

            if (isVariable) {
                type = variableManager.getVariable(current).getType();
            }

            if (isString(current, isVariable, type)) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseStringOperation();
            }
        }

        return false;
    }

    private boolean parseStringOperation() {
        if (isEmpty()) {
            return true;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        if (current.equals("+")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseStringExpression();
        } else if (current.equals("*")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseIntExpression();
        }

        return true;
    }

    private boolean parseBoolExpression() {
        if (isEmpty()) {
            return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        if (current.equals("!")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolExpression();
        } else if (current.equalsIgnoreCase("TRUE") || current.equalsIgnoreCase("FALSE") ||
                (variableManager.containsVariable(current) && variableManager.getVariable(current).getType() == VariableType.BOOLEAN)) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolOperation();
        } else if (current.equals("(")) {
            if (parseBoolExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) {
                return parseBoolOperation();
            }
        }

        return false;
    }

    private boolean parseBoolOperation() {
        if (isEmpty()) {
            return true;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        if (current.equals("&&") || current.equals("||")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolExpression();
        }

        return true;
    }
}
