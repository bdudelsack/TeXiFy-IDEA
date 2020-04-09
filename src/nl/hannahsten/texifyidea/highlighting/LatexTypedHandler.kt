package nl.hannahsten.texifyidea.highlighting

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import nl.hannahsten.texifyidea.file.LatexFile
import nl.hannahsten.texifyidea.psi.LatexInlineMath
import nl.hannahsten.texifyidea.psi.LatexTypes
import nl.hannahsten.texifyidea.settings.TexifySettings.Companion.getInstance

/**
 * @author Sten Wessel
 */
class LatexTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (file is LatexFile) {
            if (c == '$') {
                val caret = editor.caretModel
                val element = file.findElementAt(caret.offset)
                val parent = PsiTreeUtil.getParentOfType(element, LatexInlineMath::class.java) ?: return Result.CONTINUE
                val endOffset = parent.textRange.endOffset
                if (caret.offset == endOffset - 1) {
                    // Caret is at the end of the environment, so run over the closing $
                    caret.moveCaretRelatively(1, 0, false, false, true)
                    return Result.STOP
                }
            }
        }
        return Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file is LatexFile) {
            if (c == '$' && getInstance().automaticSecondInlineMathSymbol) {
                val tokenType = getTypedTokenType(editor)
                if (tokenType !== LatexTypes.COMMAND_TOKEN && tokenType !== LatexTypes.COMMENT_TOKEN && tokenType !== LatexTypes.INLINE_MATH_END) {
                    editor.document.insertString(
                            editor.caretModel.offset, c.toString())
                    return Result.STOP
                }
            }
            else if (c == '[') {
                return insertDisplayMathClose(editor)
            }
            else if (c == '(') {
                return insertRobustInlineMathClose(editor)
            }
        }
        return Result.CONTINUE
    }

    /**
     * Upon typing `\[`, inserts the closing delimiter `\]`.
     */
    private fun insertDisplayMathClose(editor: Editor): Result {
        val tokenType = getTypedTokenType(editor)
        if (tokenType === LatexTypes.DISPLAY_MATH_START) {
            // Checks if a bracket has already been inserted, if so: don't insert a 2nd one.
            val offset = editor.caretModel.offset
            val bracketHuh = editor.document.getText(TextRange.from(offset, 1))
            val insertString = "\\" + if ("]" == bracketHuh) "" else "]"
            editor.document.insertString(offset, insertString)
            return Result.STOP
        }
        return Result.CONTINUE
    }

    /**
     * Upon typing `\(`, inserts the closing delimiter `\)`.
     */
    private fun insertRobustInlineMathClose(editor: Editor): Result {
        val tokenType = getTypedTokenType(editor)
        if (tokenType === LatexTypes.INLINE_MATH_START) {
            // Only insert backslash because the closing parenthesis is already inserted by the PairedBraceMatcher.
            editor.document.insertString(editor.caretModel.offset, "\\")
            return Result.STOP
        }
        return Result.CONTINUE
    }

    /**
     * Retrieves the token type of the character just typed.
     */
    private fun getTypedTokenType(editor: Editor): IElementType {
        val caret = editor.caretModel.offset
        val highlighter = (editor as EditorEx).highlighter
        val iterator = highlighter.createIterator(caret - 1)
        return iterator.tokenType
    }
}