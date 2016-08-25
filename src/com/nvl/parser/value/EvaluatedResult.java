package com.nvl.parser.value;

import com.nvl.variable.VariableType;

public class EvaluatedResult {
    public EvaluatedResult(VariableType type, String value) {
        this.type = type;
        this.value = value;
    }

    private VariableType type;
    private String value;

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
