package de.fraunhofer.iem.icognicrypt.results.model;

import com.intellij.openapi.util.UserDataHolderBase;
import crypto.analysis.errors.AbstractError;
import crypto.analysis.errors.ConstraintError;
import crypto.interfaces.ISLConstraint;
import soot.SootMethod;
import soot.jimple.Stmt;

public class CogniCryptAnalysisResult extends UserDataHolderBase
{
    CogniCryptAnalysisResultLocation _location;
    String _message;
    CogniCryptErrorCategory _errorType;

    public CogniCryptAnalysisResultLocation GetLocation()
    {
        return _location;
    }

    public CogniCryptErrorCategory GetErrorType(){
        return _errorType;
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

        if (cogniCryptError instanceof ConstraintError){
            ConstraintError constraintError = (ConstraintError) cogniCryptError;

            ISLConstraint constraint = constraintError.getBrokenConstraint();
            String value = constraintError.getCallSiteWithExtractedValue().getVal().getValue().toString();
        }


        MethodSignature methodSignature = new MethodSignature(affectedMethod);
        CogniCryptAnalysisResultLocation location = new CogniCryptAnalysisResultLocation(affectedClass, methodSignature, line);
        _location = location;
        _message = cogniCryptError.toErrorMarkerString();
    }

    public static void Test(Object t, Object s){
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof CogniCryptAnalysisResult)) return false;
        CogniCryptAnalysisResult other = (CogniCryptAnalysisResult) obj;
        boolean sameLocation = GetLocation().equals(other.GetLocation());
        return sameLocation && _errorType == other.GetErrorType();
    }
}


