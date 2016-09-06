/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nvl.core.rpn.verifier;

import org.nvl.core.rpn.AbstractStringNumberRpnVerifier;
import org.nvl.core.variable.type.VariableTypeParserImpl;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author niki
 */
public class StringRpnVerifier extends AbstractStringNumberRpnVerifier {

    //only for strings
    public boolean correct(StringBuilder builder) {
        String input = builder.toString();
        String operation = parseOperation(input);   //determine the operation (==, <=, >, !=)

        String[] split = input.split(operation);   //split by the operation
        String leftExpression = split[0].trim();            //left expression
        String rightExpression = split[1].trim();           //right expression

        String leftRPN = createRPN(leftExpression);           //RPN for the left expression
        String rightRPN = createRPN(rightExpression);        //RPN for the right expression

        String left = calculateRpnForString(leftRPN);       //left string result
        String right = calculateRpnForString(rightRPN);     //right string result
        return compare(left, right, operation);             //compare them with the operation
    }  //end of correct

    //calculate the reverse polish notation for strings
    private String calculateRpnForString(String input) {
        StringTokenizer tokens = new StringTokenizer(input);  //tokenize the input by ' '
        Stack<String> stack = new Stack<>();  //stack for the numbers
        while (tokens.hasMoreTokens()) {   //while we have more tokens
            String current = tokens.nextToken();
            switch (current) {        //switch to see if current string is operation or string
                case "+":
                    plus(stack);
                    break;
                case "*":
                    multiply(stack);
                    break;
                default:
                    stack.push(current);       //current is string
            }   //end of switch
        }   //end of while
        return stack.pop();     //return result
    } //end of calculateRpnForStrings

    //concatenate the top 2 strings
    private void plus(Stack<String> stack) {
        String right = stack.pop();
        String left = stack.pop();
        stack.push(left.concat(right));
    } //end of plus

    //concatenate string given amount of times
    private void multiply(Stack<String> stack) {
        boolean leftIsNumber = false, rightIsNumber = false;    //at least one will be a number
        Object left, right;            //values for the operation
        int count;      //how many times to concatenate
        String strToConcatenate;
        if (VariableTypeParserImpl.isNumber(stack.peek())) {   //if right is number
            right = Integer.parseInt(stack.pop());
            rightIsNumber = true;
        } else {
            right = stack.pop();        //else its string
        }
        if (VariableTypeParserImpl.isNumber(stack.peek())) {
            left = Integer.parseInt(stack.pop());          //if left is number
            leftIsNumber = true;
        } else {
            left = stack.pop();         //else its string
        }
        if (leftIsNumber && rightIsNumber) {         //both are numbers
            stack.push(Integer.toString((Integer) left * (Integer) right));  // so we parse them to ints, multiply them and push it to stack
        } else {                                      //only one is number
            if (leftIsNumber) {    //left is number
                count = (int) left;   //concatenate left times
                strToConcatenate = (String) right;   //the right string
            } else {   //right is number
                count = (int) right;   //concatenate right times
                strToConcatenate = (String) left;       //the left string
            }
            StringBuilder sb = new StringBuilder();   //create the resulted string
            for (int i = 0; i < count; i++) {
                sb.append(strToConcatenate);
            }
            stack.push(sb.toString());   //push the resulted string
        }       //end of if
    }//end of multiply
}
