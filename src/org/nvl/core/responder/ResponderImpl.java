package org.nvl.core.responder;

import org.nvl.core.input.substituter.VariableSubstituter;
import org.nvl.core.input.type.InputType;
import org.nvl.core.input.type.InputTypeDeterminer;
import org.nvl.core.input.validator.InputValidator;
import org.nvl.core.input.white_space.InputSpaceFixer;
import org.nvl.core.responder.processor.RequestProcessor;
import org.nvl.core.rpn.verifier.ArrayRpnVerifier;
import org.nvl.core.rpn.verifier.BooleanRpnVerifier;
import org.nvl.core.rpn.verifier.NumberRpnVerifier;
import org.nvl.core.rpn.verifier.StringRpnVerifier;
import org.nvl.core.statement.RpnStatementVerifier;
import org.nvl.core.statement.type.InputTypeMatcher;
import org.nvl.core.variable.EvaluatedVariable;
import org.nvl.core.variable.VariableType;
import org.nvl.core.variable.manager.VariableManager;

import java.util.Set;

import static org.nvl.MessageConstants.*;

public class ResponderImpl implements Responder {
    private InputTypeDeterminer typeDeterminer;
    private RequestProcessor requestProcessor;
    private InputValidator inputValidator;
    private InputSpaceFixer inputSpaceFixer;
    private VariableSubstituter variableSubstituter;
    private VariableManager variableManager;
    private Divider divider;

    public ResponderImpl(InputTypeDeterminer typeDeterminer, RequestProcessor requestProcessor, InputValidator inputValidator, VariableManager variableManager) {
        this.typeDeterminer = typeDeterminer;
        this.requestProcessor = requestProcessor;
        this.inputValidator = inputValidator;
        this.variableManager = variableManager;
        this.inputSpaceFixer = new InputSpaceFixer();
        this.variableSubstituter = new VariableSubstituter(variableManager);
        this.divider = new Divider();
    }

    @Override
    public String process(String userInput) {
        String response = "";

        String spaceFixedInput = inputSpaceFixer.fix(userInput);
        DividedInput dividedInput = divider.divide(spaceFixedInput);
        DividedInput substitutedInput = variableSubstituter.substitute(dividedInput);
        validateInput(substitutedInput);

        InputType inputType = typeDeterminer.determineType(substitutedInput.toString());

        if (inputType == InputType.NEW_VARIABLE) {
            computeRightSide(substitutedInput);
            requestProcessor.addVariable(substitutedInput.toString());
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            computeRightSide(substitutedInput);
            requestProcessor.updateVariable(substitutedInput.toString());
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = requestProcessor.verifyStatement(substitutedInput.toString());
            response = String.format(STATEMENT_FORMAT, userInput, Boolean.toString(validStatement).toUpperCase());
        }

        return response;
    }

    private void computeRightSide(DividedInput dividedInput) {
        if (sameTypes(VariableType.ARRAY, dividedInput.getRightSide())) {
            ArrayRpnVerifier rpnVerifier = new ArrayRpnVerifier();
            String rpn = rpnVerifier.createRPN(dividedInput.getRightSide());
            dividedInput.setRightSide(rpnVerifier.calculateRpn(rpn));
        } else if (sameTypes(VariableType.STRING, dividedInput.getRightSide())) {
            StringRpnVerifier rpnVerifier = new StringRpnVerifier();
            String rpn = rpnVerifier.createRPN(dividedInput.getRightSide());
            dividedInput.setRightSide(rpnVerifier.calculateRpnForString(rpn));
        } else if (sameTypes(VariableType.NUMBER, dividedInput.getRightSide())) {
            NumberRpnVerifier rpnVerifier = new NumberRpnVerifier();
            String rpn = rpnVerifier.createRPN(dividedInput.getRightSide());
            dividedInput.setRightSide(String.valueOf(rpnVerifier.calculateRPN(rpn).intValue()));
        } else if (sameTypes(VariableType.BOOLEAN, dividedInput.getRightSide())) {
            BooleanRpnVerifier rpnVerifier = new BooleanRpnVerifier();
            String rpn = rpnVerifier.createRPN(dividedInput.getRightSide());
            dividedInput.setRightSide(rpnVerifier.calculateRPN(rpn).toString());
        }
    }

    private void validateInput(DividedInput input) {
        boolean validRightSide = inputValidator.isValid(input.getRightSide());
        boolean validLeftSide = true;

        if (!input.getOperation().equals("=")) {
            validLeftSide = inputValidator.isValid(input.getLeftSide());

            boolean validTypes = new InputTypeMatcher(variableManager).sidesTypeMatches(input.getLeftSide(), input.getRightSide());

            if (!validTypes) {
                throw new RuntimeException(INVALID_INPUT_MESSAGE);
            }
        } else {
            boolean isExisting = variableManager.containsVariable(input.getLeftSide());
            EvaluatedVariable variable = variableManager.getVariable(input.getLeftSide());
            if (isExisting && !sameTypes(variable.getType(), input.getRightSide())) {
                throw new RuntimeException(INVALID_INPUT_MESSAGE);
            } else {
                if (!input.getLeftSide().matches("\\w+")) {
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
            }
        }

        if (!validLeftSide || !validRightSide) {
            throw new RuntimeException(INVALID_INPUT_MESSAGE);
        }
    }

    private boolean sameTypes(VariableType type, String rightSide) {
        RpnStatementVerifier rpnStatementVerifier = new RpnStatementVerifier(variableManager);
        rpnStatementVerifier.checkType(rightSide);

        boolean bothAreArrays = type == VariableType.ARRAY && rpnStatementVerifier.isArrayOperation();
        boolean bothAreStrings = type == VariableType.STRING && rpnStatementVerifier.isStringOperation();
        boolean bothAreBooleans = type == VariableType.BOOLEAN && rpnStatementVerifier.isBooleanOperation();
        boolean bothAreNumbers =
                type == VariableType.NUMBER && !rpnStatementVerifier.isBooleanOperation() && !rpnStatementVerifier.isStringOperation() && !rpnStatementVerifier.isArrayOperation();
        return bothAreArrays || bothAreStrings || bothAreBooleans || bothAreNumbers;
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return requestProcessor.variables();
    }
}
