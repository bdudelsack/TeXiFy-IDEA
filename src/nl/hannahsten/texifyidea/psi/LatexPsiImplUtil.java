package nl.hannahsten.texifyidea.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.hannahsten.texifyidea.index.stub.LatexCommandsStub;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class is used for method injection in generated parser classes.
 * It has to be in Java for Grammar-Kit to be able to generate the parser classes correctly.
 */
public class LatexPsiImplUtil {

    /*
     * LatexCommands
     */

    /**
     * References which do not need a find usages to work on lower level psi elements (normal text) can be implemented on the command, otherwise they are in {@link LatexPsiImplUtil#getReference(LatexNormalText)}.
     * For more info and an example, see {@link nl.hannahsten.texifyidea.reference.LatexLabelParameterReference}.
     */
    @NotNull
    public static PsiReference[] getReferences(@NotNull LatexCommands element) {
        return LatexCommandsImplUtilKt.getReferences(element);
    }

    /**
     * Get the reference for this command, assuming it has exactly one reference (return null otherwise).
     */
    public static PsiReference getReference(@NotNull LatexCommands element) {
        PsiReference[] references = getReferences(element);
        if (references.length != 1) {
            return null;
        }
        else {
            return references[0];
        }
    }

    /**
     * Generates a list of all optional parameter names and values.
     */
    public static LinkedHashMap<String, String> getOptionalParameters(@NotNull LatexCommands element) {
        return LatexCommandsImplUtilKt.getOptionalParameters(element.getParameterList());
    }

    /**
     * Generates a list of all optional parameter names and values.
     */
    public static LinkedHashMap<String, String> getOptionalParameters(@NotNull LatexBeginCommand element) {
        return LatexCommandsImplUtilKt.getOptionalParameters(element.getParameterList());
    }

    /**
     * Generates a list of all names of all required parameters in the command.
     */
    public static List<String> getRequiredParameters(@NotNull LatexCommands element) {
        return LatexCommandsImplUtilKt.getRequiredParameters(element.getParameterList());
    }

    public static List<String> getRequiredParameters(@NotNull LatexBeginCommand element) {
        return LatexCommandsImplUtilKt.getRequiredParameters(element.getParameterList());
    }

    /**
     * Get the name of the command, for example \newcommand.
     */
    public static String getName(@NotNull LatexCommands element) {
        LatexCommandsStub stub = element.getStub();
        if (stub != null) return stub.getName();
        return element.getCommandToken().getText();
    }

    /**
     * Checks if the command is followed by a label.
     */
    public static boolean hasLabel(@NotNull LatexCommands element) {
        return LatexCommandsImplUtilKt.hasLabel(element);
    }

    /*
     * LatexEnvironment
     */

    public static String getLabel(@NotNull LatexEnvironment element) {
        return LatexEnvironmentUtilKt.getLabel(element);
    }

    public static String getEnvironmentName(@NotNull LatexEnvironment element) {
        return LatexEnvironmentUtilKt.getEnvironmentName(element);
    }

    /*
     * LatexNormalText
     */

    public static PsiReference[] getReferences(@NotNull LatexNormalText element) {
        return LatexNormalTextUtilKt.getReferences(element);
    }

    public static PsiReference getReference(@NotNull LatexNormalText element) {
        return LatexNormalTextUtilKt.getReference(element);
    }

    public static PsiElement getNameIdentifier(@NotNull LatexNormalText element) {
        return LatexNormalTextUtilKt.getNameIdentifier(element);
    }

    public static PsiElement setName(@NotNull LatexNormalText element, String name) {
        return LatexNormalTextUtilKt.setName(element, name);
    }

    public static String getName(@NotNull LatexNormalText element) {
        return LatexNormalTextUtilKt.getName(element);
    }
}