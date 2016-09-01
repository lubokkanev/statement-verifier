package com.nvl.parser.statement;

import com.nvl.parser.rpn.verifier.ArrayRPNVerifier;
import com.nvl.parser.rpn.verifier.BooleanRPNVerifier;
import com.nvl.parser.rpn.verifier.NumberRPNVerifier;
import com.nvl.parser.rpn.RPNVerifier;
import com.nvl.parser.rpn.verifier.StringRPNVerifier;
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
        RPNVerifier verify;
        boolean isBooleanOperation = false, isStringOperation = false, isArrayOperation = false;
        for (int i = 0; i < valueStatement.length(); ++i) {
            
            char character = valueStatement.charAt(i);
            
            if(character == '{'){           //if we have opening bracket for array
                isArrayOperation = true;        //we leave the input like that
                do{
                    i++;
                    character = valueStatement.charAt(i);
                }while(character != '}');       //so we iterrate while we reach the closing bracket
                continue;
            }

            if (character == '\'') {            //if we have quotes in the statement than we have strings and string operations, so we leave it like that
                isStringOperation = true;               
                do {
                    i++;
                    character = valueStatement.charAt(i);
                } while (character != '\'');        //iterate through the input until we get past the string
                continue;
            }
            
            if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {   //change variable with its value
                String variable = String.valueOf(character);
                if (variable.startsWith("t") || variable.startsWith("T")) {             //if we find t or T
                    if (valueStatement.substring(i, i + 4).equalsIgnoreCase("true")) {      //if its the keyword TRUE, we leave it like that
                        i = i + 3;                                                          //so we increment i
                        isBooleanOperation = true;                                           //and we have boolean operations
                        continue;
                    }    
                }
                if(variable.startsWith("f") || variable.startsWith("F")){                   //if we find f or F
                    if (valueStatement.substring(i, i + 5).equalsIgnoreCase("false")) {     //and it is from the keyword FALSE, we leave it like that
                        i = i + 4;                                                          //so we increment i
                        isBooleanOperation = true;                                          //and we have boolean operations
                        continue;
                    }
                }
                if (!variableManager.containsVariable(variable)) {                      //if the variable is not declared
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
                valueStatement.deleteCharAt(i);                                             //remove the variable from the resulted string
                valueStatement.insert(i, variableManager.getVariable(variable).getValue());     //and add its value
                i--;                                                                    //return one index to check the value
            }
        }
        if (isStringOperation) {        //we have string operations
            int i = 0;
            while (i < valueStatement.length()) {
                if (valueStatement.charAt(i) == '\'') //because the RPN for strings does not work with Quotes
                {
                    valueStatement.deleteCharAt(i);
                } else {
                    i++;
                }
            }
            verify = new StringRPNVerifier();       //we verify the statement
            return verify.correct(valueStatement);
        }
        if(isArrayOperation){
            verify = new ArrayRPNVerifier();
            return verify.correct(valueStatement);
        }
        if(isBooleanOperation){                     //we have boolean operations
            verify = new BooleanRPNVerifier();
            return verify.correct(valueStatement);
        }
        verify = new NumberRPNVerifier();           //we have number operations
        return verify.correct(valueStatement);
    }
}
