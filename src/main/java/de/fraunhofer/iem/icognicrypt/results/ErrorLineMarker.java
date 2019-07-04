package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ErrorLineMarker implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {

        if (psiElement instanceof PsiStatement) {

            int lineNumber = getLineNumber(psiElement);
            //Check if an error exists for the line number that the element is located
            Set<CogniCryptError> errors = ErrorProvider.findErrors(psiElement.getContainingFile().getVirtualFile().getPath(), lineNumber);
            if(!errors.isEmpty()){
                return new LineMarkerInfo<>(psiElement,
                        psiElement.getTextRange(),
                        PluginIcons.ERROR,
                        Pass.EXTERNAL_TOOLS,
                        new TooltipProvider(getErrorsMessage(errors)),
                        null,
                        GutterIconRenderer.Alignment.LEFT);
            }
        }
        return null;
    }

    private String getErrorsMessage(Set<CogniCryptError> errors) {
        String s = "";
        for(CogniCryptError e : errors){
            s += e.getErrorMessage() +"\n";
        }
        return s;
    }

    //Returns line number for PSI element
    private int getLineNumber(PsiElement psiElement) {
        PsiFile containingFile = psiElement.getContainingFile();
        Project project = containingFile.getProject();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Document document = psiDocumentManager.getDocument(containingFile);
        int textOffset = psiElement.getTextOffset();

        return document.getLineNumber(textOffset);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }
}
