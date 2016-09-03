package com.nvl.verifier.validator;

import com.nvl.variable.VariableType;
import com.nvl.variable.manager.VariableManager;

public class GrammarInputValidator implements InputValidator {
    private VariableManager variableManager;

    private SplitString splitString;

    public GrammarInputValidator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    // everything is separated by space; '&&', '||' and '==' are not
    public boolean isValid(String input) {
        splitString = new SplitString(input);

        if (!sidesMatch(input) || splitString.getSplitInput().length < 3) {
            return false;
        }

        if (isSimpleDefinition()) {
            return parseSimpleDefinition();
        }

        while (!splitString.isEmpty() && splitString.getNthElement(splitString.getPosition()).equals("(")) // skipping brackets
        {
            splitString.nextPosition();
        }

        if (splitString.isEmpty()) {
            return false;
        }

        splitString.setPosition(0); // going back to the beginning

        boolean isVariable = variableManager.containsVariable(splitString.getCurrentElement());

        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(splitString.getCurrentElement()).getType();
        }

        if (isExtendedBoolean(splitString.getCurrentElement(), isVariable, type)) {
            if (parseBoolExpression()) {
                parseBoolOperation();
            }
        } else {
            if (parseNotBool()) {
                parseComparison();
            }
        }

        return splitString.isEmpty();
    }

    private boolean parseSimpleDefinition() {
        if (!variableManager.containsVariable(splitString.getNthElement(0))) {
            return isValidRightSide(splitString.getNthElement(2));
        } else {
            VariableType type = variableManager.getVariable(splitString.getNthElement(0)).getType();
            return matchesType(type, splitString.getNthElement(2));
        }
    }

    private boolean isSimpleDefinition() {
        return splitString.getSplitInput().length == 3 && splitString.getNthElement(1).equals("=");
    }

    private boolean sidesMatch(String input) {
        String[] split = input.split("[=<>]");

        if (input.contains("==")) {
            split = input.split("==");
        }

        return split.length != 2 || !(split[0].contains("{") && !split[1].contains("{") || split[0].contains("'") && !split[1].contains("'"));
    }

    private boolean isExtendedBoolean(String current, boolean isVariable, VariableType type) {
        return current.equals("!") || isBoolean(current, isVariable, type);
    }

    private boolean isBoolean(String current, boolean isVariable, VariableType type) {
        return current.equalsIgnoreCase("FALSE") || current.equalsIgnoreCase("TRUE") || (isVariable && type == VariableType.BOOLEAN);
    }

    private boolean isNumber(String element) {
        return isNumberValue(element) || isNumberVariable(element);
    }

    private boolean isNumberValue(String element) {
        return element.matches("\\d+");
    }

    private boolean isNumberVariable(String element) {
        if (variableManager.containsVariable(element)) {
            VariableType type = variableManager.getVariable(element).getType();
            return type == VariableType.NUMBER;
        }

        return false;
    }

    private boolean parseNotBool() {
        int startingPosition = splitString.getPosition();

        while (splitString.getCurrentElement().equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
        }

        String afterBraces = splitString.getCurrentElement();
        splitString.setPosition(startingPosition);

        boolean isVariable = variableManager.containsVariable(afterBraces);
        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(afterBraces).getType();
        }

        if (isNumber(afterBraces)) {
            return parseIntExpression();
        } else if (isString(afterBraces, isVariable, type)) {
            return parseStringExpression();
        } else if (isArray(afterBraces, isVariable, type)) {
            return parseArrayExpression();
        }

        return false;
    }

    private boolean parseArrayExpression() {
        if (splitString.isEmpty()) {
            return false;
        }

        String current = splitString.getCurrentElement();

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
        if (splitString.isEmpty()) {
            return true;
        }

        String current = splitString.getCurrentElement();

        return !(current.equals("+") || current.equals("*")) || parseArrayOrNumber();
    }

    private boolean parseArrayOrNumber() {
        if (splitString.isEmpty()) {
            return false;
        }

        boolean isVariable = variableManager.containsVariable(splitString.getCurrentElement());
        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(splitString.getCurrentElement()).getType();
        }

        if (isArray(splitString.getCurrentElement(), isVariable, type)) {
            return parseArrayExpression();
        } else if (isNumber(splitString.getCurrentElement())) {
            return parseIntExpression();
        }

        return false;
    }

    private boolean isString(String current, boolean isVariable, VariableType type) {
        return current.matches("'\\w+'") || (isVariable && type == VariableType.STRING);
    }

    private boolean isArray(String current, boolean isVariable, VariableType type) {
        return current.matches("\\{\\d+(,\\d+)*\\}") || isVariable && type == VariableType.ARRAY;
    }

    private boolean isValidRightSide(String stringToCheck) {
        return stringToCheck.matches("'\\w+'") || stringToCheck.matches("\\d+") || stringToCheck.equalsIgnoreCase("FALSE") || stringToCheck.equalsIgnoreCase("TRUE") ||
                stringToCheck.matches("\\{\\d+(,\\d+)*\\}");
        // TODO: Lubo - refactor
    }

    private boolean matchesType(VariableType currentType, String currentElement) {
        return isString(currentElement, true, currentType) || isBoolean(currentElement, true, currentType) || isNumber(currentElement) || isArray(currentElement, true, currentType);
    }

    private boolean parseComparison() {
        if (splitString.isEmpty()) {
            return false;
        }

        if (!splitString.isEmpty() && isComparisonSymbol(splitString.getCurrentElement())) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseNotBool();
        }

        return false;
    }

    private boolean isComparisonSymbol(String current) {
        return current.equals("==") || current.equals(">") || current.equals("<") || current.equals("<=") || current.equals(">=") || current.equals("!=");
    }

    private boolean parseIntExpression() {
        if (splitString.isEmpty()) {
            return false;
        }

        String currentElement = splitString.getNthElement(splitString.getPosition());
        if (currentElement.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            if (parseIntExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) // !!
            {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseIntOperation();
            }
        } else {
            if (isNumber(currentElement)) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseIntOperation();
            }
        }

        return false;
    }

    private boolean parseIntOperation() {
        if (splitString.isEmpty()) {
            return true;
        }

        if (splitString.getCurrentElement().equals("*")) {
            splitString.setPosition(splitString.getPosition() + 1);

            return parseNotBool();
        } else if (splitString.getCurrentElement().equals("+")) {
            splitString.setPosition(splitString.getPosition() + 1);

            return parseArrayOrNumber();
        }

        return true;
    }

    private boolean parseStringExpression() {
        if (splitString.isEmpty()) {
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
        if (splitString.isEmpty()) {
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
        if (splitString.isEmpty()) {
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
        if (splitString.isEmpty()) {
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
