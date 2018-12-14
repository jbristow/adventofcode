package utility

import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun BufferedImage.floodFill(rgb: Int): BufferedImage {
    repeat(width) { x ->
        repeat(height) { y ->
            setRGB(x, y, rgb)
        }
    }
    return this
}

fun BufferedImage.floodFill(color: Color): BufferedImage {
    graphics.color = color
    graphics.fillRect(0, 0, width, height)
    return this
}

fun BufferedImage.colorPixels(
    pixels: Iterable<Point>,
    rgb: Int
): BufferedImage {
    pixels.forEach {
        setRGB(it.x, it.y, rgb)
    }
    return this
}

fun BufferedImage.colorPixels(coloredPixels: Iterable<Pair<Point, Int>>): BufferedImage {
    coloredPixels.forEach { (it, rgb) ->
        setRGB(it.x, it.y, rgb)
    }
    return this
}


fun BufferedImage.drawImage(img: Image): BufferedImage {
    graphics.drawImage(
        img.getScaledInstance(width, height, Image.SCALE_FAST),
        0,
        0,
        null
    )
    return this
}

fun BufferedImage.resizeToWidth(newWidth: Int) =
    BufferedImage(
        newWidth,
        (height * (newWidth.toDouble() / width)).toInt(),
        BufferedImage.TYPE_INT_RGB
    ).drawImage(this)

fun BufferedImage.writePng(filename: String): BufferedImage {
    ImageIO.write(this, "png", File("$filename.png"))
    return this
}

