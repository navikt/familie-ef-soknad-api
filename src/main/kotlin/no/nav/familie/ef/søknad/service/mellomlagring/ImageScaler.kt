package no.nav.familie.ef.søknad.service.mellomlagring

import no.nav.familie.ef.søknad.exception.AttachmentConversionException
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.AffineTransformOp.TYPE_BILINEAR
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_CUSTOM
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

object ImageScaler {

    private val LOG = LoggerFactory.getLogger(ImageScaler::class.java)

    fun downToA4(origImage: ByteArray, format: String): ByteArray {
        val a4 = PDRectangle.A4

        try {
            var image = ImageIO.read(ByteArrayInputStream(origImage))

            image = rotatePortrait(image)

            val pdfPageDim = Dimension(a4.width.toInt(), a4.height.toInt())
            val origDim = Dimension(image.width, image.height)
            val newDim = getScaledDimension(origDim, pdfPageDim)

            return if (newDim == origDim) {
                origImage
            } else {
                val scaledImg = scaleDown(image, newDim)
                toBytes(scaledImg, format)
            }
        } catch (ex: IOException) {
            throw AttachmentConversionException("Konvertering av vedlegg feilet", ex)
        }

    }

    private fun rotatePortrait(image: BufferedImage): BufferedImage {
        if (image.height >= image.width) {
            return image
        }
        if (image.type == TYPE_CUSTOM) {
            LOG.warn("Kan ikke rotere bilde med ukjent type")
            return image

        }

        var rotatedImage = BufferedImage(image.height, image.width, image.type)
        val transform = AffineTransform()
        transform.rotate(Math.toRadians(90.0), (image.height / 2f).toDouble(), (image.height / 2f).toDouble())
        val op = AffineTransformOp(transform, TYPE_BILINEAR)
        rotatedImage = op.filter(image, rotatedImage)
        return rotatedImage
    }

    private fun getScaledDimension(imgSize: Dimension, a4: Dimension): Dimension {
        val originalWidth = imgSize.width
        val originalHeight = imgSize.height
        val a4Width = a4.width
        val a4Height = a4.height
        var newWidth = originalWidth
        var newHeight = originalHeight

        if (originalWidth > a4Width) {
            newWidth = a4Width
            newHeight = newWidth * originalHeight / originalWidth
        }

        if (newHeight > a4Height) {
            newHeight = a4Height
            newWidth = newHeight * originalWidth / originalHeight
        }

        return Dimension(newWidth, newHeight)
    }

    private fun scaleDown(origImage: BufferedImage, newDim: Dimension): BufferedImage {
        val newWidth = newDim.getWidth().toInt()
        val newHeight = newDim.getHeight().toInt()
        val tempImg = origImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
        val scaledImg = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR)
        val g = scaledImg.graphics as Graphics2D
        g.drawImage(tempImg, 0, 0, null)
        g.dispose()
        return scaledImg
    }

    @Throws(IOException::class)
    private fun toBytes(img: BufferedImage, format: String): ByteArray {
        ByteArrayOutputStream().use { baos ->
            ImageIO.write(img, format, baos)
            return baos.toByteArray()
        }
    }

}
