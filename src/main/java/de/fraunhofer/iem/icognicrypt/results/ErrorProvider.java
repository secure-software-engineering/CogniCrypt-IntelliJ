package de.fraunhofer.iem.icognicrypt.results;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ErrorProvider {

    private static HashMap<Integer, CogniCryptError> errors = new HashMap<>();

    static void addError(int lineNumber, CogniCryptError error) {
        errors.put(lineNumber, error);
    }

    public static void clearError() {
        errors.clear();
    }

    static boolean errorExists(int lineNumber) {
        return errors.containsKey(lineNumber) && !errors.get(lineNumber).isVisible();
    }

    static String getError(int lineNumber) {
        errors.get(lineNumber).setVisible(true);
        return errors.get(lineNumber).getErrorMessage();
    }

    public static int getErrorCount() {
        return errors.values().size();
    }

    public static Collection<String> getErrorClasses() {
        Set<String> errorClasses = Sets.newHashSet();
        for(CogniCryptError e: errors.values()){
            errorClasses.add(e.getClassName());
        }
        return errorClasses;
    }
}
