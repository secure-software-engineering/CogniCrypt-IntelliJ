package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.project.Project;

public class TestServiceB
{
    private final TestServiceA _serviceA;

    public TestServiceB(Project project, TestServiceA serviceA){
        _serviceA = serviceA;
    }
}
