package com.nvl.verifier.processor;

import com.nvl.parser.statement.StatementProcessor;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.UnevaluatedVariable;
import com.nvl.variable.manager.VariableManager;

import java.util.Set;

public class RequestProcessorImpl implements RequestProcessor {
    private StatementProcessor statementProcessor;
    private VariableDefinitionParser variableDefinitionParser;
    private VariableManager variableManager;
    private VariableTypeParser variableTypeParser;

    public RequestProcessorImpl(StatementProcessor statementProcessor, VariableTypeParser variableTypeParser, VariableDefinitionParser variableDefinitionParser,
                                VariableManager variableManager) {
        this.statementProcessor = statementProcessor;
        this.variableTypeParser = variableTypeParser;
        this.variableDefinitionParser = variableDefinitionParser;
        this.variableManager = variableManager;
    }

    public void addVariable(String variableDefinition) {
        // TODO: support complex variable definitions
        UnevaluatedVariable unevaluatedVariable = variableDefinitionParser.parse(variableDefinition);
        EvaluatedVariable evaluatedVariable = variableTypeParser.parse(unevaluatedVariable);
        variableManager.addVariable(evaluatedVariable);
    }

    public void updateVariable(String variableDefinition) {
        UnevaluatedVariable unevaluatedVariable = variableDefinitionParser.parse(variableDefinition);
        EvaluatedVariable evaluatedVariable = variableTypeParser.parse(unevaluatedVariable);
        variableManager.updateVariable(evaluatedVariable);
    }

    public boolean verifyStatement(String statement) {
        return statementProcessor.verifyStatement(statement);
    }

    @Override
    public Set<EvaluatedVariable> variables() {
        return variableManager.variables();
    }
}
