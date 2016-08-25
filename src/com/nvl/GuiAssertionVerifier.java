package com.nvl;

import com.nvl.parser.statement.StatementParserImpl;
import com.nvl.parser.value.ValueParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.ui.GraphicalUserInterface;
import com.nvl.ui.GraphicalUserInterfaceImpl;
import com.nvl.ui.GraphicalUserInterfaceImplOld;
import com.nvl.ui.VerifierStub;
import com.nvl.variable.manager.VariableManagerImpl;
import com.nvl.verificator.AssertionVerifier;
import com.nvl.verificator.AssertionVerifierImpl;

public class GuiAssertionVerifier {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiAssertionVerifier() {
        ValueParserImpl valueParser = new ValueParserImpl();
        VariableManagerImpl variableManager = new VariableManagerImpl(valueParser);
        StatementParserImpl statementParser = new StatementParserImpl(variableManager);
        VariableDefinitionParserImpl variableDefinitionParser = new VariableDefinitionParserImpl();
        AssertionVerifier assertionVerifier = new AssertionVerifierImpl(statementParser, valueParser, variableDefinitionParser, variableManager);

        graphicalUserInterface = new GraphicalUserInterfaceImpl(new VerifierStub());
    }

    public void run() {
        graphicalUserInterface.start();
    }

    public static void main(String[] args) {
        GuiAssertionVerifier guiAssertionVerificator = new GuiAssertionVerifier();
        guiAssertionVerificator.main(args);
    }
}
