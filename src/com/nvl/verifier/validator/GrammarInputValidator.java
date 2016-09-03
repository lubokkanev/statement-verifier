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

        if (isExtendedBoolean()) {
            if (parseBoolExpression()) {
                parseBoolComparison();
            }
        } else {
            if (parseNotBool()) {
                parseComparison();
            }
        }

        return splitString.isEmpty();
    }

    private boolean parseBoolComparison() {
        if (!splitString.isEmpty() && splitString.getCurrentElement().equals("==")) {
            splitString.nextPosition();
            return parseBoolExpression();
        }

        return false;
    }

    private boolean parseSimpleDefinition() {
        if (!variableManager.containsVariable(splitString.getNthElement(0))) {
            return matchesType(splitString.getNthElement(2));
        } else {
            return matchesType(splitString.getNthElement(2));
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

    private boolean isExtendedBoolean() {
        skipBrackets();

        boolean result = splitString.getCurrentElement().equals("!") || isBoolean(splitString.getCurrentElement());
        splitString.setPosition(0);

        return result;
    }

    private void skipBrackets() {
        while (splitString.getCurrentElement().equals("(")) {
            splitString.nextPosition();
        }
    }

    private boolean isBoolean(String currentElement) {
        boolean isVariable = variableManager.containsVariable(currentElement);

        return currentElement.equalsIgnoreCase("FALSE") || currentElement.equalsIgnoreCase("TRUE") ||
                (isVariable && variableManager.getVariable(currentElement).getType() == VariableType.BOOLEAN);
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
        skipBrackets();
        String afterBraces = splitString.getCurrentElement();
        splitString.setPosition(startingPosition);

        if (isNumber(afterBraces)) {
            return parseIntExpression();
        } else if (isString(afterBraces)) {
            return parseStringExpression();
        } else if (isArray(afterBraces)) {
            return parseArrayExpression();
        }

        return false;
    }

    private boolean parseArrayExpression() {
        if (splitString.isEmpty()) {
            return false;
        }

        String currentElement = splitString.getCurrentElement();

        if (currentElement.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);

            if (parseArrayExpression() && splitString.getCurrentElement().equals(")")) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseArrayOperation();
            }
        } else if (isArray(currentElement)) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseArrayOperation();
        }

        return false;
    }

    private boolean parseArrayOperation() {
        if (splitString.isEmpty()) {
            return true;
        }

        String currentElement = splitString.getCurrentElement();

        if (currentElement.equals("+") || currentElement.equals("*")) {
            splitString.nextPosition();
            return parseArrayOrNumber();
        }

        return true;
    }

    private boolean parseArrayOrNumber() {
        if (splitString.isEmpty()) {
            return false;
        }

        int startingPosition = splitString.getPosition();
        skipBrackets();

        if (isArray(splitString.getCurrentElement())) {
            splitString.setPosition(startingPosition);
            return parseArrayExpression();
        } else if (isNumber(splitString.getCurrentElement())) {
            splitString.setPosition(startingPosition);
            return parseIntExpression();
        }

        return false;
    }

    private boolean isString(String currentElement) {
        boolean isVariable = variableManager.containsVariable(currentElement);

        return currentElement.matches("'\\w+'") || (isVariable && variableManager.getVariable(currentElement).getType() == VariableType.STRING);
    }

    private boolean isArray(String currentElement) {
        return isArrayValue(currentElement) || isArrayVariable(currentElement);
    }

    private boolean isArrayVariable(String currentElement) {
        boolean isVariable = variableManager.containsVariable(currentElement);
        VariableType type = null;

        if (isVariable) {
            type = variableManager.getVariable(currentElement).getType();
        }

        return isVariable && type == VariableType.ARRAY;
    }

    private boolean isArrayValue(String currentElement) {
        return currentElement.matches("\\{\\d+(,\\d+)*\\}");
    }

    private boolean matchesType(String currentElement) {
        return isString(currentElement) || isNumber(currentElement) || isBoolean(currentElement) || isArray(currentElement);
    }

    private boolean parseComparison() {
        if (!splitString.isEmpty() && isComparisonSymbol(splitString.getCurrentElement())) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseNotBool();
        }

        return false;
    }

    private boolean isComparisonSymbol(String currentElement) {
        return currentElement.equals("==") || currentElement.equals(">") || currentElement.equals("<") || currentElement.equals("<=") || currentElement.equals(">=") ||
                currentElement.equals("!=");
    }

    private boolean parseIntExpression() {
        if (splitString.isEmpty()) {
            return false;
        }

        String currentElement = splitString.getNthElement(splitString.getPosition());
        if (currentElement.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            if (parseIntExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) {
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

        String currentElement = splitString.getNthElement(splitString.getPosition());
        if (currentElement.equals("(")) {
            splitString.setPosition(splitString.getPosition() + 1);
            if (parseStringExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseStringOperation();
            }
        } else {
            if (isString(currentElement)) {
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

        String currentElement = splitString.getNthElement(splitString.getPosition());
        if (currentElement.equals("+")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseStringExpression();
        } else if (currentElement.equals("*")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseIntExpression();
        }

        return true;
    }

    private boolean parseBoolExpression() {
        if (splitString.isEmpty()) {
            return false;
        }

        String currentElement = splitString.getCurrentElement();
        if (currentElement.equals("!")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolExpression();
        } else if (isBoolean(currentElement)) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolOperation();
        } else if (currentElement.equals("(")) {
            splitString.nextPosition();
            if (parseBoolExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) {
                splitString.nextPosition();
                return parseBoolOperation();
            }
        }

        return false;
    }

    private boolean parseBoolOperation() {
        if (splitString.isEmpty()) {
            return true;
        }

        String currentElement = splitString.getNthElement(splitString.getPosition());
        if (currentElement.equals("&&") || currentElement.equals("||")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolExpression();
        }

        return true;
    }
}
