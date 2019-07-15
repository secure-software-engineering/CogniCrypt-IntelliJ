package de.fraunhofer.iem.icognicrypt.results;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.intellij.openapi.project.Project;

import java.util.Map;
import java.util.Set;

class ResultsProviderService implements IResultProvider
{
//    private final WeakList<IResultsProviderListener> _listeners = new WeakList<>();
//    private final HashSet<CogniCryptAnalysisResult> _results = new HashSet<>();

    private static Table<String, Integer, Set<CogniCryptError>> errors = HashBasedTable.create();

    private static int errorCount = 0;

    ResultsProviderService(Project project)
    {
//        IResultsProviderListener[] listeners = IResultsProviderListener.EP_NAME.getExtensions(project);
//        for (IResultsProviderListener listener : listeners)
//            this.Subscribe(listener);
    }

    /*
    @Override
    public void GetResults()
    {

    }

    @Override
    public void AddResult(CogniCryptAnalysisResult result)
    {
        _results.add(result);


//        for (IResultsProviderListener listener: _listeners)
//            listener.OnResultAdded();
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

     */

    @Override
    public void AddResult(String fullyQualifiedClassName, int lineNumber, CogniCryptError error)
    {
        Set<CogniCryptError> s = getErrors(fullyQualifiedClassName, lineNumber);
        if(s.add(error)) {
            errorCount++;
        }
        errors.put(fullyQualifiedClassName, lineNumber, s);
    }

    public void ClearErrors() {
        errorCount = 0;
        errors.clear();
    }

    public Table<String, Integer, Set<CogniCryptError>> GetErrors()
    {
        return HashBasedTable.create(errors);
    }

    public Set<CogniCryptError> FindErrors(String javaAbsolutFilePath, int lineNumber) {
        Set<CogniCryptError> res = Sets.newHashSet();

        for(String fullyQualifiedClassName : errors.rowKeySet()){
            String path = javaAbsolutFilePath.replace(".java","").replace("/",".");
            if(path.endsWith(fullyQualifiedClassName)){
                Map<Integer, Set<CogniCryptError>> resultsInFile = errors.row(fullyQualifiedClassName);
                if(resultsInFile.get(lineNumber) != null) {
                    res.addAll(resultsInFile.get(lineNumber));
                }
            }
        }
        return res;
    }

    private static Set<CogniCryptError> getErrors(String fullyQualifiedClassName, int lineNumber) {
        return errors.get(fullyQualifiedClassName, lineNumber) != null ? errors.get(fullyQualifiedClassName, lineNumber) : Sets.newHashSet();
    }
}
