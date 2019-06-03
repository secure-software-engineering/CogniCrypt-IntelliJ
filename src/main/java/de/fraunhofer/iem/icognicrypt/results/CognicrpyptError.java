package de.fraunhofer.iem.icognicrypt.results;

import crypto.analysis.errors.AbstractError;

public class CognicrpyptError  {

    private String errorMessage;
    private boolean isVisible;

    public CognicrpyptError(String error) {
        errorMessage = error;
        isVisible = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
