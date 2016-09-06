/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nvl.core.rpn;

import java.util.Stack;

/**
 * @author niki
 */
public abstract class AbstractStringNumberRpnVerifier extends AbstractRpnVerifier {

    @Override
    public abstract boolean correct(StringBuilder input);

    //creates the Reverse Polish Notation
    protected String createRPN(String input) {
        StringBuilder result = new StringBuilder();   //builder for the final result (RPN)
        boolean isInString = false;
        Stack<Character> operationStack = new Stack<>();  //sttack for the operation
        char[] charInput = input.toCharArray();  //char array for the input
        int i = 0;
        if (charInput[i] == '-' || charInput[i] == '+') {    //if the expresion begins with - or + (e.g. -52+3..)
            result.append(charInput[i]);
            i++;
        } //end of if
        while (i < charInput.length) {   //iterrate through the input
            if(charInput[i] == '\'')
                isInString = !isInString;
            switch (charInput[i]) {
                case '+':
                case '-':
                    while (!operationStack.empty() && (operationStack.peek() == '*' || operationStack.peek() == '/' ||
                            operationStack.peek() == '^')) {   //if the previous operations in the stack have higher priorities
                        result.append(' ').append(operationStack.pop());                          // add them to result
                    }
                case '*':
                case '/':
                    while (!operationStack.empty() && operationStack.peek() == '^') {   //if the previous operations in the stack have higher priorities
                        result.append(' ').append(operationStack.pop());                          // add them to result
                    }
                case '^':
                    result.append(' ');
                case '(':
                    operationStack.push(charInput[i]);
                    break;
                case ' ':
                    if(isInString)
                        result.append(charInput[i]);
                    break;
                case '\'':
                        result.append(charInput[i]);
                    break;
                case ')':
                    while (!operationStack.empty() && operationStack.peek() != '(') {   // pop everything from stack to the result until we get to the '('
                        result.append(' ').append(operationStack.pop());
                    }
                    if (!operationStack.empty()) {    //remove the '('
                        operationStack.pop();
                    }
                    break;
                default:
                    result.append(charInput[i]);    // we have a digit
                    break;
            }  //end of switch
            i++;
        }  //end of while
        while (!operationStack.isEmpty()) {  //pop every operation from the stack to the result
            result.append(' ').append(operationStack.pop());
        }  //end of while
        return result.toString();  //return resulted RPN
    }  //end of create RPN
}
