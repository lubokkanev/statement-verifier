package com.nvl.parser.variable_definition;

import com.nvl.variable.UnevaluatedVariable;

public class VariableDefinitionParserImpl implements VariableDefinitionParser {
    @Override
    public UnevaluatedVariable parse(String definition) {
        String[] split = definition.split("=");
        String name = split[0].trim();
        String value = split[1].trim();

        return new UnevaluatedVariable(name, value);
    }
}
