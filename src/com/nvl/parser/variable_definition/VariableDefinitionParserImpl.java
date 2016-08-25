package com.nvl.parser.variable_definition;

public class VariableDefinitionParserImpl implements VariableDefinitionParser {
    @Override
    public UnevaluatedResult parse(String definition) {
        String name;
        String value;

        // TODO: Lubo
        name = definition;
        value = definition;

        return new UnevaluatedResult(name, value);
    }
}
