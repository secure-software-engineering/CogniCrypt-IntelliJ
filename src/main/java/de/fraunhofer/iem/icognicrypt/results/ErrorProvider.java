package de.fraunhofer.iem.icognicrypt.results;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ErrorProvider {

    // TODO: Make this project depended to support multi-project
    // TODO: Add listeners to events like errors added/removed
    // TODO: This class should not 'publicly require'


    private static Table<String, Integer, Set<CogniCryptError>> errors = HashBasedTable.create();
    private static int errorCount = 0;
    public static void addError(String fullyQualifiedClassName, int lineNumber, CogniCryptError error) {
        Set<CogniCryptError> s = getErrors(fullyQualifiedClassName, lineNumber);
        if(s.add(error)) {
            errorCount++;
        }
        errors.put(fullyQualifiedClassName, lineNumber, s);
    }

    public static void clearError() {
        errorCount = 0;
        errors.clear();
    }

    private static Set<CogniCryptError> getErrors(String fullyQualifiedClassName, int lineNumber) {
        return errors.get(fullyQualifiedClassName, lineNumber) != null ? errors.get(fullyQualifiedClassName, lineNumber) : Sets.newHashSet();
    }
    public static Set<CogniCryptError> findErrors(String javaAbsolutFilePath, int lineNumber) {
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

    public static int getErrorCount() {
        return errorCount;
    }

    public static Collection<String> getErrorClasses() {
        return errors.rowKeySet();
    }

    public static Table<String, Integer, Set<CogniCryptError>> GetErrors(){
        return HashBasedTable.create(errors);
    }
}
