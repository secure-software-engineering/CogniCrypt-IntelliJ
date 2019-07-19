package de.fraunhofer.iem.icognicrypt.results.model;

import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;
import de.fraunhofer.iem.icognicrypt.core.Collections.ReadOnlyCollection;
import soot.SootMethod;

import java.util.List;
import java.util.stream.Collectors;

public class MethodSignature
{
    private String _name;
    private IReadOnlyCollection<String> _params;
    private String _returnType;

    public String GetName()
    {
        return _name;
    }

    public IReadOnlyCollection<String> GetParams()
    {
        return _params;
    }

    public String GetReturnType()
    {
        return _returnType;
    }

    public MethodSignature(SootMethod method)
    {
        List<String> params = method.getParameterTypes().stream().map(x -> x.toString()).collect(Collectors.toList());
        _name =  method.getName();
        _returnType =  method.getReturnType().toString();
        _params = new ReadOnlyCollection<>(params);
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
