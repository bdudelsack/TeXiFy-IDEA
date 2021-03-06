package nl.hannahsten.texifyidea.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import nl.hannahsten.texifyidea.psi.LatexNormalText

/**
 * This class is used to enable inline refactoring.
 */
class LatexRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        // Label parameters are NormalText
        return element is LatexNormalText
    }
}