package com.nvl.parser.statement;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_array {
    private StatementVerifier statementVerifier;
    private MapVariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new StatementVerifierImpl(variableManager);
    }

    @Test
    public void testVerifyStatement_simple() {
        assertTrue(statementVerifier.verifyStatement("{1,2} == {1,2}"));
    }

    @Test
    public void testVerifyStatement_lessManyDigits() {
        assertTrue(statementVerifier.verifyStatement("{1} < {244312}"));
    }

    @Test
    public void testVerifyStatement_greaterDifferentLengths() {
        assertTrue(statementVerifier.verifyStatement("{1,2} < {2}"));
    }

    @Test
    public void testVerifyStatement_additionWithNumber() {
        assertTrue(statementVerifier.verifyStatement("{1,2} + 10 == {11,12}"));
    }

    @Test
    public void testVerifyStatement_additionWithArray() {
        assertTrue(statementVerifier.verifyStatement("{1,2} + {10,10} == {11,12}"));
    }

    @Test
    public void testVerifyStatement_additionWithArrayDifferentLength() {
        assertFalse(statementVerifier.verifyStatement("{1,2} + {10} == {11,12}"));
    }

    @Test
    public void testVerifyStatement_multiplicationWithNumber() {
        assertTrue(statementVerifier.verifyStatement("{1,2} * 10 == {10,20}"));
    }

    @Test
    public void testVerifyStatement_multiplicationWithArray() {
        assertTrue(statementVerifier.verifyStatement("{3,2} * {10,1} == {30,2}"));
    }

    @Test
    public void testVerifyStatement_multiplicationWithArrayDifferentSizes() {
        assertTrue(statementVerifier.verifyStatement("{3,2} * {10,1,2} == {30,2,2}"));
    }

    @Test
    public void testVerifyStatement_complexWithVariables() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.ARRAY, "{1,2,3}", "s"));
        assertFalse(statementVerifier.verifyStatement("( {5,5,5} + s ) * s < {1,1,1}"));
    }

    @Test
    public void testVerifyStatement_shorter() {
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "{1,1,1}", "s"));
        assertTrue(statementVerifier.verifyStatement("s < {2,3}"));
    }
}
