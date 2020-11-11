// Copyright (c) .NET Foundation and Contributors

package de.fraunhofer.iem.icognicrypt.core.java;

import kotlin.LazyThreadSafetyMode;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Callable;

import static kotlin.LazyThreadSafetyMode.SYNCHRONIZED;

public class Lazy<T>
{
    private enum LazyState {
        NoneViaConstructor,
        NoneViaFactory,
        NoneException,

        PublicationOnlyViaConstructor,
        PublicationOnlyViaFactory,
        PublicationOnlyWait,
        PublicationOnlyException,

        SynchronizedViaConstructor,
        SynchronizedViaFactory,
        SynchronizedException,
    }

    private static class LazyHelper
    {
        static final LazyHelper NoneViaConstructor = new LazyHelper(LazyState.NoneViaConstructor);
        static final LazyHelper NoneViaFactory = new LazyHelper(LazyState.NoneViaFactory);
        static final LazyHelper PublicationOnlyViaConstructor = new LazyHelper(LazyState.PublicationOnlyViaConstructor);
        static final LazyHelper PublicationOnlyViaFactory = new LazyHelper(LazyState.PublicationOnlyViaFactory);

        LazyState State = null;
        private Exception _exception = null;

        LazyHelper(LazyThreadSafetyMode mode, Exception exception)
        {
            switch (mode)
            {
                case SYNCHRONIZED:
                    State = LazyState.SynchronizedException;
                    break;
                case NONE:
                    State = LazyState.NoneException;
                    break;
                case PUBLICATION:
                    State = LazyState.PublicationOnlyException;
                    break;
                default:
                    break;
            }

            _exception = exception;
        }

        LazyHelper(LazyState state)
        {
            State = state;
        }

        void ThrowException() throws Exception {
            throw _exception;
        }

        static LazyHelper Create(LazyThreadSafetyMode mode, boolean useDefaultConstructor) {
            switch (mode) {
                case NONE:
                    return useDefaultConstructor ? NoneViaConstructor : NoneViaFactory;

                case PUBLICATION:
                    return useDefaultConstructor ? PublicationOnlyViaConstructor : PublicationOnlyViaFactory;

                case SYNCHRONIZED:
                    LazyState state = useDefaultConstructor ?
                            LazyState.SynchronizedViaConstructor :
                            LazyState.SynchronizedViaFactory;
                    return new LazyHelper(state);

                default:
                    throw new IndexOutOfBoundsException();
            }
        }
    }

    private volatile LazyHelper _state;
    private T _value = null;
    private Callable<T> _factory;

    public boolean IsValueCreated() {
        return _state == null;
    }

    public T GetValue(){
        try {
            return _state == null ? _value : CreateValue();
        } catch (Exception e) {
            return null;
        }
    }

    public Lazy(T value){
        _value = value;
    }

    public Lazy(Callable<T> valueFactory)
    {
        this(valueFactory, SYNCHRONIZED, false);
    }

    private Lazy(Callable<T> valueFactory, LazyThreadSafetyMode mode, boolean useDefaultConstructor){
        if (valueFactory == null && !useDefaultConstructor)
            throw new IllegalArgumentException("valueFactory");

        _factory = valueFactory;
        _state = LazyHelper.Create(mode, useDefaultConstructor);
    }

    @Override
    public String toString() {
        if (IsValueCreated())
            return GetValue().toString();
        return "Value is not created";
    }

    private T CreateValue() throws Exception {
        LazyHelper state = _state;
        if (state != null){
            switch (state.State)
            {
                /*
                case NoneViaConstructor:
                    break;
                case NoneViaFactory:
                    break;
                case PublicationOnlyViaConstructor:
                    break;
                case PublicationOnlyViaFactory:
                    break;
                case PublicationOnlyWait:
                    break;
                case SynchronizedViaConstructor:
                    break;
                 */
                case SynchronizedViaFactory:
                    Synchronize(state, false);
                    break;
                default:
                    state.ThrowException();
                    break;
            }
        }
        return GetValue();
    }

    private void Synchronize(LazyHelper executionAndPublication, boolean useDefaultConstructor) throws Exception {
        synchronized(executionAndPublication)
        {
            if (_state == executionAndPublication)
            {
                if (useDefaultConstructor)
                    ViaConstructor();
                else
                    ViaFactory(SYNCHRONIZED);
            }
        }
    }

    private void ViaConstructor() throws Exception {
        _value = CreateViaDefaultConstructor();
        _state = null;
    }

    private void ViaFactory(LazyThreadSafetyMode mode) throws Exception {
        try
        {
            Callable<T> factory = _factory;
            if (factory == null)
                throw new OperationNotSupportedException("Lazy: Recursive calls to value");
            _factory = null;

            _value = factory.call();
            _state = null;
        }
        catch (Exception exception)
        {
            _state = new LazyHelper(mode, exception);
            throw exception;
        }
    }

    private <T> T CreateViaDefaultConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        return persistentClass.getDeclaredConstructor().newInstance();
    }

}
