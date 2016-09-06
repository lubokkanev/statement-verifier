package org.nvl.core.variable.type;

import org.nvl.core.variable.EvaluatedVariable;
import org.nvl.core.variable.UnevaluatedVariable;

/**
 * Parses the variable value to find its type
 */
public interface VariableTypeParser {
    EvaluatedVariable parse(UnevaluatedVariable variable);
}
