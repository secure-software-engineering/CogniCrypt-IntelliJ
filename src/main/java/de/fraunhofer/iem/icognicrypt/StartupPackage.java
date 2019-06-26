package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.progress.ProgressIndicator;
import de.fraunhofer.iem.icognicrypt.core.BackgroundPackage;

/**
 * This class is the main entry point to CogniCrypt. When a project is loaded after the IDE was started we consider the IDE setup to be completed.
 * After that event this class will run the {@link CogniCryptPlugin} which initializes the all other components. The project listener will be destroyed after that.
 *
 * Do not use this class for anything else.
 */
public class StartupPackage extends BackgroundPackage
{
    public StartupPackage(){
        Title = "Test123";
        CanCancelInit = true;
    }

    @Override
    protected void InitializeInBackground(ProgressIndicator indicator)
    {
        System.out.println("starting");
        indicator.setIndeterminate(true);
        for (int i = 0 ; i < 10; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("interruped");
                e.printStackTrace();
            }
            indicator.checkCanceled();
        }
        System.out.println("success");
    }
}


