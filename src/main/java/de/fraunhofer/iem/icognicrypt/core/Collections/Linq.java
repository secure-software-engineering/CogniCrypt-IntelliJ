// Copyright (c) .NET Foundation and Contributors
package de.fraunhofer.iem.icognicrypt.core.Collections;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class Linq
{
    public static <T> T last(Iterable<T> collection)
    {
        if (collection == null)
            throw new NoSuchElementException();
        return Iterables.getLast(collection);
    }

    public static <T> T lastOrDefault(Iterable<T> collection)
    {
        try
        {
            return last(collection);
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }

    public static <T> boolean any(T[] array, Function<T, Boolean> predicate){
        Iterable<T> list = Arrays.asList(array);
        return any(list, predicate);
    }

    public static <T> boolean any(Iterable<T> collection, Function<T, Boolean> predicate){
        if (collection == null || predicate == null)
            throw new IllegalArgumentException();

        for (T item : collection)
        {
            if (predicate.apply(item))
                return true;
        }
        return false;
    }

    public static <T> boolean any(Iterable<T> collection){
        if (collection == null)
            throw new IllegalArgumentException();
        if (collection instanceof Collection)
            return ((Collection<T>)collection).size() != 0;
        return collection.iterator().hasNext();
    }

    public static <T> boolean all(Iterable<T> collection, Function<T, Boolean> predicate){
        if (collection == null || predicate == null)
            throw new IllegalArgumentException();

        for (T item : collection)
        {
            if (!predicate.apply(item))
                return false;
        }
        return true;
    }

    public static <T> List<T> toList(Iterable<T> iterable)
    {
        if (iterable instanceof List)
            return (List) iterable;

        List<T> list = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }

    public static <T, R> Iterable<R> select(Iterable<T> source, Function<T, R> selector){
        if (source == null || selector == null)
            throw new IllegalArgumentException();
        Collection<R> result = new ArrayList<>();
        for (T item : source)
            result.add(selector.apply(item));
        return result;
    }

    public static <T> int count(Iterable<T> source)
    {
        if (source == null)
            throw new NullPointerException();

        if (source instanceof Collection)
            return ((Collection) source).size();

        int count = 0;

        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext())
            iterator.next();
            count++;
        return count;
    }

    public <T> Iterable<T> distinct(Iterable<T> source)
    {
        return distinct(source, null);
    }

    public <T> Iterable<T> distinct(Iterable<T> source, Comparator<T> comparator)
    {
        if (source == null)
            throw new NullPointerException();

        return new DistinctIterator<T>(source, comparator);
    }


    private final class DistinctIterator<T> extends IteratorBase<T>
    {
        private final Iterable<T> _source;
        private final Comparator<T> _comperator;
        private Set<T> _set;
        private Iterator<T> _iterator;

        public DistinctIterator(Iterable<T> source, Comparator<T> comparator)
        {
            _source = source;
            _comperator = comparator;
        }

        @Override
        public boolean hasNext()
        {
            return false;
        }

        @Override
        public T next()
        {
            return null;
        }

        @Override
        public IteratorBase<T> clone()
        {
            return new DistinctIterator<T>(_source, _comperator);
        }

        @Override
        public boolean MoveNext()
        {
            return false;
        }

        @Override
        public void dispose()
        {
            if (_iterator != null){
                _iterator = null;
                _set = null;
            }
            super.dispose();
        }
    }

    private abstract class IteratorBase<T> implements Iterator<T>, Iterable<T>
    {
        private final long _threadId;
        private int _state;

        private T _current;

        public T getCurrent(){
            return _current;
        }

        protected IteratorBase()
        {
            _threadId = Thread.currentThread().getId();
        }

        public abstract IteratorBase<T> clone();

        public abstract boolean MoveNext();

        public void dispose(){
            _current = null;
            _state = -1;
        }

        @NotNull
        @Override
        public final Iterator<T> iterator()
        {
            IteratorBase<T> iterator = _state == 0 && _threadId == Thread.currentThread().getId() ? this : clone();
            iterator._state = 1;
            return iterator;
        }
    }
}
