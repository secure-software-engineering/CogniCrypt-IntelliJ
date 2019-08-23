package de.fraunhofer.iem.icognicrypt.core.Collections;

import com.google.common.collect.Iterables;

import java.lang.reflect.Array;
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

    public static <T> boolean any(Iterable<T> collection){
        if (collection == null)
            throw new IllegalArgumentException();
        return collection.iterator().hasNext();
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
}
