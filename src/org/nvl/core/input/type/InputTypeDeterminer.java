package org.nvl.core.input.type;

/**
 * Determines the variable type from its value
 */
public interface InputTypeDeterminer {
    InputType determineType(String input);
}
