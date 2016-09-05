package com.nvl.responder;

import com.nvl.constants.Constants;
import com.nvl.variable.EvaluatedVariable;
import com.nvl.verifier.determiner.InputType;
import com.nvl.verifier.determiner.InputTypeDeterminer;
import com.nvl.verifier.processor.RequestProcessor;
import com.nvl.verifier.validator.InputValidator;

import java.util.Set;

public class ResponderImpl implements Responder {
    private InputTypeDeterminer typeDeterminer;
    private RequestProcessor requestProcessor;
    private InputValidator inputValidator;
    private InputSpaceFixer inputSpaceFixer;

    public ResponderImpl(InputTypeDeterminer typeDeterminer, RequestProcessor requestProcessor, InputValidator inputValidator) {
        this.typeDeterminer = typeDeterminer;
        this.requestProcessor = requestProcessor;
        this.inputValidator = inputValidator;
        this.inputSpaceFixer = new InputSpaceFixer();
    }

    @Override
    public String process(String userInput) {
        String response = "";

        try {
            String spaceFixedInput = inputSpaceFixer.fix(userInput);
            validateInput(spaceFixedInput);

            InputType inputType = typeDeterminer.determineType(spaceFixedInput);

            if (inputType == InputType.NEW_VARIABLE) {
                requestProcessor.addVariable(spaceFixedInput);
                response = Constants.NEW_VARIABLE_MESSAGE;
            } else if (inputType == InputType.EXISTING_VARIABLE) {
                requestProcessor.updateVariable(spaceFixedInput);
                response = Constants.EXISTING_VARIABLE_MESSAGE;
            } else if (inputType == InputType.STATEMENT) {
                boolean validStatement = requestProcessor.verifyStatement(spaceFixedInput);
                response = String.format(Constants.STATEMENT_FORMAT, userInput, Boolean.toString(validStatement).toUpperCase());
            }
        } catch (Exception e) {
            throw new RuntimeException(Constants.INVALID_INPUT_MESSAGE + ":( ");
        }

        return response;
    }

    private void validateInput(String userInput) {
        if (!inputValidator.isValid(userInput)) {
            throw new RuntimeException(Constants.INVALID_INPUT_MESSAGE);
        }
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return requestProcessor.variables();
    }
}

