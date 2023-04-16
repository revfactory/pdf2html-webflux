package com.kakao.tech.pdf2html

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

@Controller
class FileUploadController {
    private val pdfToHTMLConverter = PDFToHTMLConverter()

    @GetMapping("/")
    fun index(): String {
        return "upload"
    }

    @PostMapping("upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun handleFileUpload(@RequestPart("file") file: Mono<FilePart>, model: Model): Mono<String> {
        return file.flatMap { part ->
            part.content().collectList().flatMap { bufferList ->
                val pdfContent = bufferList.asByteBuffer()
                val html = pdfToHTMLConverter.convertPDFToHTML(ByteArrayInputStream(pdfContent.array()))
                model.addAttribute("html", html)
                Mono.just("display")
            }
        }
    }

    private fun List<DataBuffer>.asByteBuffer(): ByteBuffer {
        val totalSize = this.map { it.readableByteCount() }.sum()
        val buffer = ByteBuffer.allocate(totalSize)
        this.forEach { dataBuffer ->
            val byteBuffer = dataBuffer.asByteBuffer()
            buffer.put(byteBuffer)
            DataBufferUtils.release(dataBuffer)
        }
        buffer.flip()
        return buffer
    }
}
