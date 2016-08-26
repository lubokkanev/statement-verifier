package com.nvl;

import com.nvl.parser.statement.StatementProcessor;
import com.nvl.parser.statement.StatementProcessorImpl;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.responder.Responder;
import com.nvl.responder.ResponderImpl;
import com.nvl.responder.ResponderStub;
import com.nvl.ui.GraphicalUserInterface;
import com.nvl.ui.GraphicalUserInterfaceImpl;
import com.nvl.variable.manager.VariableManager;
import com.nvl.variable.manager.VariableManagerImpl;
import com.nvl.verifier.InputProcessor;
import com.nvl.verifier.InputProcessorImpl;
import com.nvl.verifier.determinator.InputTypeDeterminator;
import com.nvl.verifier.determinator.InputTypeDeterminatorImpl;

public class GuiAssertionVerifier {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiAssertionVerifier() {
        VariableTypeParser valueParser = new VariableTypeParserImpl();
        VariableManager variableManager = new VariableManagerImpl();
        StatementProcessor statementProcessor = new StatementProcessorImpl(variableManager);
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        InputProcessor inputProcessor = new InputProcessorImpl(statementProcessor, valueParser, variableDefinitionParser, variableManager);

        InputTypeDeterminator determinator = new InputTypeDeterminatorImpl();
        Responder responder = new ResponderImpl(determinator, inputProcessor);
        responder = new ResponderStub();

        graphicalUserInterface = new GraphicalUserInterfaceImpl(responder);
    }

    public void start() {
        graphicalUserInterface.start();
    }

    public static void main(String[] args) {
        GuiAssertionVerifier guiAssertionVerifier = new GuiAssertionVerifier();
        guiAssertionVerifier.start();
    }
}
