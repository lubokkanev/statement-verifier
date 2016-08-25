package com.nvl.parser.value;

import com.nvl.variable.VariableType;

public class ValueParserImpl implements ValueParser {
    @Override
    public EvaluatedResult parse(String value) {
        // TODO: Lubo
        return new EvaluatedResult(VariableType.BOOLEAN, "False");
    }
}
