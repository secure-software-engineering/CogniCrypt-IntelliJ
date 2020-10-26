package de.fraunhofer.iem.icognicrypt.core;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for Packages that shall run in Background. A package is considered to be an instance that contains routines to setup services which will
 * be used by the plugin.
 *
 * NOTE: When running a {@link BackgroundComponent} as a application-component pushing the modal dialog into background causes it to disappear from the
 * background task window of the IDE.
 */
public class BackgroundComponent implements BaseComponent
{
    protected final Logger Logger = com.intellij.openapi.diagnostic.Logger.getInstance(this.getClass());
    protected String Title = "Running CogniCrypt";
    protected boolean CanCancelInit = true;


    @Override
    public final void initComponent()
    {
        // Commented code does not create any dialog but i can not get it to display if I'd want to.
        // ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        // ProgressManager.getInstance().runProcessWithProgressAsynchronously(new PackageTask(this), indicator);
        ProgressManager.getInstance().run(new PackageTask(this));
    }

    protected void InitializeInBackground(ProgressIndicator indicator) throws CogniCryptException
    {
    }

    protected void OnCanceled()
    {
    }

    private class PackageTask extends Task.Backgroundable
    {
        private BackgroundComponent _owner;

        public PackageTask(BackgroundComponent owner)
        {
            super(null, owner.Title, owner.CanCancelInit);
            _owner = owner;
        }

        @Override
        public void run(@NotNull ProgressIndicator indicator)
        {
            try
            {
                _owner.InitializeInBackground(indicator);
            }
            catch (CogniCryptException e)
            {

            }
        }

        @Override
        public void onThrowable(@NotNull Throwable error)
        {
            Logger.error(error);
            super.onThrowable(error);
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
