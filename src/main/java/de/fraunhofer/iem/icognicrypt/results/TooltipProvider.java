package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.psi.PsiElement;
import com.intellij.util.Function;

public class TooltipProvider implements Function<PsiElement, String> {

    private final String toolTip;

    TooltipProvider(String text) {
        toolTip = text;
    }

    public String fun(PsiElement psiElement) {
        return toolTip;
    }
}