package com.nvl.verifier;

import com.nvl.variable.Variable;

import java.util.Set;

public interface Verifier {

    /*This takes the user's input, parses it, decides which function to call
        and returns the result to be displayed as string
        e.g. "Variable added/updated successfully"
            ,"Something is True/false "
            ,"X = 7"  */
    String verify(String userInput);

    /*returns all variables, which the users has entered*/
    Set<Variable> variables();
}