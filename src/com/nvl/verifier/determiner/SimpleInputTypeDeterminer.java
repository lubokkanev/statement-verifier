package com.nvl.verifier.determiner;

import com.nvl.variable.manager.VariableManager;

public class SimpleInputTypeDeterminer implements InputTypeDeterminer {
    private VariableManager variableManager;

    public SimpleInputTypeDeterminer(VariableManager variableManager) {
        this.variableManager = variableManager;
    }

    @Override
    public InputType determineType(String input) {
        if (isDefinition(input)) {
            String variableName = input.substring(0, input.indexOf('=')).trim();
            return determineVariableDefinition(variableName);
        } else {
            return InputType.STATEMENT;
        }
    }

    private InputType determineVariableDefinition(String variableName) {
        if (variableManager.containsVariable(variableName)) {
            return InputType.EXISTING_VARIABLE;
        } else {
            return InputType.NEW_VARIABLE;
        }
    }
    
    /*tuk stana ibasi kashata :D*/
    private boolean isDefinition(String input) {
        char[] charInput = input.toCharArray();
        int i = 0;
        while(charInput[i]!='='  && charInput[i]!='>'  && charInput[i]!='<'  && (charInput[i]!='!' || charInput[i+1] != '='))  //input must contain atleast =, >, < 
            i++;
        if(charInput[i]=='>' || charInput[i]=='<' || charInput[i+1] == '=')  //check if input contains ==, !=, >=, <=
            return false;
        return true;  
    }
}
