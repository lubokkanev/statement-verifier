package com.nvl.verifier.validator;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import com.nvl.variable.manager.VariableManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GrammarInputValidatorTest {
    private GrammarInputValidator grammarInputValidator;
    private VariableManager variableManager;

    @Before
    public void setUp() {
        variableManager = new MapVariableManager(new HashMap<>());
        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "5", "a"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "7", "b"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "8", "c"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'abra'", "str"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'cadabra'", "str2"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "TRUE", "bool"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "FALSE", "bool2"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "TRUE", "bool3"));

        grammarInputValidator = new GrammarInputValidator(variableManager);
    }

    @Test
    public void testDefinition() {
        assertTrue(grammarInputValidator.isValid("k = 10"));
    }

    @Test
    public void testDefinition2() {
        assertTrue(grammarInputValidator.isValid("s = 'this'"));
    }

    @Test
    public void testDefinition3() {
        assertTrue(grammarInputValidator.isValid("bool = FALSE"));
    }

    @Test
    public void testDefinition5() {
        assertTrue(grammarInputValidator.isValid("bool == ! FALSE"));
    }

    @Test
    public void testString() {
        assertTrue(grammarInputValidator.isValid("'this' < 'that'"));
    }

    //!
    @Test
    public void testWrongDefinition() {
        assertFalse(grammarInputValidator.isValid("s = 'this is it'"));
    }

    @Test
    public void testDefinition4() {
        assertTrue(grammarInputValidator.isValid("bool = false"));
    }

    @Test
    public void testIsEqual() {
        assertTrue(grammarInputValidator.isValid("a * 2 == 10"));
    }

    @Test
    public void testIsSmaller() {
        assertTrue(grammarInputValidator.isValid("a * 2 + 5 < 10 + b"));
    }

    @Test
    public void testIsLarger() {
        assertTrue(grammarInputValidator.isValid("a * 2 + c > 10 * a"));
    }

    @Test
    public void testStringBracketsFalse() {
        assertFalse(grammarInputValidator.isValid("str * ( str2 + 5 ) < 10 * ( str + 1 )"));
    }

    @Test
    public void testStringBracketsFalse2() {
        assertFalse(grammarInputValidator.isValid("str * ( str2 + 'abba' ) < 'lqlq' * ( str + 'anna' )"));
    }

    @Test
    public void testStringBracketsTrue() {
        assertTrue(grammarInputValidator.isValid("str * ( a + 5 ) < 'lqlq' * ( 3 + b )"));
    }

    @Test
    public void testIsInvalid4() {
        assertFalse(grammarInputValidator.isValid("str * 4 + 15 ( < 30 + str2 "));
    }

    @Test
    public void testDifferentTypes2() {
        assertFalse(grammarInputValidator.isValid("bool * 55 > 9 + str"));
    }

    @Test
    public void testIsInvalid5() {
        assertFalse(grammarInputValidator.isValid("( ( ) )"));
    }

    @Test
    public void testIsInvalid6() {
        assertFalse(grammarInputValidator.isValid("bool + 4 * 3 < 6 ( + bool2 )"));
    }

    @Test
    public void testInvalidString() {
        assertFalse(grammarInputValidator.isValid("str + 3 * str2 == str3 * ( 8 + a )"));
    }

    @Test
    public void testBoolean() {
        assertTrue(grammarInputValidator.isValid("bool && bool2 || bool3"));
    }

    @Test
    public void testIsInvalid7() {
        assertFalse(grammarInputValidator.isValid("bool || bool1 & "));
    }

    @Test
    public void testInvalidBoolean() {
        assertFalse(grammarInputValidator.isValid("bool && bool4"));
    }

    @Test
    public void testBrackets() {
        assertTrue(grammarInputValidator.isValid("a * ( b + 5 ) < 10 * ( c + 1 )"));
    }

    @Test
    public void testIsInvalid() {
        assertFalse(grammarInputValidator.isValid("c * 4 + 15 < 30 + b )"));
    }

    @Test
    public void testDifferentTypes() {
        assertFalse(grammarInputValidator.isValid("a * 55 > 9 + str"));
    }

    @Test
    public void testStringPlusNumber() {
        assertFalse(grammarInputValidator.isValid("a + str < 0"));
    }

    @Ignore
    @Test
    public void testStringTimesNumber() {
        assertFalse(grammarInputValidator.isValid("a * str < 0"));
    }

    @Test
    public void testIsInvalid2() {
        assertFalse(grammarInputValidator.isValid("( ( == 10 + str )"));
    }

    @Test
    public void testIsInvalid3() {
        assertFalse(grammarInputValidator.isValid("str + 4 * 3 < 6 ( + str2 )"));
    }

    @Test
    public void testInvalidInteger() {
        assertFalse(grammarInputValidator.isValid("b + 3 * c == k * ( 8 + a )"));
    }

    @Test
    public void testInvalidBracketsUneven() {
        assertFalse(grammarInputValidator.isValid("((3 + 4) == 7"));
    }

    @Test
    public void testInvalidBracketsBadOrder() {
        assertFalse((grammarInputValidator.isValid(")3( == 3")));
    }
}
