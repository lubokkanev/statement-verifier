package com.nvl.parser.value;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.UnevaluatedVariable;
import com.nvl.variable.VariableType;

public class VariableTypeParserImpl implements VariableTypeParser {
    @Override
    public EvaluatedVariable parse(UnevaluatedVariable variable) {
        if (isArray(variable.getValue())) {
            return new EvaluatedVariable(VariableType.ARRAY, String.valueOf(variable.getValue()).toString(), variable.getName());
        } else if (isBoolean(variable.getValue())) {
            return new EvaluatedVariable(VariableType.BOOLEAN, String.valueOf(Boolean.parseBoolean(variable.getValue())), variable.getName());
        } else if (isNumber(variable.getValue())) {
            return new EvaluatedVariable(VariableType.NUMBER, Integer.valueOf(variable.getValue()).toString(), variable.getName());
        } else {
            return new EvaluatedVariable(VariableType.STRING, variable.getValue(), variable.getName());
        }
    }

    private boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
    
    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isArray(String value){
        char[] charValue = value.toCharArray();
        for(int i = 0; i < charValue.length; i++)
            if(charValue[i] == '[' || charValue[i] == '{'){
                return true;
            }
                
        return false;
    }
}
