package javaLinq;

import com.intellij.openapi.externalSystem.service.execution.NotSupportedException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class Query<T> implements Iterable<T>, IQuery
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
    {
        return _source.iterator();
    }

    public static <T> IQuery from(Iterable<T> source){
        return new Query(source);
    }

    @Override
    public boolean any()
    {
        return Linq.any(_source);
    }

    @Override
    public <T> boolean any(Function<T, Boolean> predicate)
    {
        return Linq.any(_source, predicate);
    }

    @Override
    public <T> boolean all(Function<T, Boolean> predicate)
    {
        return Linq.all(_source, predicate);
    }
}

