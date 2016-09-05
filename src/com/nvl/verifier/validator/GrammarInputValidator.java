package com.nvl.verifier.validator;

import com.nvl.variable.VariableType;
import com.nvl.variable.manager.VariableManager;

/**
 * Validates mathematical statements using a context-free grammar. It requires all operators and constants to be separated by spaces.
 */
public class GrammarInputValidator implements InputValidator {
    private VariableManager variableManager;

    private InputTypeMatcher inputTypeMatcher;
    private SplitString splitString;

    public GrammarInputValidator(VariableManager variableManager) {
        this.variableManager = variableManager;
        inputTypeMatcher = new InputTypeMatcher(variableManager);
    }

    @Override
    public boolean isValid(String input) {
        try {
            splitString = new SplitString(input);

            if (!inputTypeMatcher.sidesTypeMatches(input) || splitString.getSplitInput().length < 3) {
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
        } catch (RuntimeException e) {
            return false;
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
        if (variableManager.containsVariable(splitString.getNthElement(0))) {
            return variableMatchesRightSideType();
        } else {
            return isValidVariableName(splitString.getNthElement(0)) && matchesTypes(splitString.getNthElement(2));
        }
    }

    private boolean variableMatchesRightSideType() {
        String variable = splitString.getNthElement(0);
        return variableManager.getVariable(variable).getType() == getType(splitString.getNthElement(2));
    }

    private VariableType getType(String input) {
        if (isNumber(input)) {
            return VariableType.NUMBER;
        } else if (isArray(input)) {
            return VariableType.ARRAY;
        } else if (isBoolean(input)) {
            return VariableType.BOOLEAN;
        } else if (isString(input)) {
            return VariableType.STRING;
        } else {
            return null;
        }
    }

    private boolean isValidVariableName(String variableName) {
        return variableName.matches("[a-zA-Z]+");
    }

    private boolean isSimpleDefinition() {
        return splitString.getSplitInput().length == 3 && splitString.getNthElement(1).equals("=");
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
        return isBooleanValue(currentElement) || isVariableOfType(currentElement, VariableType.BOOLEAN);
    }

    private boolean isBooleanValue(String currentElement) {
        return currentElement.equalsIgnoreCase("FALSE") || currentElement.equalsIgnoreCase("TRUE");
    }

    private boolean isNumber(String element) {
        return isNumberValue(element) || isVariableOfType(element, VariableType.NUMBER);
    }

    private boolean isNumberValue(String element) {
        return element.matches("\\d+");
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
        if (!splitString.isEmpty()) {
            String currentElement = splitString.getCurrentElement();

            if (currentElement.equals("+") || currentElement.equals("*")) {
                splitString.nextPosition();
                return parseArrayOrNumber();
            }
        }

        return true;
    }

    private boolean parseArrayOrNumber() {
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
        return isArrayValue(currentElement) || isVariableOfType(currentElement, VariableType.ARRAY);
    }

    private boolean isVariableOfType(String currentElement, VariableType neededType) {
        boolean isVariable = variableManager.containsVariable(currentElement);
        return isVariable && variableManager.getVariable(currentElement).getType() == neededType;
    }

    private boolean isArrayValue(String currentElement) {
        return currentElement.matches("\\{\\d+(,\\d+)*\\}");
    }

    private boolean matchesTypes(String currentElement) {
        return isString(currentElement) || isNumber(currentElement) || isBoolean(currentElement) || isArray(currentElement);
    }

    private boolean parseComparison() {
        if (isComparisonSymbol(splitString.getCurrentElement())) {
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
            return parseArrayOrNumber();
        } else if (splitString.getCurrentElement().equals("+")) {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseArrayOrNumber();
        }

        return true;
    }

    private boolean parseStringExpression() {
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
        }

        return true;
    }

    private boolean parseBoolExpression() {
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
        if (!splitString.isEmpty()) {
            String currentElement = splitString.getCurrentElement();

            if (currentElement.equals("&&") || currentElement.equals("||")) {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseBoolExpression();
            }
        }

        return true;
    }
}
