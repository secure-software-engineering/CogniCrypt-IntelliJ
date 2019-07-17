package de.fraunhofer.iem.icognicrypt.core.Collections;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.NoSuchElementException;

public class Linq
{
    public static <T> T last(Collection<T> collection)
    {
        if (collection == null || collection.isEmpty())
            throw new NoSuchElementException();
        return Iterables.getLast(collection);
    }

    public static <T> T lastOrDefault(Collection<T> collection)
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
}
