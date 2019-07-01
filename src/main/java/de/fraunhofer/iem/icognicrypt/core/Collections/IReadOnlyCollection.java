package de.fraunhofer.iem.icognicrypt.core.Collections;

import org.jetbrains.annotations.Contract;

import java.util.Collection;

public interface IReadOnlyCollection<T> extends Iterable<T>
{
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    <T> T[] toArray(T[] a);

    boolean containsAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    <T> T Get(int index);

    int indexOf(T item);

    @Contract(value = "null -> false", pure = true)
    boolean equals(Object o);

    int hashCode();
}
