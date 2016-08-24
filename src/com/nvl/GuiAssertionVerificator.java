package com.nvl;

import com.nvl.parser.statement.StatementParserImpl;
import com.nvl.parser.value.ValueParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.ui.GraphicalUserInterface;
import com.nvl.ui.GraphicalUserInterfaceImpl;
import com.nvl.variable.manager.VariableManagerImpl;
import com.nvl.verificator.AssertionVerificator;
import com.nvl.verificator.AssertionVerificatorImpl;

public class GuiAssertionVerificator {
    private GraphicalUserInterface graphicalUserInterface;

    public GuiAssertionVerificator() {
        ValueParserImpl valueParser = new ValueParserImpl();
        VariableManagerImpl variableManager = new VariableManagerImpl(valueParser);
        StatementParserImpl statementParser = new StatementParserImpl(variableManager);
        VariableDefinitionParserImpl variableDefinitionParser = new VariableDefinitionParserImpl();
        AssertionVerificator assertionVerificator = new AssertionVerificatorImpl(statementParser, valueParser, variableDefinitionParser, variableManager);

        graphicalUserInterface = new GraphicalUserInterfaceImpl(assertionVerificator);
    }

    public void run() {
        graphicalUserInterface.start();
    }

    public static void main(String[] args) {
        GuiAssertionVerificator guiAssertionVerificator = new GuiAssertionVerificator();
        guiAssertionVerificator.run();
    }
}
