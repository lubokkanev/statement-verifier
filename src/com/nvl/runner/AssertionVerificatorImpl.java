package com.nvl.runner;

import com.nvl.parser.statement.StatementParser;
import com.nvl.parser.value.ValueParser;
import com.nvl.parser.variable_definition.UnevaluatedResult;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.variable.manager.VariableManager;

import java.util.Scanner;

public class AssertionVerificatorImpl implements AssertionVerificator {
    private StatementParser statementParser;
    private VariableDefinitionParser variableDefinitionParser;
    private Scanner scanner;
    private VariableManager variableManager;

    public AssertionVerificatorImpl(StatementParser statementParser, ValueParser valueParser, VariableDefinitionParser variableDefinitionParser, Scanner scanner,
                                    VariableManager variableManager) {
        this.statementParser = statementParser;
        this.variableDefinitionParser = variableDefinitionParser;
        this.scanner = scanner;
        this.variableManager = variableManager;
    }

    public void addVariable() {
        String variableDefinition = scanner.nextLine();
        UnevaluatedResult result = variableDefinitionParser.parse(variableDefinition);
        variableManager.addVariable(result.getName(), result.getValue());
    }

    public void updateVariable() {
        String variableDefinition = scanner.nextLine();
        UnevaluatedResult result = variableDefinitionParser.parse(variableDefinition);
        variableManager.updateVariable(result.getName(), result.getValue());
    }

    public boolean evaluateStatement() {
        return statementParser.evaluate(scanner.nextLine());
    }
}
