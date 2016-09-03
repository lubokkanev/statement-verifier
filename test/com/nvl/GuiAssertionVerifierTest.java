package com.nvl;

import com.nvl.parser.statement.StatementProcessor;
import com.nvl.parser.statement.StatementProcessorImpl;
import com.nvl.parser.value.VariableTypeParser;
import com.nvl.parser.value.VariableTypeParserImpl;
import com.nvl.parser.variable_definition.VariableDefinitionParser;
import com.nvl.parser.variable_definition.VariableDefinitionParserImpl;
import com.nvl.responder.Responder;
import com.nvl.responder.ResponderImpl;
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

public class GuiAssertionVerifierTest {
    // TODO: Vicky
    private Responder responder;
    private VariableManager variableManager;
    private RequestProcessor requestProcessor;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());

        InputTypeDeterminer typeDeterminer = new SimpleInputTypeDeterminer(variableManager);
        StatementProcessor statementProcessor = new StatementProcessorImpl(variableManager);
        VariableTypeParser variableTypeParser = new VariableTypeParserImpl();
        VariableDefinitionParser variableDefinitionParser = new VariableDefinitionParserImpl();
        requestProcessor = new RequestProcessorImpl(statementProcessor, variableTypeParser, variableDefinitionParser, variableManager);
        InputValidator inputValidator = new GrammarInputValidator(variableManager);

        responder = new ResponderImpl(typeDeterminer, requestProcessor, inputValidator);
    }

    @Test(expected = RuntimeException.class)
    public void testChangeType() {
        responder.process("a = 5");
        responder.process("a = 'asdf'");
    }
}
