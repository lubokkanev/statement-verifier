/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nvl.parser.rpn;

import com.nvl.parser.value.VariableTypeParserImpl;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author niki
 */
public class ArrayRPNVerifier extends AbstractStringNumberRPNVerifier{

    @Override
    public boolean correct(StringBuilder builder) {
        String input = builder.toString();
        String operation = parseOperation(input);   //determine the operation (==, <=, >, !=)
        
        String[] split = input.split(operation);   //split by the operation
        String leftExpreion = split[0].trim();            //left expresion
        String rightExpresion = split[1].trim();           //right expression
        
        String leftRPN = createRPN(leftExpreion);           //RPN for the left expresion
        String rightRPN = createRPN(rightExpresion);        //RPN for the right expresion
        
        String left = calculateRPN(leftRPN);       //left string result
        String right = calculateRPN(rightRPN);     //right string result
        return compare(left, right, operation);             //compare them with the operation
    }
    
    private String calculateRPN(String input){
        StringTokenizer tokens = new StringTokenizer(input);  //tokenize the input by ' '
        Stack<String> stack = new Stack<>();  //stack for the numbers
        
        while (tokens.hasMoreTokens()) {   //while we have more tokens
            String current = tokens.nextToken();
            
            switch(current){        //swtich to see if current string is operation or string
                case "+":
                case "*": execute(stack,current); break;
                case "^": concatenate(stack); break;
                default: stack.push(current);       //current is string
            }   //end of switch
        }   //end of while
        return stack.pop();     //return result
    } //end of calculateRPN
    
        
    private void execute(Stack<String> stack, String operation){  //execute + and *
        boolean leftIsNumber = false, rightIsNumber = false;        //if any of the operands is a number
        String right = stack.pop();     //right operand
        String left = stack.pop();      //left operand
        
        if(VariableTypeParserImpl.isNumber(left))   //if the left operand is a number
            leftIsNumber = true;
        
        if(VariableTypeParserImpl.isNumber(right))  //if the right operand is a number
            rightIsNumber = true;
        
        if(leftIsNumber && rightIsNumber){          //if both are numbers
            stack.push(executeOperation(left, right, operation));   //than just do the operations naturally
            return;
        }
        
        if(leftIsNumber && !rightIsNumber){     //if the left operand is a number and the right one is an array
            stack.push(executeOperationNumberArray(right, left, operation));    //do the operation with array and number
            return;
        }
        
        if(!leftIsNumber && rightIsNumber){      //if the left is an array and the right one is number
            stack.push(executeOperationNumberArray(left, right, operation));        //do the operation with array and number
            return;
        }else{
            stack.push(executeOperationArrays(left, right, operation));     //else both are arrays, so we do the operation with array
            return;
        }   //end of if/else
    }   //end of execute
    
    //executes the given operation(+ or *) over 2 integers
    private String executeOperation(String left, String right, String operation){
        Integer r = Integer.parseInt(left);
        Integer l = Integer.parseInt(right);     
        switch(operation){
            case "+": return Integer.toString(l+r);
            default : return Integer.toString(l*r);
        } //end of switch
    }   //end of executeOperataion
    
    
    //execute for operation(+ or *) with array operand and number operand
    private String executeOperationNumberArray(String array, String number, String operation){
        StringBuilder result = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(array.substring(1, array.length()-1), ", ");       //with this tokenizer we can get every value in the array (, is token)
        result.append('{');     //the result will be an array so we start with '{'
        
        while(tokens.hasMoreTokens()){      //while we have more numbers
            result.append(executeOperation(tokens.nextToken(), number, operation));     //execute the operation over the number and the current element of the array(token) and save the result
            result.append(',');                                                         //append ',' to separate the elements in the array
        }       //end of while
        
        result.deleteCharAt(result.length()-1);         //delete the last ',' added
        result.append('}');                     //add the closing '}'
        return result.toString();       //return result
    }   //end of executeOperationNumberArray
    
    
    //execute operation(+ or *) if we have two array operands
    private String executeOperationArrays(String leftArray, String rightArray, String operation){
        StringBuilder result = new StringBuilder();         
        StringTokenizer leftTokens = new StringTokenizer(leftArray.substring(1, leftArray.length()-1), ", ");  //tokenize the first array by ',' (we get its elements)
        StringTokenizer rightTokens = new StringTokenizer(rightArray.substring(1, rightArray.length()-1), ", ");    //tokenize the second array by ',' (we get its elements)        
        result.append('{');     //the result will be an array so we start with '{'
        
        while(leftTokens.hasMoreTokens() && rightTokens.hasMoreTokens()){       //while we have more tokens in both arrays
            result.append(executeOperation(leftTokens.nextToken(), rightTokens.nextToken(), operation));    //we do the operation over the corresponding el. of the arrays
                                                                                                            //and save the result
            result.append(',');                                                                             //than we separate the elements in the result
        }
        
        if(leftTokens.hasMoreTokens()){         //if the left array has more elements than the right one
            result.append(leftTokens.nextToken());      //add the rest elements to the result
            result.append(',');                         //separate them
        }
        
        if(rightTokens.hasMoreTokens()){        //if the right array --||--
            result.append(rightTokens.nextToken());
            result.append(',');
        }
        
        result.deleteCharAt(result.length()-1);     //delete the last ','
        result.append('}');                     //close the array
        return result.toString();
    }   //end of executeOperationArrays
    

    //concatenates both arrays if we have ^ operation
    private void concatenate(Stack<String> stack){
        String right = stack.pop(); //right array
        String left = stack.pop();  //left array
       
        StringBuilder result = new StringBuilder(left.substring(0, left.length() - 1));     //get the first element, without the closing } in the result
        result.append(',');                                                                 //separate its last element with a ,
        result.append(right.substring(1, right.length()));                                  //append the second array without the opening {
        stack.push(result.toString());                                                      //push result to stack
    }//end of concatenate
    
    
}
