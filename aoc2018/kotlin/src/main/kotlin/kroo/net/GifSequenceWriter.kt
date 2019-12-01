package kroo.net

//
//  GifSequenceWriter.java
//
//  Created by Elliot Kroo on 2009-04-25.
//  Modified to Kotlin by Jon Bristow on 2018-12-12
//
// This work is licensed under the Creative Commons Attribution 3.0 Unported
// License. To view a copy of this license, visit
// http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
// Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.


import java.awt.image.RenderedImage
import java.io.File
import java.io.IOException
import javax.imageio.*
import javax.imageio.metadata.IIOMetadata
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.stream.ImageOutputStream

/**
 * Creates a new GifSequenceWriter
 *
 * @param outputStream the ImageOutputStream to be written to
 * @param imageType one of the imageTypes specified in BufferedImage
 * @param msBtwnFrames the time between frames in milliseconds
 * @param loopContinuously whether the gif should loop repeatedly
 * @throws IIOException if no gif ImageWriters are found
 *
 * @author Elliot Kroo (elliot[`at`]kroo[`dot`]net)
 */
class GifSequenceWriter(
    outputStream: ImageOutputStream,
    imageType: Int,
    msBtwnFrames: Int,
    loopContinuously: Boolean
) : AutoCloseable {
    private var gifWriter: ImageWriter
    private var imageWriteParam: ImageWriteParam
    private var imageMetaData: IIOMetadata

    init {
        // my method to create a writer
        gifWriter = writer
        imageWriteParam = gifWriter.defaultWriteParam

        imageMetaData = gifWriter.getDefaultImageMetadata(
            ImageTypeSpecifier.createFromBufferedImageType(imageType),
            imageWriteParam
        )

        val metaFormatName = imageMetaData.nativeMetadataFormatName

        val root = imageMetaData.getAsTree(metaFormatName) as IIOMetadataNode

        getNode(root, "GraphicControlExtension")
            .withAttribute("disposalMethod", "none")
            .withAttribute("userInputFlag", "FALSE")
            .withAttribute("transparentColorFlag", "FALSE")
            .withAttribute("delayTime", "${msBtwnFrames / 10}")
            .withAttribute("transparentColorIndex", "0")

        val appEntensionsNode = getNode(root, "ApplicationExtensions")

        val loop = if (loopContinuously) 0 else 1

        appEntensionsNode.appendChild(
            IIOMetadataNode("ApplicationExtension")
                .withAttribute("applicationID", "NETSCAPE")
                .withAttribute("authenticationCode", "2.0")
                .withUserObject(
                    byteArrayOf(
                        0x1,
                        (loop and 0xFF).toByte(),
                        (loop shr 8 and 0xFF).toByte()
                    )
                )
        )

        imageMetaData.setFromTree(metaFormatName, root)

        gifWriter.output = outputStream

        gifWriter.prepareWriteSequence(null)
    }

    @Throws(IOException::class)
    fun writeToSequence(img: RenderedImage) {
        gifWriter.writeToSequence(
            IIOImage(img, null, imageMetaData),
            imageWriteParam
        )
    }

    /**
     * Close this GifSequenceWriter object. This does not close the underlying
     * stream, just finishes off the GIF.
     */
    @Throws(IOException::class)
    override fun close() {
        gifWriter.endWriteSequence()
    }


    /**
     * Returns the first available GIF ImageWriter using
     * ImageIO.getImageWritersBySuffix("gif").
     *
     * @return a GIF ImageWriter object
     * @throws IIOException if no GIF image writers are returned
     */
    private val writer: ImageWriter
        @Throws(IIOException::class)
        get() = ImageIO.getImageWritersBySuffix("gif").let { iter ->
            require(iter.hasNext()) { "No GIF Image Writers Exist" }
            iter.next()
        }

    /**
     * Returns an existing child node, or creates and returns a new child node (if
     * the requested node does not exist).
     *
     * @param root the <tt>IIOMetadataNode</tt> to search for the child node.
     * @param nodeName the name of the child node.
     *
     * @return the child node, if found or a new node created with the given name.
     */
    private fun getNode(root: IIOMetadataNode, nodeName: String) =
        generateSequence(root.firstChild) { it.nextSibling }
            .find { it.nodeName.equals(nodeName, ignoreCase = true) }
            .let { matching ->
                when (matching) {
                    null -> root.appendChild(IIOMetadataNode(nodeName))
                    else -> matching
                } as IIOMetadataNode
            }


}

/**
 * public GifSequenceWriter(
 * BufferedOutputStream outputStream,
 * int imageType,
 * int timeBetweenFramesMS,
 * boolean loopContinuously) {
 *
 */
@Throws(Exception::class)
fun main(args: Array<String>) {
    if (args.size > 1) {
        // grab the output image type from the first image in the sequence
        val firstImage = ImageIO.read(File(args[0]))

        // create a new BufferedOutputStream with the last argument
        val output = FileImageOutputStream(File(args[args.size - 1]))

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        val writer =
            GifSequenceWriter(output, firstImage.type, 1, false)

        // write out the first image to our sequence...
        writer.writeToSequence(firstImage)
        for (i in 1 until args.size - 1) {
            val nextImage = ImageIO.read(File(args[i]))
            writer.writeToSequence(nextImage)
        }

        writer.close()
        output.close()
    } else {
        println(
            "Usage: java GifSequenceWriter [list of gif files] [output file]"
        )
    }
}

private fun IIOMetadataNode.withUserObject(userObjectBA: ByteArray): IIOMetadataNode {
    this.userObject = userObjectBA
    return this
}

private fun IIOMetadataNode.withAttribute(
    name: String,
    value: String
): IIOMetadataNode {
    setAttribute(name, value)
    return this
}
