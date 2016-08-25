package com.nvl.verificator;

import com.nvl.parser.statement.StatementParser;
import com.nvl.parser.value.ValueParser;
import com.nvl.parser.variable_definition.UnevaluatedResult;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.variable.Variable;
import com.nvl.variable.manager.VariableManager;

import java.util.Set;

public class AssertionVerifierImpl implements AssertionVerifier {
    private StatementParser statementParser;
    private VariableDefinitionParser variableDefinitionParser;
    private VariableManager variableManager;

    public AssertionVerifierImpl(StatementParser statementParser, ValueParser valueParser, VariableDefinitionParser variableDefinitionParser, VariableManager variableManager) {
        this.statementParser = statementParser;
        this.variableDefinitionParser = variableDefinitionParser;
        this.variableManager = variableManager;
    }

    public void addVariable(String variableDefinition) {
        UnevaluatedResult result = variableDefinitionParser.parse(variableDefinition);
        variableManager.addVariable(result.getName(), result.getValue());
    }

    public void updateVariable(String variableDefinition) {
        UnevaluatedResult result = variableDefinitionParser.parse(variableDefinition);
        // TODO: Lubo - get the valueParser out of the variable Manager
        variableManager.updateVariable(result.getName(), result.getValue());
    }

    public boolean evaluateStatement(String statement) {
        return statementParser.evaluateStatement(statement);
    }

    @Override
    public Set<Variable> variables() {
        return variableManager.variables();
    }
}
