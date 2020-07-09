package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.ClassElement;
import org.jetbrains.kotlin.psi.*;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UElementKt;
import java.util.Set;

public class KotlinErrorLineMarker implements LineMarkerProvider
{
    // TODO: Somehow this code is not executed with Kotlin files.
    @Nullable
    public LineMarkerInfo getLineMarkerInfo(@NotNull KtElement psiElement)
    {
            IResultProvider resultProvider = ServiceManager.getService(psiElement.getProject(), IResultProvider.class);
            int lineNumber = getLineNumber(psiElement) + 1;

        UElement uElement= (UElement) psiElement.getNode();
       // PsiElement element= UElementKt.getAsJavaPsiElement((UElement) psiElement.getNode(),psiElement.getClass());
        PsiElement element= uElement.getSourcePsi();
            String path = psiElement.getContainingFile().getVirtualFile().getPath();
            Set<CogniCryptError> errors = resultProvider.FindErrors(path, lineNumber);
            if (!errors.isEmpty())
                return CreateNewMarker(element, errors);

        return null;
    }
    /*
        @Override
        public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {
            int i = 0;
        }
    */
    private LineMarkerInfo CreateNewMarker(PsiElement element, Iterable<CogniCryptError> errors){
        return new LineMarkerInfo<>(element, element.getTextRange(), PluginIcons.ERROR, Pass.LINE_MARKERS,
                new TooltipProvider(getErrorsMessage(errors)), null, GutterIconRenderer.Alignment.LEFT);
    }

    private String getErrorsMessage(Iterable<CogniCryptError> errors) {
        String s = "";
        for(CogniCryptError e : errors){
            s += e.getErrorMessage() +"\n";
        }
        return s;
    }

    //Returns line number for PSI element
    private int getLineNumber(KtElement psiElement) {
        KtFile containingFile = (KtFile) psiElement.getContainingFile();
        Project project = containingFile.getProject();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = psiDocumentManager.getDocument(containingFile);
        int textOffset = psiElement.getTextOffset();

        return document.getLineNumber(textOffset);
    }

    private PsiClass FindClass(KtElement element){
        if (element instanceof KtClass)
            return (PsiClass) element;
        if (element == null)
            return null;
        return FindClass((KtElement) element.getParent());
    }

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }
}
