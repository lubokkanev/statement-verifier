package org.nvl.core.statement;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nvl.core.variable.EvaluatedVariable;
import org.nvl.core.variable.VariableType;
import org.nvl.core.variable.manager.MapVariableManager;

import java.util.HashMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_string {
    private StatementVerifier statementVerifier;
    private MapVariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new RpnStatementVerifier(variableManager);
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
        variableManager.addVariable(new EvaluatedVariable("s", "'asd'", VariableType.STRING));
        assertFalse(statementVerifier.verifyStatement("'str' + ( s + 'a' ) > 'z'"));
    }

    @Test
    public void testVerifyStatement_unequalWithVariables() {
        variableManager.addVariable(new EvaluatedVariable("s", "'asd'", VariableType.STRING));
        assertFalse(statementVerifier.verifyStatement("s + 'a' != 'asda'"));
    }

    @Test
    public void testVerifyStatement_shorter() {
        variableManager.addVariable(new EvaluatedVariable("s", "'asd'", VariableType.STRING));
        assertTrue(statementVerifier.verifyStatement("'a' < s"));
    }

    @Test
    public void testVerifyStatement_capitalLetters() {
        variableManager.addVariable(new EvaluatedVariable("s", "'aSd'", VariableType.STRING));
        assertTrue(statementVerifier.verifyStatement("s + 'a' == 'aSd' + 'a'"));
    }

    @Ignore("Test ignored: Not implemented nor needed for now. ")
    @Test
    public void testVerityStatement_spacesMatter() {
        assertFalse(statementVerifier.verifyStatement("'one two' == 'o net wo'"));
    }

    @Test
    public void testVerifyStatement_spaces() {
        variableManager.addVariable(new EvaluatedVariable("s", "'once upon'", VariableType.STRING));
        assertTrue(statementVerifier.verifyStatement("s + 'a time' == 'once upon a time'"));
    }
}
