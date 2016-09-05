package com.nvl.parser.statement;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_boolean {
    private StatementVerifier statementVerifier;
    private MapVariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new StatementVerifierImpl(variableManager);
    }

    @Test
    public void testVerifyStatement_simple() {
        assertTrue(statementVerifier.verifyStatement("false != true"));
    }

    @Test
    public void testVerifyStatement_conjunction() {
        assertTrue(statementVerifier.verifyStatement("true && ! true == false"));
    }

    @Test
    public void testVerifyStatement_disjunction() {
        assertFalse(statementVerifier.verifyStatement("( true || false ) == false"));
    }

    @Test
    public void testVerifyStatement_negation() {
        assertTrue(statementVerifier.verifyStatement("! true == false"));
    }

    @Test
    public void testVerifyStatement_brackets() {
        assertTrue(statementVerifier.verifyStatement("true && ( false || true ) == true"));
    }

    @Test
    public void testVerifyStatement_bracketsComplex() {
        assertFalse(statementVerifier.verifyStatement("false || ( ( false && true || false) && true ) == ! true || (true && true)"));
    }

    @Test
    public void testVerifyStatement_complexWithVariables() {
        variableManager.addVariable(new EvaluatedVariable("a", "true", VariableType.BOOLEAN));
        assertTrue(statementVerifier.verifyStatement("true && ( ! true || true ) == a && a"));
    }
}
