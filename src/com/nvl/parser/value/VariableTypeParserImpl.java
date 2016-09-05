package com.nvl.parser.value;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.UnevaluatedVariable;
import com.nvl.variable.VariableType;

public class VariableTypeParserImpl implements VariableTypeParser {
    @Override
    public EvaluatedVariable parse(UnevaluatedVariable variable) {
        if (isArray(variable.getValue())) {
            return new EvaluatedVariable(variable.getName(), String.valueOf(variable.getValue()).toString(), VariableType.ARRAY);
        } else if (isBoolean(variable.getValue())) {
            return new EvaluatedVariable(variable.getName(), String.valueOf(Boolean.parseBoolean(variable.getValue())), VariableType.BOOLEAN);
        } else if (isNumber(variable.getValue())) {
            return new EvaluatedVariable(variable.getName(), Integer.valueOf(variable.getValue()).toString(), VariableType.NUMBER);
        } else {
            return new EvaluatedVariable(variable.getName(), variable.getValue(), VariableType.STRING);
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

    private boolean isArray(String value) {
        char[] charValue = value.toCharArray();
        for (int i = 0; i < charValue.length; i++) {
            if (charValue[i] == '[' || charValue[i] == '{') {
                return true;
            }
        }

        return false;
    }
}
