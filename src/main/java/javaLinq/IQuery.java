package javaLinq;

import java.util.function.Function;

public interface IQuery
{
    boolean any();

    <T> boolean any(Function<T, Boolean> predicate);

    <T> boolean all(Function<T, Boolean> predicate);
}
