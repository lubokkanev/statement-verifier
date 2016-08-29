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

        for (int i = 0; i < valueStatement.length(); ++i) {
            char character = valueStatement.charAt(i);

            if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {
                String variable = String.valueOf(character);

                if (!variableManager.containsVariable(variable)) {
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
                valueStatement.replace(i, i + 1, variableManager.getVariable(variable).getValue());
            }
        }

        return correct(valueStatement);
    }

    private String createRPN(String input) {
        StringBuilder result = new StringBuilder();
        Stack<Character> operationStack = new Stack<>();
        char[] charInput = input.toCharArray();
        for (int i = 0; i < charInput.length; i++) {
            switch (charInput[i]) {
                case '+':
                case '-':
                    while (!operationStack.empty()
                            && (operationStack.peek() == '*' || operationStack.peek() == '/')) {
                        result.append(' ').append(operationStack.pop());
                    }
                case '*':
                case '/':
                    result.append(' ');
                case '(':
                    operationStack.push(charInput[i]);
                case ' ':
                    break;
                case ')':
                    while (!operationStack.empty() && operationStack.peek() != '(') {
                        result.append(' ').append(operationStack.pop());
                    }
                    if (!operationStack.empty()) {
                        operationStack.pop();
                    }
                    break;
                default:
                    result.append(charInput[i]);
                    break;
            }

        }
        while (!operationStack.isEmpty()) {
            result.append(' ').append(operationStack.pop());
        }
        return result.toString();
    }
    
    private Double calculateRPN(String input){
        StringTokenizer tokens = new StringTokenizer(input);
        Stack<Double> stack = new Stack<>();
        while(tokens.hasMoreTokens()){
            String current = tokens.nextToken();
            if(VariableTypeParserImpl.isNumber(current)){
                stack.push(Double.parseDouble(current));
            }else{
                Double right = stack.pop();            
                Double left = stack.pop();
                switch(current){
                    case "+": stack.push(left + right); break;
                    case "-": stack.push(left - right); break;
                    case "*": stack.push(left * right); break;
                    case "/": stack.push(left / right); break;
                }
            }
        }
        return stack.pop();
    }

    private boolean correct(StringBuilder builder) {
        // TODO: Niki / Lubo      
        return true;
    }
}
