/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.parser.rpn.verifier;

import com.nvl.parser.rpn.AbstractStringNumberRPNVerifier;
import com.nvl.parser.value.VariableTypeParserImpl;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author niki
 */
public class NumberRPNVerifier extends AbstractStringNumberRPNVerifier{
    /*this works only for numbers*/
    public boolean correct(StringBuilder builder) {
        String input = builder.toString();   //convert builder to string
        String operation = parseOperation(input);   //deternmine the operation (==, >=, <, !=)
        String[] split = input.split(operation);   // split the input by the operation
        String leftString = split[0].trim();        //take left side of the statement
        String rightString = split[1].trim();     //take right side of the statement
        String leftRPN = createRPN(leftString);    //create Reverse Polish Notation for the left argument
        String rightRPN = createRPN(rightString);  //create Reverse Polish Notation for the right argument
        Double left = calculateRPN(leftRPN);    //calculate the values
        Double right = calculateRPN(rightRPN);
        return compare(left, right, operation);
    }   //end of correctForNumbers
    
    //calculates the value of the expresion by RPN
    private Double calculateRPN(String input) {
        StringTokenizer tokens = new StringTokenizer(input);  //tokenize the input by ' '
        Stack<Double> stack = new Stack<>();  //stack for the numbers
        while (tokens.hasMoreTokens()) {   //while we have more tokens
            String current = tokens.nextToken();   //get current token
            if (VariableTypeParserImpl.isNumber(current)) {   //if the token is a number, push it in the stack
                stack.push(Double.parseDouble(current));
            } else {    //else it is operation
                Double right = stack.pop();  //get the right number
                Double left = stack.pop();   //get the left
                switch (current) {    //current is an operation, so wi push the resulted number in the stack
                    case "+":
                        stack.push(left + right);
                        break;
                    case "-":
                        stack.push(left - right);
                        break;
                    case "*":
                        stack.push(left * right);
                        break;
                    case "/":
                        stack.push(left / right);
                        break;
                } //end of switch
            }  // end of if/else
        }     //end of while
        return stack.pop();   //the result is in the stack(last element)
    }   //end of calculate RPN
}
