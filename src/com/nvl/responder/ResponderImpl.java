package com.nvl.responder;

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

    private static final String NEW_VARIABLE_MESSAGE = "Variable added successfully. ";
    private static final String EXISTING_VARIABLE_MESSAGE = "Variable updated successfully. ";
    private static final String STATEMENT_FORMAT = "The statement \"%s\" is %s.";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. ";

    public ResponderImpl(InputTypeDeterminer typeDeterminer, RequestProcessor requestProcessor, InputValidator inputValidator) {
        this.typeDeterminer = typeDeterminer;
        this.requestProcessor = requestProcessor;
        this.inputValidator = inputValidator;
    }

    @Override
    public String process(String userInput) {
        InputType inputType = typeDeterminer.determineType(userInput);
        String response = "";

        validateInput(userInput);

        if (inputType == InputType.NEW_VARIABLE) {
            requestProcessor.addVariable(userInput);
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            requestProcessor.updateVariable(userInput);
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = requestProcessor.verifyStatement(userInput);
            response = String.format(STATEMENT_FORMAT, userInput, Boolean.toString(validStatement));
        }

        return response;
    }

    private void validateInput(String userInput) {
        if (!inputValidator.isValid(userInput)) {
            throw new RuntimeException(INVALID_INPUT_MESSAGE);
        }
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return requestProcessor.variables();
    }
}
