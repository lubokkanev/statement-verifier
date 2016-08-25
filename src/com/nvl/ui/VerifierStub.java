/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.ui;

import com.nvl.variable.Variable;
import com.nvl.variable.VariableType;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ratzy
 */
public class VerifierStub implements Verifier {

       /*This takes the user's input, parses it, decides which function to call
        and returns the result to be displayed as string 
        e.g. "Variable added/updated successfully"
            ,"Something is True/false "
            ,"X = 7"  */
    @Override
    public String verify(String userInput) {
        return "Variable added successfully";
    }
    
    /*returns all variables, which the users has entered*/
    @Override
    public Set<Variable> variables() {
        Set<Variable> result = new HashSet<>();
        result.add(new Variable("X", VariableType.NUMBER, "5"));
        result.add(new Variable("Y", VariableType.NUMBER, "26"));
        result.add(new Variable("Z", VariableType.NUMBER, "42"));
        return result;
    }
}
