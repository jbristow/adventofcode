import arrow.core.extensions.list.monad.ap
import arrow.optics.optics
import java.nio.file.Files
import java.nio.file.Paths

data class ElementPiece(val amount: Int, val id: String)

fun String.toElementPiece() =
    split(" ").let { ElementPiece(it[0].toInt(), it[1]) }


@optics
data class Material(val amount: Int, val name: String) {
    companion object
}

data class Reaction(val list: List<Material>, val material: Material)
data class Edge(val a: String, val b: String)

object Day14 {

    private const val FILENAME = "src/main/resources/day14.txt"
    private val fileData = Files.readAllLines(Paths.get(FILENAME))

}


fun main() {
//    Day14.part1()
}