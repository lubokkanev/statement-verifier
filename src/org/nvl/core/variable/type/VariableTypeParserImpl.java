package org.nvl.core.variable.type;

import org.nvl.core.variable.EvaluatedVariable;
import org.nvl.core.variable.UnevaluatedVariable;
import org.nvl.core.variable.VariableType;

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
        for (char aCharValue : value.toCharArray()) {
            if (aCharValue == '[' || aCharValue == '{') {
                return true;
            }
        }

        return false;
    }
}
