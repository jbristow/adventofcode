import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
internal class Day07Test {
    private val cluster: Cluster = Cluster.build()
        .addContactPoint("localhost")
        .port(8182)
        .create()

    private val g = EmptyGraph.instance().traversal()
        .withRemote(DriverRemoteConnection.using(cluster))

    private val input =
        File("src/main/resources/day07.txt").readLines()
            .map(String::parseInstruction)
    private val data =
        """Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.""".lines()
            .map(String::parseInstruction)

    @AfterAll
    fun teardown() {
        cluster.close()
    }


    @Nested
    inner class Part1 {
        @Test
        fun sample() {
            assertEquals("CABDFE", Day07.answer1(g, data))
        }

        @Test
        fun answer() {
            assertEquals("HPDTNXYLOCGEQSIMABZKRUWVFJ", Day07.answer1(g, input))
        }
    }

    @Nested
    inner class Part2 {
        @Test
        fun sample() {
            assertEquals(15, Day07.answer2(g, data, 1L, 0))
        }

        @Test()
        fun answer() {
            assertEquals(908, Day07.answer2(g, input, 5L, 60))
        }
    }
}