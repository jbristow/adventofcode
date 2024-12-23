import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.EagerResult
import org.neo4j.driver.GraphDatabase
import util.AdventOfCode

object Day23 : AdventOfCode() {

    @JvmStatic
    fun main(args: Array<String>) {
        // println("\tPart 1: ${part1()}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
    fun part2(input: List<String>): String {
        val connections = input.flatMap { it.split("-").let { (a, b) -> listOf(a to b, b to a) } }
            .groupBy({ (a, _) -> a }, { (_, b) -> b }).mapValues { entry -> entry.value.sorted() }
        return connections.map { (c, cConnections) ->
            cConnections.fold(setOf(c)) { group, connectedToC ->
                if (group.none { groupElement -> connectedToC !in connections.getValue(groupElement) }) {
                    group + connectedToC
                } else {
                    group
                }
            }
        }.maxBy { it.size }.sorted().joinToString(",")
    }

    fun part1() {
        withNeo4j { driver ->

            deleteDb(driver)
            createDb(driver)

            val result = driver.executableQuery(
                """
               match (a)--(b)--(c)--(a) 
               where a.name < b.name 
               AND b.name < c.name 
               AND (
                 a.name starts with 't' 
                 OR b.name starts with 't' 
                 OR c.name starts with 't')
               RETURN COUNT(a) as n_rows
                """.trimIndent(),
            ).execute()
            println(result.records().first()["n_rows"].asInt())
        }
    }
    private fun deleteDb(driver: Driver) {
        driver.executableQuery("match (x) detach delete x").execute()
    }

    private fun createDb(driver: Driver): EagerResult? {
        driver.executableQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (c:Computer) REQUIRE (c.name) IS UNIQUE;").execute()
        driver.executableQuery("CREATE INDEX IF NOT EXISTS FOR (c:Computer) ON (c.name);").execute()
        val nodes = inputFileLines.flatMap { it.split("-") }.toSet().joinToString("\n") {
            "CREATE ($it:Computer {name:'$it'})"
        }
        val relationships = inputFileLines.map { it.split("-") }
            .joinToString(",\n") { (a, b) -> "($a)-[:CONNECTS_TO]->($b)" }
        return driver.executableQuery("$nodes\nCREATE\n$relationships").execute()
    }

    private fun <T> withNeo4j(withDb: (Driver) -> T) {
        val dbUri = "neo4j://localhost:7687"
        val dbUser = "neo4j"
        val dbPassword = "neo4j123"

        GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword)).use { driver ->
            driver.verifyConnectivity()

            withDb(driver)
        }
    }
}
