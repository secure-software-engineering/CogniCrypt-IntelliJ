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
        while (iterable.iterator().hasNext())
            list.add(iterable.iterator().next());
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
}
