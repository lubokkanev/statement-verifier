package com.nvl.parser.statement;

import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.variable.manager.VariableManager;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class StatementProcessorImpl implements StatementProcessor {

    private VariableManager variableManager;

    private static final String INVALID_INPUT_MESSAGE = "You caught a bug! Invalid input. ";

    public StatementProcessorImpl(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public boolean verifyStatement(String statement) {
        StringBuilder valueStatement = new StringBuilder(statement);
        boolean areQuotes = false;
        for (int i = 0; i < valueStatement.length(); ++i) {
            char character = valueStatement.charAt(i);
            if(character == '"'){
                areQuotes = true;
                do{
                    i++;
                    character = valueStatement.charAt(i);
                }while(character != '"');
                continue;
            }
            if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {
                String variable = String.valueOf(character);
                if (!variableManager.containsVariable(variable)) {
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
                valueStatement.deleteCharAt(i);
                valueStatement.insert(i, variableManager.getVariable(variable).getValue());
                i--;
                //valueStatement.replace(i, i + 1, variableManager.getVariable(variable).getValue());
            }
        }
        if(areQuotes){
            int i = 0;
            while(i < valueStatement.length()){
                if(valueStatement.charAt(i) == '"') //because the RPN for strings does not work with Quotes
                    valueStatement.deleteCharAt(i);
                else
                    i++;
            }
            return correctForStrings(valueStatement);
        }
            
        return correctForNumbers(valueStatement);  //no quotes -> no strings -> numbers
    }

    //creates the Reverse Polish Notation
    private String createRPN(String input) {
        StringBuilder result = new StringBuilder();   //builder for the final result (RPN)
        Stack<Character> operationStack = new Stack<>();  //sttack for the operation
        char[] charInput = input.toCharArray();  //char array for the input
        int i = 0;
        if (charInput[i] == '-' || charInput[i] == '+') {    //if the expresion begins with - or + (e.g. -52+3..)
            result.append(charInput[i]);
            i++;
        } //end of if

        while (i < charInput.length) {   //iterrate through the input
            switch (charInput[i]) {
                case '+':
                case '-':
                    while (!operationStack.empty()
                            && (operationStack.peek() == '*' || operationStack.peek() == '/')) {   //if the previous operations in the stack have higher priorities
                        result.append(' ').append(operationStack.pop());                          // add them to result
                    }
                case '*':
                case '/':
                    result.append(' ');
                case '(':
                    operationStack.push(charInput[i]);
                case ' ':
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
        return result.toString();  //return resulter RPN
    }  //end of create RPN

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
    
    
    //calculate the reverse polish notation for strings
    private String calculateRPNforString(String input){
        StringTokenizer tokens = new StringTokenizer(input);  //tokenize the input by ' '
        Stack<String> stack = new Stack<>();  //stack for the numbers
        while (tokens.hasMoreTokens()) {   //while we have more tokens
            String current = tokens.nextToken();
            if(current.equals("+")){       //concatenate the top 2 strings
                String right = stack.pop();
                String left = stack.pop();
                stack.push(left.concat(right));
            }else if(current.equals("*")){    //concatenate the string before the top given amount of times (the top should be the amount)
                Integer count = Integer.parseInt(stack.pop());   //this should be a number
                String left = stack.pop();
                StringBuilder sb = new StringBuilder(); 
                for(int i = 0; i < count; i++){
                    sb.append(left);
                }
                stack.push(sb.toString());
            }else{
                stack.push(current);   //current is a string
            } //end of if/else
        } //end of switch
        return stack.pop(); 
    } //end of calculateRPNforStrings

    //finds the operation of the statement
    private String parseOperation(String input) {
        char[] charInput = input.toCharArray();
        int i = 0;
        while (charInput[i] != '=' && charInput[i] != '>' && charInput[i] != '<' && charInput[i] != '!') {  //iterrate while input[i] is not an operation symbol
            i++;
        }
        if (charInput[i + 1] != '=') {    //if operation is > or <
            return input.substring(i, i + 1);
        } else {    //operation is  >= or <= or  == or !=
            return input.substring(i, i + 2);
        }//end if
    } //end of parseOperation

    /*this works only for numbers*/
    private boolean correctForNumbers(StringBuilder builder) {
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

    //only for strings
    private boolean correctForStrings(StringBuilder builder) {
        String input = builder.toString();
        String operation = parseOperation(input);   //determine the operation (==, <=, >, !=)
        String[] split = input.split(operation);   //split by the operation
        String leftExpreion = split[0].trim();            //left expresion
        String rightExpresion = split[1].trim();           //right expression
        //TODO parse expresions
        
        String leftRPN = createRPN(leftExpreion);
        String rightRPN = createRPN(rightExpresion);
        
        
        String left = calculateRPNforString(leftRPN);
        String right = calculateRPNforString(rightRPN);
        return compare(left, right, operation);
    }  //end of correctForStrings
    
    
    //execute operation for Strings and Numbers
    private <E extends Comparable<E>> boolean compare(E left, E right, String operation){
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
    
    
    private boolean correct(StringBuilder builder) {
        // TODO: Niki / Lubo
        return correctForStrings(builder);
    }
}
