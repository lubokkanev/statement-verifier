package com.nvl.ui;

import com.nvl.verificator.AssertionVerifier;

public class GraphicalUserInterfaceImpl implements GraphicalUserInterface {
    private AssertionVerifier assertionVerificator;

    public GraphicalUserInterfaceImpl(AssertionVerifier assertionVerificator) {
        this.assertionVerificator = assertionVerificator;
    }

    @Override
    public void start() {
        // TODO: Niki
    }
}
