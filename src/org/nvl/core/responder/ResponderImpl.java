package org.nvl.core.responder;

import org.nvl.core.input.substituter.VariableSubstituter;
import org.nvl.core.input.type.InputType;
import org.nvl.core.input.type.InputTypeDeterminer;
import org.nvl.core.input.validator.InputValidator;
import org.nvl.core.input.white_space.InputSpaceFixer;
import org.nvl.core.responder.processor.RequestProcessor;
import org.nvl.core.variable.EvaluatedVariable;
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
        DividedInput substitutedVariablesInput = variableSubstituter.substitute(dividedInput);
        String concatenatedInput = substitutedVariablesInput.getLeftSide() + " " + substitutedVariablesInput.getOperation() + " " + substitutedVariablesInput.getRightSide();
        validateInput(concatenatedInput);

        InputType inputType = typeDeterminer.determineType(spaceFixedInput);

        if (inputType == InputType.NEW_VARIABLE) {
            requestProcessor.addVariable(concatenatedInput);
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            requestProcessor.updateVariable(concatenatedInput);
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = requestProcessor.verifyStatement(concatenatedInput);
            response = String.format(STATEMENT_FORMAT, userInput, Boolean.toString(validStatement).toUpperCase());
        }

        return response;
    }

    private void validateInput(String input) {
        if (!inputValidator.isValid(input)) {
            throw new RuntimeException(INVALID_INPUT_MESSAGE);
        }
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return requestProcessor.variables();
    }
}
