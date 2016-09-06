package org.nvl.core.responder;

import org.nvl.core.input.type.InputType;
import org.nvl.core.input.type.InputTypeDeterminer;
import org.nvl.core.input.validator.InputValidator;
import org.nvl.core.input.white_space.InputSpaceFixer;
import org.nvl.core.responder.processor.RequestProcessor;
import org.nvl.core.variable.EvaluatedVariable;

import java.util.Set;

import static org.nvl.MessageConstants.*;

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

        String spaceFixedInput = inputSpaceFixer.fix(userInput);
        validateInput(spaceFixedInput);

        InputType inputType = typeDeterminer.determineType(spaceFixedInput);

        if (inputType == InputType.NEW_VARIABLE) {
            requestProcessor.addVariable(spaceFixedInput);
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            requestProcessor.updateVariable(spaceFixedInput);
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = requestProcessor.verifyStatement(spaceFixedInput);
            response = String.format(STATEMENT_FORMAT, userInput, Boolean.toString(validStatement).toUpperCase());
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

