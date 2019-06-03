package de.fraunhofer.iem.icognicrypt.results;

import java.util.HashMap;

class ErrorProvider {

    static private HashMap<Integer, CognicrpyptError> errors = new HashMap<>();

    static void addError(int lineNumber, CognicrpyptError error) {

        errors.put(lineNumber, error);
    }

    static void clearError() {
        errors.clear();
    }

    static boolean errorExists(int lineNumber) {

        return errors.containsKey(lineNumber) && !errors.get(lineNumber).isVisible();
    }

    static String getError(int lineNumber) {

        errors.get(lineNumber).setVisible(true);

        return errors.get(lineNumber).getErrorMessage();
    }
}
