package com.nvl.ui;

import java.util.Set;

import com.nvl.variable.Variable;

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