package com.kakao.tech.pdf2html

import org.apache.pdfbox.pdmodel.PDDocument
import org.fit.pdfdom.PDFDomTree
import java.io.IOException
import java.io.InputStream
import java.io.StringWriter
import javax.xml.parsers.ParserConfigurationException

class PDFToHTMLConverter {

    @Throws(IOException::class, ParserConfigurationException::class)
    fun convertPDFToHTML(pdfFile: InputStream): String {
        PDDocument.load(pdfFile).use { document ->
            val pdfDomTree = PDFDomTree()
            val sw = StringWriter()
            pdfDomTree.writeText(document, sw)
            return sw.toString()
        }
    }
}
