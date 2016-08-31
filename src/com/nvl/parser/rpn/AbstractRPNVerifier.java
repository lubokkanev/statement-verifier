/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.parser.rpn;

/**
 *
 * @author niki
 */
public abstract class AbstractRPNVerifier implements RPNVerifier{

    @Override
    public abstract boolean correct(StringBuilder input);
    
    //execute operation 
    protected <E extends Comparable<E>> boolean compare(E left, E right, String operation) {
        switch (operation) {
            case "==":
                return left.compareTo(right) == 0;
            case ">":
                return left.compareTo(right) > 0;
            case "<":
                return left.compareTo(right) < 0;
            case ">=":
                return left.compareTo(right) >= 0;
            case "<=":
                return left.compareTo(right) >= 0;
            case "!=":
                return left.compareTo(right) != 0;
            default:
                return false;
        }   //end of switch
    }   //end of compare
    
    //finds the operation of the statement
    protected String parseOperation(String input) {
        char[] charInput = input.toCharArray();
        int i = 0;
        while (charInput[i] != '=' && charInput[i] != '>' && charInput[i] != '<' && (charInput[i] != '!' || charInput[i+1] != '=')) {  //iterrate while input[i] is not an operation symbol
            i++;
        }
        if (charInput[i + 1] != '=') {    //if operation is > or <
            return input.substring(i, i + 1);
        } else {    //operation is  >= or <= or  == or !=
            return input.substring(i, i + 2);
        }//end if
    } //end of parseOperation
}
