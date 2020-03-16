package nl.hannahsten.texifyidea.run

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import nl.hannahsten.texifyidea.run.latex.logtab.LatexLogMessage
import nl.hannahsten.texifyidea.run.latex.logtab.LatexOutputListener
import nl.hannahsten.texifyidea.run.latex.ui.LatexCompileMessageTreeView

class LatexOutputListenerTester : BasePlatformTestCase() {
    val TEST_LOG = """
(main.tex
LaTeX2e <2020-02-02> patch level 5
L3 programming layer <2020-03-06> (test/test.cls
Document Class: test/test 2019/12/19 test class
 (test/package/language.sty
) (test/package/titlepage.sty 
)
     ) 
)
    """.trimIndent()

    override fun getTestDataPath(): String {
        return "test/resources/run"
    }

    fun testRunLogParser() {
        val srcRoot = myFixture.copyDirectoryToProject("./", "./")
        val project = myFixture.project
        val mainFile = srcRoot.findFileByRelativePath("main.tex")
        val latexMessageList = mutableListOf<LatexLogMessage>()
        val bibtexMessageList = mutableListOf<LatexLogMessage>()
        val treeView = LatexCompileMessageTreeView(project)
        val listener = LatexOutputListener(project, mainFile, latexMessageList, bibtexMessageList, treeView)

        val input = TEST_LOG.split('\n')
        input.forEach { listener.processNewText(it) }

        latexMessageList.forEach { println(it) }
    }
}