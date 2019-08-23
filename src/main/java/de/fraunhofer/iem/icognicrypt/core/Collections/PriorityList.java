package de.fraunhofer.iem.icognicrypt.core.Collections;

import java.util.*;

public class PriorityList<T> extends AbstractList<T>
{
    private LinkedList<T> _innerList = new LinkedList<>();

    public PriorityList(){
    }

    public PriorityList(Collection<T> list)
    {
        _innerList.addAll(list);
    }

    @Override
    public T get(int index)
    {
        return _innerList.get(index);
    }

    public void Promote(T element){
        Promote(element, 0);
    }

    public void Promote(T element, int index)
    {
        if (element == null || !_innerList.contains(element))
            throw new NoSuchElementException();
        if (index < 0)
            throw new ArrayIndexOutOfBoundsException();
        if (index > _innerList.size() + 1)
            index = _innerList.size() +1;
        _innerList.remove(element);
        _innerList.add(index, element);
    }

    @Override
    public int size()
    {
        return _innerList.size();
    }

    @Override
    public T set(int index, T element)
    {
        return _innerList.set(index, element);
    }

    @Override
    public void add(int index, T element)
    {
        _innerList.add(index, element);
    }

    @Override
    public T remove(int index)
    {
        return _innerList.remove(index);
    }

    @Override
    public Iterator<T> iterator()
    {
        return _innerList.iterator();
    }
}
