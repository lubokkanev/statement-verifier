package com.nvl;

import com.nvl.parser.statement.StatementParser;
import com.nvl.parser.statement.StatementParserImpl;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.ui.GraphicalUserInterface;
import com.nvl.ui.GraphicalUserInterfaceImpl;
import com.nvl.variable.manager.VariableManager;
import com.nvl.variable.manager.VariableManagerImpl;
import com.nvl.verifier.VariableManagerAndStatementParser;
import com.nvl.verifier.VariableManagerAndStatementParserImpl;
import com.nvl.verifier.Verifier;
import com.nvl.verifier.VerifierImpl;
import com.nvl.verifier.VerifierStub;
import com.nvl.verifier.determinator.Determinator;
import com.nvl.verifier.determinator.DeterminatorImpl;

public class GuiAssertionVerifier {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiAssertionVerifier() {
        VariableTypeParser valueParser = new VariableTypeParserImpl();
        VariableManager variableManager = new VariableManagerImpl();
        StatementParser statementParser = new StatementParserImpl(variableManager);
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        VariableManagerAndStatementParser assertionVerifier = new VariableManagerAndStatementParserImpl(statementParser, valueParser, variableDefinitionParser, variableManager);

        Determinator determinator = new DeterminatorImpl();
        Verifier verifier = new VerifierImpl(determinator, assertionVerifier);
        verifier = new VerifierStub();

        graphicalUserInterface = new GraphicalUserInterfaceImpl(verifier);
    }

    public void start() {
        graphicalUserInterface.start();
    }

    public static void main(String[] args) {
        GuiAssertionVerifier guiAssertionVerificator = new GuiAssertionVerifier();
        guiAssertionVerificator.start();
    }
}
