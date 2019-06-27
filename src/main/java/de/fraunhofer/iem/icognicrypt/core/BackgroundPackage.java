package de.fraunhofer.iem.icognicrypt.core;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for Packages that shall run in Background. A package is considered to be an instance that contains routines to setup services which will
 * be used by the plugin.
 *
 * NOTE: When running a {@link BackgroundPackage} as a application-component pushing the modal dialog into background causes it to disappear from the
 * background task window of the IDE.
 */
public class BackgroundPackage implements BaseComponent
{
    protected String Title = "";
    protected boolean CanCancelInit = true;


    @Override
    public final void initComponent()
    {
        // Commented code does not create any dialog but i can not get it to display if I'd want to.
        // ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        // ProgressManager.getInstance().runProcessWithProgressAsynchronously(new PackageTask(this), indicator);
        ProgressManager.getInstance().run(new PackageTask(this));
    }

    protected void InitializeInBackground(ProgressIndicator indicator)
    {
    }

    protected void OnCanceled()
    {
    }

    private class PackageTask extends Task.Backgroundable
    {

        private BackgroundPackage _owner;

        public PackageTask(BackgroundPackage owner)
        {
            super(null, owner.Title, owner.CanCancelInit);
            _owner = owner;
        }

        @Override
        public void run(@NotNull ProgressIndicator indicator)
        {
            _owner.InitializeInBackground(indicator);
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
