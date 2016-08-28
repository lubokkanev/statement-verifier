package com.nvl.parser.value;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.UnevaluatedVariable;
import com.nvl.variable.VariableType;

public class VariableTypeParserImpl implements VariableTypeParser {
    @Override
    public EvaluatedVariable parse(UnevaluatedVariable variable) {
        if (isBoolean(variable.getValue())) {
            return new EvaluatedVariable(VariableType.BOOLEAN, String.valueOf(Boolean.parseBoolean(variable.getValue())), variable.getName());
        } else if (isNumber(variable.getValue())) {
            return new EvaluatedVariable(VariableType.NUMBER, Double.valueOf(variable.getValue()).toString(), variable.getName());
        } else {
            return new EvaluatedVariable(VariableType.STRING, variable.getValue(), variable.getName());
        }
    }

    private boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
