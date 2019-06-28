package de.fraunhofer.iem.icognicrypt.core.Collections;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class ReadOnlyCollection<T> implements IReadOnlyCollection<T>
{
    private final Collection<T> _inner;

    public ReadOnlyCollection(Collection<T> collection)
    {
        _inner = collection;
    }

    public int size()
    {
        return _inner.size();
    }

    public boolean isEmpty()
    {
        return _inner.isEmpty();
    }

    public boolean contains(Object o)
    {
        return _inner.contains(o);
    }

    public <T> T[] toArray(T[] a)
    {
        return _inner.toArray(a);
    }

    public boolean containsAll(Collection<?> c)
    {
        return _inner.containsAll(c);
    }

    public boolean retainAll(Collection<?> c)
    {
        return _inner.retainAll(c);
    }

    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o)
    {
        return _inner.equals(o);
    }

    public int hashCode()
    {
        return _inner.hashCode();
    }

    @NotNull
    @Override
    public Iterator<T> iterator()
    {
        return _inner.iterator();
    }
}
