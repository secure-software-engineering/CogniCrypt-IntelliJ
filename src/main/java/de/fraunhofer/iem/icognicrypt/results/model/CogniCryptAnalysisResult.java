package de.fraunhofer.iem.icognicrypt.results.model;

import boomerang.jimple.Statement;
import crypto.analysis.errors.AbstractError;
import soot.SootMethod;
import soot.jimple.Stmt;

public class CogniCryptAnalysisResult
{
    CogniCryptAnalysisResultLocation _location;
    String _message;

    public CogniCryptAnalysisResultLocation GetLocation()
    {
        return _location;
    }

    public String GetMessage(){
        return _message;
    }

    public CogniCryptAnalysisResult(AbstractError cogniCryptError)
    {
        String affectedClass = cogniCryptError.getErrorLocation().getMethod().getDeclaringClass().getName();
        SootMethod affectedMethod = cogniCryptError.getErrorLocation().getMethod();
        int line = cogniCryptError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber() - 1;

        Stmt statement = cogniCryptError.getErrorLocation().getUnit().get();

        MethodSignature methodSignature = new MethodSignature(affectedMethod);
        CogniCryptAnalysisResultLocation location = new CogniCryptAnalysisResultLocation(affectedClass, methodSignature, line);
        _location = location;
        _message = cogniCryptError.toErrorMarkerString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof CogniCryptAnalysisResult)) return false;
        CogniCryptAnalysisResult other = (CogniCryptAnalysisResult) obj;
        return GetLocation().equals(other.GetLocation());
    }
}


