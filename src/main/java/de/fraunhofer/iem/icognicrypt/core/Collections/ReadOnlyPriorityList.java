package de.fraunhofer.iem.icognicrypt.core.Collections;

import org.bouncycastle.util.Iterable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ReadOnlyPriorityList<T> implements Iterable<T>
{
    private LinkedList<T> _innerList = new LinkedList<>();

    public ReadOnlyPriorityList(Collection<T> list)
    {
        _innerList.addAll(list);
    }

    public ReadOnlyPriorityList(T[] list)
    {
        this(Arrays.asList(list));
    }

    public T get(int index)
    {
        return _innerList.get(index);
    }

    public void Promote(T element){
        Promote(element, 0);
    }

    public void Promote(T element, int index)
    {
        if (element == null)
            throw new NullPointerException();
        if (index < 0)
            throw new ArrayIndexOutOfBoundsException();
        if (index > _innerList.size() + 1)
            index = _innerList.size() +1;
        _innerList.remove(element);
        _innerList.add(index, element);
    }

    public int size()
    {
        return _innerList.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return _innerList.iterator();
    }
}
