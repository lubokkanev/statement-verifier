package com.nvl.parser.statement;

import com.nvl.variable.manager.MapVariableManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class StatementVerifierTest_string {
    private StatementVerifier statementVerifier;

    @Before
    public void setUp() {
        MapVariableManager variableManager = new MapVariableManager(new HashMap<>());
        statementVerifier = new StatementVerifierImpl(variableManager);
    }

    @Test
    public void testVerifyStatement() {
        assertTrue(statementVerifier.verifyStatement("5 == 5"));
    }
}
