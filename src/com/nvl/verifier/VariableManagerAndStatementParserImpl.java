package com.nvl.verifier;

import com.nvl.parser.statement.StatementParser;
import com.nvl.parser.value.EvaluatedResult;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.variable_definition.UnevaluatedResult;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.variable.Variable;
import com.nvl.variable.manager.VariableManager;

import java.util.Set;

public class VariableManagerAndStatementParserImpl implements VariableManagerAndStatementParser {
	// TODO: Lubo - maybe create it here, using the VM, and somehow get from outside the exact type of the SP
    private StatementParser statementParser; 
    private VariableDefinitionParser variableDefinitionParser;
    private VariableManager variableManager;
    private VariableTypeParser variableTypeParser;

    public VariableManagerAndStatementParserImpl(StatementParser statementParser, VariableTypeParser variableTypeParser, VariableDefinitionParser variableDefinitionParser, VariableManager variableManager) {
        this.statementParser = statementParser;
        this.variableDefinitionParser = variableDefinitionParser;
        this.variableManager = variableManager;
    }

    public void addVariable(String variableDefinition) {
        UnevaluatedResult unevaluatedResult = variableDefinitionParser.parse(variableDefinition);
        EvaluatedResult evaluatedResult = variableTypeParser.parse(variableDefinition);
        Variable newVariable = new Variable(unevaluatedResult.getName(), evaluatedResult.getType(), unevaluatedResult.getValue());
        variableManager.addVariable(newVariable);
    }

    public void updateVariable(String variableDefinition) {
        UnevaluatedResult unevaluatedResult = variableDefinitionParser.parse(variableDefinition);
        EvaluatedResult evaluatedResult = variableTypeParser.parse(variableDefinition);
        Variable newVariable = new Variable(unevaluatedResult.getName(), evaluatedResult.getType(), unevaluatedResult.getValue());
        variableManager.updateVariable(newVariable);
    }

    public boolean evaluateStatement(String statement) {
        return statementParser.evaluateStatement(statement);
    }

    @Override
    public Set<Variable> variables() {
        return variableManager.variables();
    }
}
