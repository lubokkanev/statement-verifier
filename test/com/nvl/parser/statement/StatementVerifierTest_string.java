package com.nvl.parser.statement;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_string {
    private StatementVerifier statementVerifier;
    private MapVariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new StatementVerifierImpl(variableManager);
    }

    @Test
    public void testVerifyStatement_simple() {
        assertTrue(statementVerifier.verifyStatement("'str' == 'str'"));
    }

    @Test
    public void testVerifyStatement_less() {
        assertTrue(statementVerifier.verifyStatement("'str' < 'stx'"));
    }

    @Test
    public void testVerifyStatement_addition() {
        assertTrue(statementVerifier.verifyStatement("'str' + 'x' == 'strx'"));
    }

    @Test
    public void testVerifyStatement_complex() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'asd'", "s"));
        assertFalse(statementVerifier.verifyStatement("'str' + ( s + 'a' ) > 'z'"));
    }

    @Test
    public void testVerifyStatement_unequalWithVariables() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'asd'", "s"));
        assertFalse(statementVerifier.verifyStatement("s + 'a' != 'asda'"));
    }

    @Test
    public void testVerifyStatement_shorter() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'asd'", "s"));
        assertTrue(statementVerifier.verifyStatement("'a' < s"));
    }

    @Test
    public void testVerifyStatement_capitalLetters() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'aSd'", "s"));
        assertTrue(statementVerifier.verifyStatement("s + 'a' == 'aSd' + 'a'"));
    }

    @Ignore("Test ignored: Not implemented nor needed for now. ")
    @Test
    public void testVerityStatement_spacesMatter() {
        assertFalse(statementVerifier.verifyStatement("'one two' == 'o net wo'"));
    }

    @Test
    public void testVerifyStatement_spaces() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'once upon'", "s"));
        assertTrue(statementVerifier.verifyStatement("s + 'a time' == 'once upon a time'"));
    }
}
