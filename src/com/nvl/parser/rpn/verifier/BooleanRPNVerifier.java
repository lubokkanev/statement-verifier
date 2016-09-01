/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.parser.rpn.verifier;

import com.nvl.parser.rpn.AbstractRPNVerifier;
import com.nvl.parser.value.VariableTypeParserImpl;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author niki
 */
public class BooleanRPNVerifier extends AbstractRPNVerifier {

    //
    public boolean correct(StringBuilder builder) {
        String input = builder.toString();
        String operation = parseOperation(input);   //determine the operation (==, <=, >, !=)

        String[] split = input.split(operation);   //split by the operation
        String leftExpreion = split[0].trim();            //left expresion
        String rightExpresion = split[1].trim();           //right expression

        String leftRPN = createRPN(leftExpreion);           //RPN for the left expresion
        String rightRPN = createRPN(rightExpresion);        //RPN for the right expresion

        Boolean left = calculateRPN(leftRPN);       //left string result
        Boolean right = calculateRPN(rightRPN);     //right string result
        return compare(left, right, operation);             //compare them with the operation
    }  //end of correct

    private String createRPN(String input) {
        StringBuilder result = new StringBuilder();   //builder for the final result (RPN)
        Stack<String> operationStack = new Stack<>();  //sttack for the operation
        char[] charInput = input.toCharArray();  //char array for the input
        int i = 0;
        while (i < charInput.length) {   //iterrate through the input
            String sw;
            if (i < charInput.length - 1 && charInput[i] == charInput[i + 1]) {
                sw = new String(charInput, i, 2);
                i++;
            } else {
                sw = new String(Character.toString(charInput[i]));
            }
            switch (sw) {
                case "|":
                case "||":
                    while (!operationStack.empty()
                            && (operationStack.peek().equals("!") || operationStack.peek().equals("&")
                            || operationStack.peek().equals("&&"))) {   //if the previous operations in the stack have higher priorities
                        result.append(' ').append(operationStack.pop());                          // add them to result
                    }
                case "&":
                case "&&":
                    while (!operationStack.empty()
                            && (operationStack.peek().equals("!"))) {   //if the previous operations in the stack have higher priorities
                        result.append(' ').append(operationStack.pop());                          // add them to result
                    }
                    result.append(' ');
                case "!":
                case "(":
                    operationStack.push(String.valueOf(sw));
                case " ":
                    break;
                case ")":
                    while (!operationStack.empty() && operationStack.peek().equals("(")) {   // pop everything from stack to the result until we get to the '('
                        result.append(' ').append(operationStack.pop());
                    }
                    if (!operationStack.empty()) {    //remove the '('
                        operationStack.pop();
                    }
                    break;
                default:
                    result.append(sw);    // we have a letter
                    break;
            }  //end of switch
            i++;
        }  //end of while
        while (!operationStack.isEmpty()) {  //pop every operation from the stack to the result
            result.append(' ').append(operationStack.pop());
        }  //end of while
        return result.toString();  //return resulter RPN
    }  //end of create RPN

    private Boolean calculateRPN(String input) {
        StringTokenizer tokens = new StringTokenizer(input);  //tokenize the input by ' '
        Stack<Boolean> stack = new Stack<>();  //stack for the bools
        while (tokens.hasMoreTokens()) {   //while we have more tokens
            String current = tokens.nextToken();   //get current token
            if (current.equalsIgnoreCase("true") || current.equalsIgnoreCase("false")) {   //if the token is a number, push it in the stack
                stack.push(Boolean.parseBoolean(current));
            } else {    //else it is operation
                Boolean right = stack.pop();  //get the right number
                Boolean left;
                switch (current) {    //current is an operation, so wi push the resulted number in the stack
                    case "!":
                        stack.push(!right);
                        break;
                    case "&":
                        left = stack.pop();
                        stack.push(Boolean.logicalAnd(left, right));
                        break;
                    case "|":
                        left = stack.pop();
                        stack.push(Boolean.logicalOr(left, right));
                        break;
                    case "&&":
                        left = stack.pop();
                        stack.push(left && right);
                        break;
                    case "||":
                        left = stack.pop();
                        stack.push(left || right);
                        break;
                } //end of switch
            }  // end of if/else
        }     //end of while
        return stack.pop();   //the result is in the stack(last element)
    }   //end of calculate RPN

}
