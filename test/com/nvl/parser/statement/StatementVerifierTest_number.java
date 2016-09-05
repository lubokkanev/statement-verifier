package com.nvl.parser.statement;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_number {
    private StatementVerifier statementVerifier;
    private MapVariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new StatementVerifierImpl(variableManager);
    }

    @Test
    public void testVerifyStatement_simple() {
        assertTrue(statementVerifier.verifyStatement("5 == 5"));
    }

    @Test
    public void testVerifyStatement_multiDigit() {
        assertFalse(statementVerifier.verifyStatement("50 == 64"));
    }

    @Test
    public void testVerifyStatement_addition() {
        assertTrue(statementVerifier.verifyStatement("50 + 14 == 64 + 0"));
    }

    @Test
    public void testVerifyStatement_multiplication() {
        assertTrue(statementVerifier.verifyStatement("32 * 2 == 64"));
    }

    @Test
    public void testVerifyStatement_simpleVariables() {
        variableManager.addVariable(new EvaluatedVariable("a", "5", VariableType.NUMBER));

        assertTrue(statementVerifier.verifyStatement("a == 5"));
    }

    @Test
    public void testVerifyStatement_brackets() {
        variableManager.addVariable(new EvaluatedVariable("a", "6", VariableType.NUMBER));
        variableManager.addVariable(new EvaluatedVariable("b", "7", VariableType.NUMBER));

        assertTrue(statementVerifier.verifyStatement("( a + ( b + 3 ) ) <= 16"));
    }

    @Test
    public void testVerifyStatement_biggerThan() {
        variableManager.addVariable(new EvaluatedVariable("a", "6", VariableType.NUMBER));
        variableManager.addVariable(new EvaluatedVariable("b", "7", VariableType.NUMBER));

        assertTrue(statementVerifier.verifyStatement("( b + 5 ) * a > 11 * a"));
    }

    @Test
    public void testVerifyStatement_complex() {
        variableManager.addVariable(new EvaluatedVariable("a", "6", VariableType.NUMBER));
        variableManager.addVariable(new EvaluatedVariable("b", "7", VariableType.NUMBER));

        assertFalse(statementVerifier.verifyStatement("( ( a * 6 ) + 5 ) * 4 <= 2 * ( b + 1 )"));
    }

    @Test
    public void testVerifyStatement_unequal() {
        assertFalse(statementVerifier.verifyStatement("5 + 6 != 11"));
    }
}
