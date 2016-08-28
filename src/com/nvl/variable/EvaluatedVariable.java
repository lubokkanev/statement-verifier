package com.nvl.variable;

public class EvaluatedVariable {
    public EvaluatedVariable(VariableType type, String value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    private VariableType type;
    private String value;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
