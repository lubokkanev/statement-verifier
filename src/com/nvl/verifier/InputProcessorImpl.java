package com.nvl.verifier;

import com.nvl.parser.statement.StatementProcessor;
import com.nvl.parser.value.EvaluatedResult;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.variable_definition.UnevaluatedResult;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.variable.Variable;
import com.nvl.variable.manager.VariableManager;

import java.util.Set;

public class InputProcessorImpl implements InputProcessor {
	// TODO: Lubo - maybe create the StatementParser here, using the VM, and somehow get from outside the exact type of the SP
    private StatementProcessor statementProcessor; 
    private VariableDefinitionParser variableDefinitionParser;
    private VariableManager variableManager;
    private VariableTypeParser variableTypeParser;

    public InputProcessorImpl(StatementProcessor statementProcessor, VariableTypeParser variableTypeParser, VariableDefinitionParser variableDefinitionParser, VariableManager variableManager) {
        this.statementProcessor = statementProcessor;
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

    public boolean verifyStatement(String statement) {
        return statementProcessor.verifyStatement(statement);
    }

    @Override
    public Set<Variable> variables() {
        return variableManager.variables();
    }
}
