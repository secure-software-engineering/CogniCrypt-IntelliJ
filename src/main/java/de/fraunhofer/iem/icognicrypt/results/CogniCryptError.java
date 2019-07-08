package de.fraunhofer.iem.icognicrypt.results;

public class CogniCryptError {

    private String _errorMessage;
    private String _className;
    private int _lineNumer;

    public CogniCryptError(String error, String className, int line) {
        _errorMessage = error;
        _className = className;
        _lineNumer = line;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public String getClassName(){
        return _className;
    }

    public int getLine(){
        return _lineNumer;
    }

}
