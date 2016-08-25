package com.nvl.ui;

import com.nvl.variable.Variable;

import java.util.Set;

public interface Verifier {
    String verify(String userInput);

    Set<Variable> variables();
}
