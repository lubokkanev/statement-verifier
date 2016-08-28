package com.nvl.parser.value;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.UnevaluatedVariable;

/**
 * Parses the variable value to find its type
 */
public interface VariableTypeParser {
    EvaluatedVariable parse(UnevaluatedVariable variable);
}
