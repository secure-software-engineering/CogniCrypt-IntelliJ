package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;
import sun.util.resources.cldr.om.CalendarData_om_ET;

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
    }

    @Override
    protected void InitializeAsync(ProgressIndicator indicator)
    {
        System.out.println("starting");
        indicator.setIndeterminate(true);
        indicator.setText("123");
        indicator.setText2("456");
        for (int i = 0 ; i < 100; i++){
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

class BackgroundPackage implements BaseComponent
{
    protected String Title = "";

    @Override
    public final void initComponent()
    {
        ProgressManager.getInstance().run(new BTask(this));
    }

    protected void InitializeAsync(ProgressIndicator indicator){

    }

    protected void OnCanceled(){

    }

    private class BTask extends Task.Backgroundable{

        private BackgroundPackage _owner;

        public BTask(BackgroundPackage owner)
        {
            super(null, owner.Title, true);
            _owner =owner;
        }

        @Override
        public void run(@NotNull ProgressIndicator indicator)
        {
            _owner.InitializeAsync(indicator);
        }

        @Override
        public void onFinished()
        {
            super.onFinished();
            _owner = null;
        }

        @Override
        public void onCancel()
        {
            super.onCancel();
            _owner.OnCanceled();
        }
    }
}
