package com.nvl.verifier.validator;

import com.nvl.variable.VariableType;
import com.nvl.variable.manager.VariableManager;

public class GrammarInputValidator implements InputValidator {
    private VariableManager variableManager;

    private SplitString splitString; // new

    public GrammarInputValidator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    // everything is separated by space; '&&', '||' and '==' are not
    public boolean isValid(String input)
    {
        splitString = new SplitString(input);

        if(splitString.getSplitInput().length < 3)
        {
            return false;
        }

        if(splitString.getSplitInput().length == 3 && splitString.getNthElement(1).equals("="))
        {
            if(!variableManager.containsVariable(splitString.getNthElement(0)))
            {
                return checkString(splitString.getNthElement(2));
            }
            else
            {
                VariableType type = variableManager.getVariable(splitString.getNthElement(0)).getType();
                return compareType(type, splitString.getNthElement(2));
            }
        }

        while(!isEmpty() && splitString.getNthElement(splitString.getPosition()).equals("(")) // skipping brackets
        {
            splitString.setPosition(splitString.getPosition() + 1);
        }

        if(isEmpty())
        {
           return false;
        }

        String current = splitString.getNthElement(splitString.getPosition());
        splitString.setPosition(0); // going back to the beginning

        if(!variableManager.containsVariable(current))
        {
            return false;
        }

        VariableType type = variableManager.getVariable(current).getType();

        if(current.matches("\\d+") || type == VariableType.NUMBER)
        {
            if(parseIntExpression())
            {
                parseIntComparison();
            }
        }

        else if(current.matches("'\\w+'") || type == VariableType.STRING)
        {
            if(parseStringExpression())
            {
                parseStringComparison();
            }
        }

        else if(current.equals("FALSE") || current.equals("TRUE") || current.equals("!") || type == VariableType.BOOLEAN)
        {
            parseBoolExpression();
        }

        return isEmpty();
    }

    private boolean checkString(String stringToCheck)
    {
        return stringToCheck.matches("'\\w+'") || stringToCheck.matches("\\d+") || stringToCheck.equals("FALSE") ||
                stringToCheck.equals("TRUE");
    }

    private boolean compareType(VariableType type, String string)
    {
        if(type == VariableType.STRING && string.matches("'\\w+'"))
        {
            return true;
        }
        else if(type == VariableType.BOOLEAN && (string.equals("FALSE") || string.equals("TRUE")))
        {
            return true;
        }
        else if(type == VariableType.NUMBER && string.matches("\\d+"))
        {
            return true;
        }
        return false;
    }

    private boolean isEmpty()
    {
        return splitString.getPosition() == splitString.getSplitInput().length;
    }

    private boolean parseIntComparison() // debuuug
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && (current.equals("==") || current.equals(">") || current.equals("<")))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseIntExpression();
        }
        return false;
    }

    private boolean parseIntExpression() // deeebuuuug
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && current.equals("("))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseIntExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) // !!
            {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseIntOperation();
            }
        }
        else if(!isEmpty() && (current.matches("\\d+") ||
                (variableManager.containsVariable(current)
                        && variableManager.getVariable(current).getType()== VariableType.NUMBER)))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseIntOperation();
        }
        return false;
    }

    private boolean parseIntOperation()
    {
        if(isEmpty())
        {
            return true;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && (current.equals("+") || current.equals("*")))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseIntExpression()) // !!
            {
                return parseIntOperation();
            }
        }
        return true;
    }

    private boolean parseStringComparison()
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && (current.equals("==") || current.equals(">") || current.equals("<")))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseStringExpression();
        }
        return false;
    }

    private boolean parseStringExpression()
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && current.equals("("))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseStringExpression() && splitString.getNthElement(splitString.getPosition()).equals(")")) // !!
            {
                splitString.setPosition(splitString.getPosition() + 1);
                return parseStringOperation();
            }
        }
        else if(!isEmpty() && (current.matches("'\\w+'") || (variableManager.containsVariable(current) &&
                variableManager.getVariable(current).getType() == VariableType.STRING)))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseStringOperation();
        }
        return false;
    }

    private boolean parseStringOperation()
    {
        if(isEmpty())
        {
            return true;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && (current.equals("+") || current.equals("*")))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseStringExpression()) // !!
            {
                return parseStringOperation();
            }
        }
        return true;
    }

    private boolean parseBoolExpression()
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && current.equals("!"))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseBoolExpression()) // !!
            {
                return parseBoolOperation();
            }
        }
        else if(!isEmpty() && (current.equals("TRUE") || current.equals("FALSE")
                || (variableManager.containsVariable(current) &&
                variableManager.getVariable(current).getType() == VariableType.BOOLEAN)))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            return parseBoolOperation();
        }
        return false;
    }

    private boolean parseBoolOperation()
    {
        if(isEmpty())
        {
            return false;
        }
        String current = splitString.getNthElement(splitString.getPosition());
        if(!isEmpty() && (current.equals("&&") || current.equals("||")))
        {
            splitString.setPosition(splitString.getPosition() + 1);
            if(parseBoolExpression()) // !!
            {
                return parseBoolOperation();
            }
        }
        return true;
    }
}
