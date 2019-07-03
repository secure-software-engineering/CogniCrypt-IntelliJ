package de.fraunhofer.iem.icognicrypt.results;

public class CogniCryptError {

    private String errorMessage;
    private String className;
    private boolean isVisible;

    public CogniCryptError(String error, String className) {
        errorMessage = error;
        this.className = className;
        isVisible = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getClassName(){
        return className;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
