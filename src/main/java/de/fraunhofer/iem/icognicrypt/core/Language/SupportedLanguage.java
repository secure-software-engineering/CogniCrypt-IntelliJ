package de.fraunhofer.iem.icognicrypt.core.Language;

public enum SupportedLanguage
{
    Java("JAVA"),
    Kotlin("kotlin");
    // TODO: Test and re-enable
    //Scala("Scala"),
    //Clojure("Clojure");

    private final String _id;

    SupportedLanguage(String id)
    {
        _id = id;
    }

    public String GetId(){
        return _id;
    }
}
