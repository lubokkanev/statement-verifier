package com.nvl.parser.statement;

import com.nvl.parser.rpn.BooleanRPNVerifier;
import com.nvl.parser.rpn.NumberRPNVerifier;
import com.nvl.parser.rpn.RPNVerifier;
import com.nvl.parser.rpn.StringRPNVerifier;
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
        boolean isBooleanOperation = false, areQuotes = false;
        String input = statement.toString();
        if (input.contains("true") || input.contains("false")) {
            isBooleanOperation = true;
        }
        for (int i = 0; i < valueStatement.length(); ++i) {
            
            char character = valueStatement.charAt(i);

            if (character == '"') {
                areQuotes = true;
                do {
                    i++;
                    character = valueStatement.charAt(i);
                } while (character != '"');
                continue;
            }
            if (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z') {
                String variable = String.valueOf(character);
                if (variable.startsWith("t")) {
                    if (valueStatement.substring(i, i + 4).equals("true")) {
                        i = i + 3;
                        isBooleanOperation = true;
                        continue;
                    }    
                }
                if(variable.startsWith("f")){
                    if (valueStatement.substring(i, i + 5).equals("false")) {
                        i = i + 4;
                        isBooleanOperation = true;
                        continue;
                    }
                }
                if (!variableManager.containsVariable(variable)) {
                    throw new RuntimeException(INVALID_INPUT_MESSAGE);
                }
                valueStatement.deleteCharAt(i);
                valueStatement.insert(i, variableManager.getVariable(variable).getValue());
                i--;
            }
        }
        if (areQuotes) {
            int i = 0;
            while (i < valueStatement.length()) {
                if (valueStatement.charAt(i) == '"') //because the RPN for strings does not work with Quotes
                {
                    valueStatement.deleteCharAt(i);
                } else {
                    i++;
                }
            }
            verify = new StringRPNVerifier();
            return verify.correct(valueStatement);
        }
        if(isBooleanOperation){
            verify = new BooleanRPNVerifier();
            return verify.correct(valueStatement);
        }
        verify = new NumberRPNVerifier();
        return verify.correct(valueStatement);
    }
}
