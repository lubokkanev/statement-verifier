package com.nvl.responder;

import com.nvl.variable.Variable;
import com.nvl.verifier.determinator.InputTypeDeterminator;
import com.nvl.verifier.InputProcessor;
import com.nvl.verifier.determinator.InputType;

import java.util.Set;

public class ResponderImpl implements Responder {
    private InputTypeDeterminator determinator;
    private InputProcessor inputProcessor;

    private static final String NEW_VARIABLE_MESSAGE = "Variable added successfully. ";
    private static final String EXISTING_VARIABLE_MESSAGE = "Variable updated successfully. ";
    private static final String STATEMENT_FORMAT = "The statement is %s.";

    public ResponderImpl(InputTypeDeterminator determinator, InputProcessor assertionVerifier) {
        this.determinator = determinator;
        this.inputProcessor = assertionVerifier;
    }

    @Override
    public String process(String userInput) {
        InputType inputType = determinator.determineInput(userInput);
        String response = null;

        if (inputType == InputType.NEW_VARIABLE) {
            inputProcessor.addVariable(userInput);
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            inputProcessor.updateVariable(userInput);
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = inputProcessor.verifyStatement(userInput);
            response = String.format(STATEMENT_FORMAT, Boolean.toString(validStatement));
        }

        return response;
    }

    @Override
    public Set<Variable> variables() {
        return inputProcessor.variables();
    }
}
