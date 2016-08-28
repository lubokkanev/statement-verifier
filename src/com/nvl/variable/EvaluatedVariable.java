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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EvaluatedVariable variable = (EvaluatedVariable) o;
        if (name != null ? !name.equals(variable.name) : variable.name != null) {
            return false;
        }
        return true;
    }

    @Override

    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
