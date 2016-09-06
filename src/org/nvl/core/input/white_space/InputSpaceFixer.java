package org.nvl.core.input.white_space;

import org.nvl.core.input.secure.StringSecurer;

public class InputSpaceFixer {
    private static final String ISOLATION_FORMAT = " %s ";
    private static final String NON_ISOLATION_FORMAT = "%s";

    public String fix(String input) {
        input = doNotIsolate(input);
        input = isolate(input);
        input = new StringSecurer(input).proofString();
        return input.trim();
    }

    private String doNotIsolate(String input) {
        String[] shouldNotBeIsolatedElements = {",", "\\{", "\\}"};
        input = singleIsolate(input, shouldNotBeIsolatedElements, NON_ISOLATION_FORMAT);
        return input;
    }

    private String isolate(String input) {
        String[] specialElements = {"\\(", "\\)", "\\+", "\\*", "&&", "\\|\\|", "!", "==", "<=", ">="};
        String[] singleSpecialElements = {"=", "<", ">"};

        input = singleIsolate(input, specialElements, ISOLATION_FORMAT);

        if (!input.contains("==") && !input.contains("<=") && !input.contains(">=")) {
            input = singleIsolate(input, singleSpecialElements, ISOLATION_FORMAT);
        }

        return input;
    }

    private String singleIsolate(String input, String[] specialElements, String format) {
        for (String element : specialElements) {
            input = input.replaceAll("[\\s]*" + element + "[\\s]*", String.format(format, element));
        }

        return input;
    }
}
