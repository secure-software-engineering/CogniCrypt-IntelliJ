package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ServiceManager;

public class TestPackage implements BaseComponent
{
    public TestPackage(){
        System.out.println("Test Thread: " + Thread.currentThread().getId());
    }
}
