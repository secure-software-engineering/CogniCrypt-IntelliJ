package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectTypeService;

public class TestServiceB
{
    private final TestServiceA _serviceA;

    public TestServiceB(Project project, TestServiceA serviceA){
        _serviceA = serviceA;
    }
}
