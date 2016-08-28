package com.nvl;

import com.nvl.parser.statement.StatementProcessor;
import com.nvl.parser.statement.StatementProcessorImpl;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.responder.Responder;
import com.nvl.responder.ResponderImpl;
import com.nvl.ui.GraphicalUserInterface;
import com.nvl.ui.SwingGraphicalUserInterface;
import com.nvl.variable.manager.MapVariableManager;
import com.nvl.variable.manager.VariableManager;
import com.nvl.verifier.determiner.InputTypeDeterminer;
import com.nvl.verifier.determiner.SimpleInputTypeDeterminer;
import com.nvl.verifier.processor.RequestProcessor;
import com.nvl.verifier.processor.RequestProcessorImpl;
import com.nvl.verifier.validator.GrammarInputValidator;
import com.nvl.verifier.validator.InputValidator;

import java.util.HashMap;

/**
 * Constructs the StatementVerifier and runs it on a GUI
 */
public class GuiStatementVerifier {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiStatementVerifier() {
        VariableManager variableManager = new MapVariableManager(new HashMap<>());
        VariableTypeParser typeParser = new VariableTypeParserImpl();
        StatementProcessor statementProcessor = new StatementProcessorImpl(variableManager);
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        RequestProcessor requestProcessor = new RequestProcessorImpl(statementProcessor, typeParser, variableDefinitionParser, variableManager);
        InputValidator inputValidator = new GrammarInputValidator(variableManager);

        InputTypeDeterminer typeDeterminer = new SimpleInputTypeDeterminer(variableManager);
        Responder responder = new ResponderImpl(typeDeterminer, requestProcessor, inputValidator);

        graphicalUserInterface = new SwingGraphicalUserInterface(responder);
    }

    public void start() {
        graphicalUserInterface.start();
    }

    public static void main(String[] args) {
        GuiStatementVerifier guiStatementVerifier = new GuiStatementVerifier();
        guiStatementVerifier.start();
    }
}
