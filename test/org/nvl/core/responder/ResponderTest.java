package org.nvl.core.responder;

import org.junit.Before;
import org.junit.Test;
import org.nvl.MessageConstants;
import org.nvl.core.input.type.InputTypeDeterminer;
import org.nvl.core.input.type.SimpleInputTypeDeterminer;
import org.nvl.core.input.validator.GrammarInputValidator;
import org.nvl.core.input.validator.InputValidator;
import org.nvl.core.responder.processor.RequestProcessor;
import org.nvl.core.responder.processor.RequestProcessorImpl;
import org.nvl.core.statement.RpnStatementVerifier;
import org.nvl.core.statement.StatementVerifier;
import org.nvl.core.variable.EvaluatedVariable;
import org.nvl.core.variable.VariableType;
import org.nvl.core.variable.definition.VariableDefinitionParser;
import org.nvl.core.variable.definition.VariableDefinitionParserImpl;
import org.nvl.core.variable.manager.ListVariableManager;
import org.nvl.core.variable.manager.VariableManager;
import org.nvl.core.variable.type.VariableTypeParser;
import org.nvl.core.variable.type.VariableTypeParserImpl;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ResponderTest {
    private Responder responder;
    private VariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new ListVariableManager(new ArrayList<>());//new MapVariableManager(new HashMap<>());

        InputTypeDeterminer typeDeterminer = new SimpleInputTypeDeterminer(variableManager);
        StatementVerifier statementVerifier = new RpnStatementVerifier(variableManager);
        VariableTypeParser variableTypeParser = new VariableTypeParserImpl();
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        RequestProcessor requestProcessor = new RequestProcessorImpl(statementVerifier, variableTypeParser, variableDefinitionParser, variableManager);
        InputValidator inputValidator = new GrammarInputValidator(variableManager);

        responder = new ResponderImpl(typeDeterminer, requestProcessor, inputValidator, variableManager);
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_changeType() {
        responder.process("a = 5");
        responder.process("a = 'asdf'");
    }

    @Test
    public void testProcess_string() {
        String statement = "'asdf' <= 'asdf'";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingNumbers() {
        String statement = "   3+   5  ==8   ";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingStrings() {
        String statement = "   'asdf'+   'asdf'  <=    'asdf'   ";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingArrays() {
        String statement = "{1, 2,3}+{10}=={11,2,3}";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingBooleans() {
        String statement = "true&&!  true==    !false";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingInStrings() {
        String statement = "'a b c' == 'abc'";
        assertEquals(String.format(MessageConstants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }

    @Test
    public void testProcess_updateVariable() {
        variableManager.addVariable(new EvaluatedVariable("a", "5", VariableType.NUMBER));
        String statement = "a = 6";

        assertEquals(MessageConstants.EXISTING_VARIABLE_MESSAGE, responder.process(statement));
    }

    @Test
    public void testProcess_addVariable() {
        assertEquals(MessageConstants.NEW_VARIABLE_MESSAGE, responder.process("b=   {  1   ,   3,4}"));
    }

    @Test
    public void testProcess_addVariableStringWithSpaces() {
        String value = "'a b c'";
        String input = "a = " + value;

        assertEquals(MessageConstants.NEW_VARIABLE_MESSAGE, responder.process(input));
        assertEquals(value, variableManager.getVariable("a").getValue());
    }
}
