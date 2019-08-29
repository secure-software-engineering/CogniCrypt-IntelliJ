package javaLinq;

import com.intellij.openapi.externalSystem.service.execution.NotSupportedException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Query<T> implements IQuery, Iterable<T>
{
    private final Iterable _source;

    private Query(){
        throw new NotSupportedException("This constructor is not supported");
    }

    private Query(Iterable source){
        if (source == null)
            throw new NullPointerException();
        _source = source;
    }

    @NotNull
    @Override
    public Iterator<T> iterator()
    { return _source.iterator(); }

    public static <T> IQuery from(Iterable<T> source){
        return new Query(source);
    }
}

