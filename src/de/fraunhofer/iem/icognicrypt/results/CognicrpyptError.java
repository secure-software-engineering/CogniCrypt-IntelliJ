package de.fraunhofer.iem.icognicrypt.results;

import crypto.analysis.errors.AbstractError;

public class CognicrpyptError  {

    private AbstractError abstractError;
    private boolean isVisible;

    public CognicrpyptError(AbstractError error) {
        abstractError = error;
        isVisible = false;
    }

    public AbstractError getAbstractError() {
        return abstractError;
    }

    public void setAbstractError(AbstractError abstractError) {
        this.abstractError = abstractError;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
