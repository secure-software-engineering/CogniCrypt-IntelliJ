package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.project.Project;

public class TestServiceA
{
    private final Project _project;

    public TestServiceA(TestServiceC c, Project a){
        _project = a;
    }
}
