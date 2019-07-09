package de.fraunhofer.iem.icognicrypt.results;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.intellij.util.containers.WeakList;

import java.util.Set;

public class ResultsProviderService implements IResultProvider
{
    WeakList<IResultsProviderListener> _listeners = new WeakList<>();

    private static Table<String, Integer, Set<CogniCryptError>> errors = HashBasedTable.create();

    private static int errorCount = 0;

    @Override
    public void GetResults()
    {

    }

    @Override
    public void AddResult()
    {

    }

    @Override
    public void RemoveResult()
    {

    }

    @Override
    public void RemoveAllResults()
    {

    }

    @Override
    public void Subscribe(IResultsProviderListener listener)
    {
        if (_listeners.contains(listener))
            return;
        _listeners.add(listener);
    }

    @Override
    public void Unsubscribe(IResultsProviderListener listener)
    {
        _listeners.remove(listener);
    }

    @Override
    public void AddResult(String fullyQualifiedClassName, int lineNumber, CogniCryptError error)
    {
        Set<CogniCryptError> s = getErrors(fullyQualifiedClassName, lineNumber);
        if(s.add(error)) {
            errorCount++;
        }
        errors.put(fullyQualifiedClassName, lineNumber, s);
    }

    private static Set<CogniCryptError> getErrors(String fullyQualifiedClassName, int lineNumber) {
        return errors.get(fullyQualifiedClassName, lineNumber) != null ? errors.get(fullyQualifiedClassName, lineNumber) : Sets.newHashSet();
    }

    public Table<String, Integer, Set<CogniCryptError>> GetErrors(){
        return HashBasedTable.create(errors);
    }
}
