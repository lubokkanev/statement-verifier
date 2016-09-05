package com.nvl.responder;

import com.nvl.constants.Constants;
import com.nvl.parser.statement.StatementVerifier;
import com.nvl.parser.statement.StatementVerifierImpl;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.variable.manager.MapVariableManager;
import com.nvl.variable.manager.VariableManager;
import com.nvl.verifier.determiner.InputTypeDeterminer;
import com.nvl.verifier.determiner.SimpleInputTypeDeterminer;
import com.nvl.verifier.processor.RequestProcessor;
import com.nvl.verifier.processor.RequestProcessorImpl;
import com.nvl.verifier.validator.GrammarInputValidator;
import com.nvl.verifier.validator.InputValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ResponderTest {
    private Responder responder;
    private VariableManager variableManager;
    private RequestProcessor requestProcessor;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());

        InputTypeDeterminer typeDeterminer = new SimpleInputTypeDeterminer(variableManager);
        StatementVerifier statementVerifier = new StatementVerifierImpl(variableManager);
        VariableTypeParser variableTypeParser = new VariableTypeParserImpl();
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        requestProcessor = new RequestProcessorImpl(statementVerifier, variableTypeParser, variableDefinitionParser, variableManager);
        InputValidator inputValidator = new GrammarInputValidator(variableManager);

        responder = new ResponderImpl(typeDeterminer, requestProcessor, inputValidator);
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_changeType() {
        responder.process("a = 5");
        responder.process("a = 'asdf'");
    }

    @Test
    public void testProcess_string() {
        String statement = "'asdf' <= 'asdf'";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingNumbers() {
        String statement = "   3+   5  ==8   ";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingStrings() {
        String statement = "   'asdf'+   'asdf'  <=    'asdf'   ";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingArrays() {
        String statement = "{1, 2,3}+{10}=={11,2,3}";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "TRUE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingBooleans() {
        String statement = "true&&!  true==    !false";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }

    @Test
    public void testProcess_spacingInStrings() {
        String statement = "'a b c' == 'abc'";
        assertEquals(String.format(Constants.STATEMENT_FORMAT, statement, "FALSE"), responder.process(statement));
    }
}
