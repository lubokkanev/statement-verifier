package com.nvl.ui;

import com.nvl.runner.AssertionVerificator;

public class GraphicalUserInterfaceImpl implements GraphicalUserInterface {
    private AssertionVerificator assertionVerificator;

    public GraphicalUserInterfaceImpl(AssertionVerificator assertionVerificator) {
        this.assertionVerificator = assertionVerificator;
    }

    @Override
    public void start() {
        // TODO: Niki
    }
}
