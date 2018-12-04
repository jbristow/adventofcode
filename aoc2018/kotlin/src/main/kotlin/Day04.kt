import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

typealias Duration = Pair<LocalDateTime, LocalDateTime>

private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun String.parseEntry() =
    Regex("""\[(.*)] (.*)""").matchEntire(this)!!.groupValues.let {
        LocalDateTime.parse(it[1], dateFormat) to it[2]
    }

fun String.parseGuardId() =
    Regex("""Guard #(\d+) begins shift""")
        .matchEntire(this)!!
        .groupValues[1]
        .toInt()


fun Duration.minutes() = let { (start, end) ->
    ((end.toEpochSecond(UTC) - start.toEpochSecond(UTC)) / 60).toInt()
}

fun Duration.minuteRange() = let { (start, end) ->
    start.minute until end.minute
}


object Day04 {
    fun answer1(input: List<Guard>): Int {
        val sleepiest = input.maxBy {
            it.sleepLog.sumBy(Duration::minutes)
        }!!

        val minuteMostOftenAsleep = sleepiest.sleepLog
            .flatMap(Duration::minuteRange)
            .frequencies()
            .maxBy { (_, v) -> v }!!.key

        return sleepiest.id * minuteMostOftenAsleep
    }

    private fun computeGuardSchedules(initial: List<Pair<LocalDateTime, String>>): List<Guard> {
        val guards = mutableMapOf<Int, Guard>()
        initial.sortedBy { it.first }.fold(-1) { onDuty, (ts, type) ->
            val guard = when (type) {
                "wakes up" -> guards[onDuty]?.awaken(ts)
                "falls asleep" -> guards[onDuty]?.asleepen(ts)
                else -> type.parseGuardId().let { gid ->
                    if (gid !in guards) {
                        guards[gid] = Guard(gid)
                    }
                    guards[gid]!!.ondutyen(ts)
                }
            }!!
            guard.id
        }
        return guards.values.toList()
    }

    fun answer2(input: List<Guard>) =
        input.map {
            it.id to (
                    it.sleepLog
                        .flatMap(Duration::minuteRange)
                        .frequencies()
                        .toList()
                        .sortedBy { (k, v) -> -v }
                        .firstOrNull()
                    )
        }.sortedBy { (k, v) ->
            -(v?.second ?: -10000)
        }.first().let {
            it.first * it.second!!.first
        }


    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day04.txt").readLines()
        val guardSchedules = computeGuardSchedules(input.map(String::parseEntry))
        println("answer 1: ${answer1(guardSchedules)}")
        println("answer 2: ${answer2(guardSchedules)}")
    }
}

class Guard(val id: Int) {
    private var lastStatusChange: LocalDateTime? = null
    val sleepLog = mutableListOf<Duration>()

    fun awaken(ts: LocalDateTime): Guard {
        sleepLog.add(lastStatusChange!! to ts)
        lastStatusChange = ts
        return this
    }


    fun asleepen(ts: LocalDateTime): Guard {
        lastStatusChange = ts
        return this
    }

    fun ondutyen(ts: LocalDateTime): Guard {
        lastStatusChange = ts
        return this
    }
}


