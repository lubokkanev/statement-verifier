package com.nvl.verifier.determiner;

/**
 * Determines the variable type from its value
 */
public interface InputTypeDeterminer {
    InputType determineType(String input);
}
