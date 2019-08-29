// Copyright (c) .NET Foundation and Contributors
package javaLinq;

import com.google.common.collect.Iterables;
import org.apache.commons.lang.ObjectUtils;

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

    public static  <T> Iterable<T> distinct(Iterable<T> source)
    {
        return new HashSet<T>(Linq.toList(source));
    }

    public static <T> Iterable<T> where(Iterable<T> source, Function<T, Boolean> predicate){

        List<T> result = new ArrayList<>();

        for (T item : source){
            if (predicate.apply(item))
                result.add(item);
        }
        return result;
    }

    public static <T> boolean contains(Iterable<T> source, T value)
    {
        if (source == null)
            throw new NullPointerException();

        if (source instanceof Collection)
            return ((Collection)source).contains(value);
        else
        {
            for (T element : source){
                if (element.equals(value))
                    return true;
            }
        }
        return false;
    }

}

