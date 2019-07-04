package de.fraunhofer.iem.icognicrypt.analysis;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.LineMarkersPass;
import com.intellij.ide.plugins.PluginManagerMain;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.results.ui.CogniCryptResultWindow;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import crypto.analysis.errors.AbstractError;
import crypto.analysis.errors.IncompleteOperationError;
import de.fraunhofer.iem.crypto.CogniCryptAndroidAnalysis;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ErrorProvider;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptToolWindowManager;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import soot.G;
import soot.SootClass;

import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class AndroidProjectAnalysisQueue extends Task.Backgroundable{

    private static final Logger logger = Logger.getInstance(AndroidProjectAnalysisQueue.class);
    private final CogniCryptToolWindowManager _toolWindowManager;
    private Queue<CogniCryptAndroidAnalysis> _analysisQueue;

    private Project _project;
    private final List<String> sourceCodeJavaFiles;

    public AndroidProjectAnalysisQueue(Project p, Queue<CogniCryptAndroidAnalysis> analysisQueue){
        super(p, "Performing CogniCrypt Analysis");
        _project = p;
        _analysisQueue = analysisQueue;

        _toolWindowManager = ServiceManager.getService(CogniCryptToolWindowManager.class);
        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY) {
            sourceCodeJavaFiles = findFullyQualifiedJavaClassNames();
        } else {
            sourceCodeJavaFiles = Lists.newArrayList();
        }
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        // Remove errors before rerunning Cognicrypt
        ErrorProvider.clearError();

        int size = _analysisQueue.size();
        int index = 0;  Stopwatch w = Stopwatch.createStarted();
        while(!_analysisQueue.isEmpty()){
            index++;
            indicator.setText(String.format("Performing CogniCrypt Analysis (APK %s of %s)", index,size));
            G.v().reset();
            CogniCryptAndroidAnalysis curr = _analysisQueue.poll();

            try {
                Collection<AbstractError> results = curr.run();
                for(AbstractError abstractError : results){
                    if (abstractError.getErrorLocation().getUnit().isPresent()) {
                        if(isIgnoredErrorType(abstractError))
                            continue;
                        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();

                        ErrorProvider.addError(affectedClass.getName(), abstractError.getErrorLocation().getUnit().get().getJavaSourceStartLineNumber() - 1, new CogniCryptError(abstractError.toErrorMarkerString(), affectedClass.getName()));
                    }
                }
            } catch (Throwable e){
                Notification notification = new Notification("CogniCrypt", "CogniCrypt", String.format("Crashed on %s", curr), NotificationType.INFORMATION);
                logger.error(e);
                Notifications.Bus.notify(notification);
            }
            indicator.setFraction((index / (double)size));
        }
        Notification notification = new Notification("CogniCrypt", "CogniCrypt Info", String.format("Analyzed %s APKs in %s", index,w), NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
        Notification errorNotification = new Notification("CogniCrypt", "CogniCrypt Info", String.format("Found %s errors in classes: ", ErrorProvider.getErrorCount()) + Joiner.on("\n").join(ErrorProvider.getErrorClasses()), NotificationType.INFORMATION);
        Notifications.Bus.notify(errorNotification);

        try
        {
            ToolWindow t  = _toolWindowManager.GetToolWindow(_project);
            CogniCryptResultWindow errorWindow =  _toolWindowManager.GetWindowModel(t, CogniCryptToolWindowManager.ResultsView, CogniCryptResultWindow.class);
            errorWindow.SetSearchText("Test Text");
        }
        catch (CogniCryptException ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean isIgnoredErrorType(AbstractError abstractError) {
        SootClass affectedClass = abstractError.getErrorLocation().getMethod().getDeclaringClass();
        if(Constants.WARNINGS_IN_SOURCECODECLASSES_ONLY && !isSourceCodeClass(affectedClass)){
            return true;
        }
        //TODO Add options in preference page to enable / disable error types
        return abstractError instanceof IncompleteOperationError;
    }

    private boolean isSourceCodeClass(SootClass affectedClass) {
        return sourceCodeJavaFiles.contains(affectedClass.getName());
    }

    private List<String> findFullyQualifiedJavaClassNames() {
        List<String> results = Lists.newArrayList();
        List<VirtualFile> sourceRoots = getSourceRoots(this.getProject());
        for(VirtualFile m : sourceRoots){
            results.addAll(FileUtils.listFiles(new File(m.getPath()), new String[]{"java"}, true).stream().map(f -> convertToFullyQualifiedClassName(f, m.getPath())
            ).distinct().collect(Collectors.toList()));
        }
        return results;
    }
    private String convertToFullyQualifiedClassName(File javaFile, String sourceCodeBasePath) {
        String withoutFileending = javaFile.getAbsolutePath().replace(".java","");
        String replaceWindowsStrings = withoutFileending.replace("\\","/");
        String stripBasePath = replaceWindowsStrings.replace(sourceCodeBasePath,"");
        String slashesToDots = stripBasePath.replace("/",".");
        return slashesToDots.replaceFirst(".","");
    }
    private List<VirtualFile> getSourceRoots(Project project) {
        List<VirtualFile> res = Lists.newArrayList();
        for(Module m : ModuleManager.getInstance(project).getModules()){
            ModuleRootManager mgr = ModuleRootManager.getInstance(m);
            for(VirtualFile f : mgr.getSourceRoots(false)) {
                res.add(f);
            }
        }
        return res;
    }
}
