package org.nvl;

import org.nvl.core.input.type.InputTypeDeterminer;
import org.nvl.core.input.type.SimpleInputTypeDeterminer;
import org.nvl.core.input.validator.GrammarInputValidator;
import org.nvl.core.input.validator.InputValidator;
import org.nvl.core.responder.Responder;
import org.nvl.core.responder.ResponderImpl;
import org.nvl.core.responder.processor.RequestProcessor;
import org.nvl.core.responder.processor.RequestProcessorImpl;
import org.nvl.core.statement.RpnStatementVerifier;
import org.nvl.core.statement.StatementVerifier;
import org.nvl.core.variable.definition.VariableDefinitionParser;
import org.nvl.core.variable.definition.VariableDefinitionParserImpl;
import org.nvl.core.variable.manager.MapVariableManager;
import org.nvl.core.variable.manager.VariableManager;
import org.nvl.core.variable.type.VariableTypeParser;
import org.nvl.core.variable.type.VariableTypeParserImpl;
import org.nvl.ui.GraphicalUserInterface;
import org.nvl.ui.SwingGraphicalUserInterface;

import java.util.HashMap;

/**
 * Constructs the StatementVerifier and runs it on a GUI
 */
public class GuiStatementVerifier {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiStatementVerifier() {
        VariableManager variableManager = new MapVariableManager(new HashMap<>());
        VariableTypeParser typeParser = new VariableTypeParserImpl();
        StatementVerifier statementVerifier = new RpnStatementVerifier(variableManager);
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        RequestProcessor requestProcessor = new RequestProcessorImpl(statementVerifier, typeParser, variableDefinitionParser, variableManager);
        InputValidator inputValidator = new GrammarInputValidator(variableManager);

        InputTypeDeterminer typeDeterminer = new SimpleInputTypeDeterminer(variableManager);
        Responder responder = new ResponderImpl(typeDeterminer, requestProcessor, inputValidator, variableManager);

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
