package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.project.Project;

public interface ICogniCryptProjectListener
{
    void OnProjectOpened(Project project);

    void OnProjectInitialized(Project project);

    void OnProjectClosing(Project project);

    void OnProjectClosed(Project project);
}
