package no.nav.familie.ef.søknad.mellomlagring

import no.nav.familie.ef.søknad.excpetion.AttachmentConversionException
import no.nav.familie.ef.søknad.excpetion.AttachmentTypeUnsupportedException
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils.copyToByteArray
import java.io.ByteArrayOutputStream
import java.io.IOException

@Component
class Image2PDFConverter private constructor() {

    private val supportedMediaTypes: List<MediaType> = listOf(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG)

    @Throws(IOException::class)
    internal fun convert(resource: Resource): ByteArray {
        return convert(copyToByteArray(resource.inputStream))
    }

    fun convert(bytes: ByteArray): ByteArray {
        val mediaType = mediaType(bytes)
        return when {
            MediaType.APPLICATION_PDF == mediaType -> bytes
            validImageTypes(mediaType) -> embedImagesInPdf(mediaType.subtype, bytes)
            else -> throw AttachmentTypeUnsupportedException(mediaType)
        }
    }

    private fun validImageTypes(mediaType: MediaType): Boolean {
        val validImageTypes = supportedMediaTypes.contains(mediaType)
        LOG.info("{} konvertere bytes av type {} til PDF", if (validImageTypes) "Vil" else "Vil ikke", mediaType)
        return validImageTypes
    }

    companion object {

        private val A4 = PDRectangle.A4

        private val LOG = LoggerFactory.getLogger(Image2PDFConverter::class.java)

        private fun embedImagesInPdf(imgType: String, vararg images: ByteArray): ByteArray {
            return embedImagesInPdf(listOf(*images), imgType)
        }

        private fun embedImagesInPdf(images: List<ByteArray>, imgType: String): ByteArray {
            try {
                PDDocument().use { doc ->
                    ByteArrayOutputStream().use { outputStream ->
                        images.forEach { i -> addPDFPageFromImage(doc, i, imgType) }
                        doc.save(outputStream)
                        return outputStream.toByteArray()
                    }
                }
            } catch (ex: Exception) {
                throw AttachmentConversionException("Konvertering av vedlegg feilet", ex)
            }

        }

        private fun mediaType(bytes: ByteArray): MediaType {
            return MediaType.valueOf(Tika().detect(bytes))
        }

        private fun addPDFPageFromImage(doc: PDDocument, origImg: ByteArray, imgFormat: String) {
            val page = PDPage(A4)
            doc.addPage(page)
            val scaledImg = ImageScaler.downToA4(origImg, imgFormat)
            try {
                PDPageContentStream(doc, page).use { contentStream ->
                    val ximage = PDImageXObject.createFromByteArray(doc, scaledImg, "img")
                    contentStream.drawImage(ximage, A4.lowerLeftX.toInt().toFloat(), A4.lowerLeftY.toInt().toFloat())
                }
            } catch (ex: Exception) {
                throw AttachmentConversionException("Konvertering av vedlegg feilet", ex)
            }

        }
    }
}
