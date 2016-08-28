package com.nvl.parser.variable_definition;

import com.nvl.variable.UnevaluatedVariable;

/**
 * Parses a variable definition to it's parts in string form
 */
public interface VariableDefinitionParser {
    UnevaluatedVariable parse(String definition);
}
