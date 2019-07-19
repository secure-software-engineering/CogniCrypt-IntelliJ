package de.fraunhofer.iem.icognicrypt.results.model;

public class CogniCryptAnalysisResultLocation
{
    private final String _className;
    private final MethodSignature _method;
    private final int _line;

    public String GetClassName()
    {
        return _className;
    }

    public MethodSignature GetMethod()
    {
        return _method;
    }

    public int GetLine()
    {
        return _line;
    }

    public CogniCryptAnalysisResultLocation(String className, MethodSignature methodSignature, int line){
        _className = className;
        _method = methodSignature;
        _line = line;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
