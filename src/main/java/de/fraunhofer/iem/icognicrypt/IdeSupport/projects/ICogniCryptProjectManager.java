package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.core.Collections.IReadOnlyCollection;

public interface ICogniCryptProjectManager
{
    void Subscribe(ICogniCryptProjectListener subscriber);

    void UnSubscribe(ICogniCryptProjectListener subscriber);

    IReadOnlyCollection<Project> GetOpenProject();
}
