package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ErrorLineMarker implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {

        if (psiElement instanceof PsiExpression) {

            int lineNumber = getLineNumber(psiElement);

            //Check if an error exists for the line number that the element is located
            if (ErrorProvider.errorExists(lineNumber)) {

                psiElement = psiElement.getLastChild();

                return new LineMarkerInfo<PsiElement>(psiElement,
                        psiElement.getTextRange(),
                        PluginIcons.ERROR,
                        Pass.UPDATE_ALL,
                        new TooltipProvider(ErrorProvider.getError(lineNumber).toErrorMarkerString()),
                        null,
                        GutterIconRenderer.Alignment.LEFT);
            } else

                return null;
        }
        return null;
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
