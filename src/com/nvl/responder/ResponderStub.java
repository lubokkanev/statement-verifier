/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.responder;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ratzy
 */
public class ResponderStub implements Responder {

    /*This takes the user's input, parses it, decides which function to call
     and returns the result to be displayed as string
     e.g. "Variable added/updated successfully"
         ,"Something is True/false "
         ,"X = 7"  */
    /* (non-Javadoc)
     * @see com.nvl.ui.Verifier#verify(java.lang.String)
	 */
    @Override
    public String process(String userInput) {
        return "Variable added successfully";
    }

    /*returns all variables, which the users has entered*/
    /* (non-Javadoc)
     * @see com.nvl.ui.Verifier#variables()
	 */
    @Override
    public Set<EvaluatedVariable> variables() {
        Set<EvaluatedVariable> result = new HashSet<>();
        result.add(new EvaluatedVariable(VariableType.NUMBER, "5", "X"));
        result.add(new EvaluatedVariable(VariableType.STRING, "word", "Y"));
        result.add(new EvaluatedVariable(VariableType.BOOLEAN, "False", "Z"));
        return result;
    }
}
