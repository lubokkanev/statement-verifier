package com.nvl.verifier.validator;

import com.nvl.variable.EvaluatedVariable;
import com.nvl.variable.VariableType;
import com.nvl.variable.manager.MapVariableManager;
import com.nvl.variable.manager.VariableManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GrammarInputValidatorTest {
    private GrammarInputValidator grammarInputValidator;

    @Before
    public void setUp() {
        VariableManager variableManager = new MapVariableManager(new HashMap<>());

        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "5", "a"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "7", "b"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.NUMBER, "8", "c"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'abra'", "str"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.STRING, "'cadabra'", "str2"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "TRUE", "bool"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "FALSE", "bool2"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.BOOLEAN, "TRUE", "bool3"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.ARRAY, "{1,2,3}", "arr1"));
        variableManager.addVariable(new EvaluatedVariable(VariableType.ARRAY, "{11,22,33}", "arr2"));

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
    public void testBoolDefinitionExclamationMarkSimple() {
        assertTrue(grammarInputValidator.isValid("! bool == false"));
    }

    @Test
    public void testBoolDefinitionExclamationMark() {
        assertTrue(grammarInputValidator.isValid("! bool == true || ! false"));
    }

    @Test
    public void testString() {
        assertTrue(grammarInputValidator.isValid("'this' < 'that'"));
    }

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
        assertFalse(grammarInputValidator.isValid("str * ( a + 5 ) < 'lqlq' * ( 3 + b )"));
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
        assertFalse(grammarInputValidator.isValid("str + ( 3 * str2 ) == str2 * ( 8 + a )"));
    }

    @Test
    public void testInvalidStringMissingVariable() {
        assertFalse(grammarInputValidator.isValid("str + 3 * str2 == str3 * ( 8 + a )"));
    }

    @Test
    public void testBoolean() {
        assertFalse(grammarInputValidator.isValid("bool && bool2 || bool3"));
    }

    @Test
    public void testIntegerIncomplete() {
        assertFalse(grammarInputValidator.isValid("5 + 6 + 7"));
    }

    @Test
    public void testInvalidVariableName() {
        assertFalse(grammarInputValidator.isValid("a5s = 5"));
    }

    @Test
    public void testVariableTypeChange() {
        assertFalse(grammarInputValidator.isValid("a = 'str'"));
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
    public void testInvalidBracketsSurroundAll() {
        assertFalse(grammarInputValidator.isValid("((3 + 4) == 7)"));
    }

    @Test
    public void testInvalidBracketsUneven() {
        assertFalse(grammarInputValidator.isValid("((3 + 4) == 7"));
    }

    @Test
    public void testInvalidBracketsBadOrder() {
        assertFalse((grammarInputValidator.isValid(")3( == 3")));
    }

    @Test
    public void testBooleanBrackets() {
        assertTrue(grammarInputValidator.isValid("( true || false ) && true == false"));
    }

    @Test
    public void testBooleanBracketsUneven() {
        assertFalse(grammarInputValidator.isValid("( true )) == bool"));
    }

    @Test
    public void testBooleanDifferentCaseFalse() {
        assertTrue(grammarInputValidator.isValid("bool == FALse"));
    }

    @Test
    public void testBooleanDifferentCaseTrue() {
        assertTrue(grammarInputValidator.isValid("bool == tRuE"));
    }

    @Test
    public void testArrayAdditionalComma() {
        assertFalse(grammarInputValidator.isValid("arr = {1,2,3,}"));
    }

    @Test
    public void testArrayInvalidElement() {
        assertFalse(grammarInputValidator.isValid("arr = {1,2,3,a}"));
    }

    @Test
    public void testArray() {
        assertTrue(grammarInputValidator.isValid("arr = {1,2,3}"));
    }

    @Test
    public void testArrayNumberAddition() {
        assertTrue(grammarInputValidator.isValid("arr1 + 5 == {12,24,36}"));
    }

    @Test
    public void testArrayAdditionEqualsNumber() {
        assertFalse(grammarInputValidator.isValid("arr1 + arr2 == 5"));
    }

    @Test
    public void testArrayAdditionLessOrEqual() {
        assertTrue(grammarInputValidator.isValid("arr1 + arr2 <= {1,2,3}"));
    }

    @Test
    public void testArrayMultiply() {
        assertTrue(grammarInputValidator.isValid("arr1 * arr2 > {12,24,36}"));
    }

    @Test
    public void testArrayAddition() {
        assertTrue(grammarInputValidator.isValid("arr1 + arr2 == {12,24,36}"));
    }

    @Test
    public void testBoolInequality() {
        assertFalse(grammarInputValidator.isValid("true < bool"));
    }

    @Test
    public void testIncomplete() {
        assertFalse(grammarInputValidator.isValid("5 + 2 <="));
    }

    @Test
    public void testArrayValidInput() {
        assertTrue(grammarInputValidator.isValid("( 5 * arr1 ) + {8,9,0} != {12,24,36} * ( arr2 + 5 )"));
    }

    @Test
    public void testBoolUnequal() {
        assertTrue(grammarInputValidator.isValid("true != true"));
    }

    @Test
    public void testStringMultiplication() {
        assertFalse(grammarInputValidator.isValid("'a' * 'a' == 'a'"));
    }

    @Test
    public void testStringBrackets() {
        assertTrue(grammarInputValidator.isValid("str + ( 'asd' + str2 ) == str"));
    }

    @Test
    public void testStringAddition() {
        assertTrue(grammarInputValidator.isValid("'as' + str == str2"));
    }

    @Test
    public void testStringNumberMultiplication() {
        assertFalse(grammarInputValidator.isValid("'as' * 2 == str2"));
    }

    @Test
    public void testNumberStringMultiplication() {
        assertFalse(grammarInputValidator.isValid("2 * 'as' == str2"));
    }

    @Test
    public void testComplexBooleans() {
        assertTrue(grammarInputValidator.isValid("bool && ( ! bool || true ) == bool && bool"));
    }
}
