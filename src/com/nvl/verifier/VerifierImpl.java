package com.nvl.verifier;

import com.nvl.variable.Variable;
import com.nvl.verifier.determinator.Determinator;
import com.nvl.verifier.determinator.InputType;

import java.util.Set;

public class VerifierImpl implements Verifier {
    private Determinator determinator;
    private AssertionVerifier assertionVerifier;

    private static final String NEW_VARIABLE_MESSAGE = "Variable added successfully. ";
    private static final String EXISTING_VARIABLE_MESSAGE = "Variable updated successfully. ";
    private static final String STATEMENT_FORMAT = "The statement is %s.";

    public VerifierImpl(Determinator determinator, AssertionVerifier assertionVerifier) {
        this.determinator = determinator;
        this.assertionVerifier = assertionVerifier;
    }

    @Override
    public String verify(String userInput) {
        InputType inputType = determinator.determineInput(userInput);
        String response = null;

        if (inputType == InputType.NEW_VARIABLE) {
            assertionVerifier.addVariable(userInput);
            response = NEW_VARIABLE_MESSAGE;
        } else if (inputType == InputType.EXISTING_VARIABLE) {
            assertionVerifier.updateVariable(userInput);
            response = EXISTING_VARIABLE_MESSAGE;
        } else if (inputType == InputType.STATEMENT) {
            boolean validStatement = assertionVerifier.evaluateStatement(userInput);
            response = String.format(STATEMENT_FORMAT, Boolean.toString(validStatement));
        }

        return response;
    }

    @Override
    public Set<Variable> variables() {
        return assertionVerifier.variables();
    }
}
