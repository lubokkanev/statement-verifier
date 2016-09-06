package org.nvl.core.responder;

import org.nvl.core.variable.EvaluatedVariable;

import java.util.Set;

public interface Responder {

    /**
     * This takes the user's input, parses it, decides which function to call
     * and returns the result to be displayed as string
     * e.g. "Variable added/updated successfully"
     * ,"Something is True/false "
     * ,"X = 7"
     */
    String process(String userInput);

    /**
     * returns all variables, which the users has entered
     */
    Set<EvaluatedVariable> variables();
}