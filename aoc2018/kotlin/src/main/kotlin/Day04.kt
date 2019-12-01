import utility.frequencies
import utility.sortedByValueDescending
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

typealias Duration = Pair<LocalDateTime, LocalDateTime>
typealias LogEntry = Pair<LocalDateTime, String>

val LogEntry.timestamp get() = first
val LogEntry.type get() = second

private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun String.parseLogEntry() =
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

private fun computeGuardSchedules(initial: List<String>) =
    initial.map(String::parseLogEntry)
        .sortedBy(LogEntry::timestamp)
        .fold(-1 to mutableMapOf<Int, Guard>()) { (onDuty, guards), logEntry ->
            when (logEntry.type) {
                "wakes up" -> guards[onDuty]?.awaken(logEntry.timestamp)
                "falls asleep" -> guards[onDuty]?.asleepen(logEntry.timestamp)
                else -> logEntry.type.parseGuardId().let { gid ->
                    if (gid !in guards) {
                        guards[gid] = Guard(gid)
                    }
                    guards[gid]!!.ondutyen(logEntry.timestamp)
                }
            }!!.id to guards
        }.second.values.toList()

class Guard(val id: Int) {
    private var lastStatusChange: LocalDateTime? = null
    val onDuty = mutableListOf<LocalDateTime>()
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
        onDuty.add(ts)
        return this
    }

    val minuteMostOftenAsleep by lazy {
        sleepLog.flatMap(Duration::minuteRange)
            .frequencies()
            .sortedByValueDescending()
            .firstOrNull()
    }
}


object Day04 {
    fun answer1(input: List<Guard>) = input.maxBy {
        it.sleepLog.sumBy(Duration::minutes)
    }!!.let { sleepiest ->
        sleepiest.id * sleepiest.minuteMostOftenAsleep!!.first
    }


    fun answer2(input: List<Guard>) =
        input.maxBy {
            it.minuteMostOftenAsleep?.second ?: -1000
        }.let {
            it!!.id * it.minuteMostOftenAsleep!!.first
        }


    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("src/main/resources/day04.txt").readLines()
        val guardSchedules = computeGuardSchedules(input)
        println("answer 1: ${answer1(guardSchedules)}")
        println("answer 2: ${answer2(guardSchedules)}")
        output(guardSchedules)
    }

    private fun output(guardSchedules: List<Guard>) {
        val whenOnDuty = guardSchedules.flatMap { g ->
            g.onDuty.map { ldt ->
                ldt
            }
        }.sorted()


        val maxOnDuty = guardSchedules.map { g ->
            g.onDuty.max()!!
        }.max()
        val minOnDuty = guardSchedules.map { g ->
            g.onDuty.min()!!
        }.min()

        val indent = "                 "
        val minutes = (0L until 60)
        val minuteHeaders =
            (0..3).joinToString("\n") { row -> indent + minutes.map { "%04d".format(it) }.joinToString("") { "${it[row]}" } }
        println(
            minuteHeaders
        )


        val out =
            generateSequence(minOnDuty!!.toLocalDate()) { it.plusDays(1) }
                .takeWhile { it <= maxOnDuty!!.toLocalDate() }
                .joinToString("\n") { ld ->
                    guardSchedules
                        .filter { g ->
                            g.sleepLog.any { (a, b) ->
                                (ld.atStartOfDay() <= a && a < ld.atStartOfDay().plusDays(1))
                                        || (ld.atStartOfDay() <= b && b < ld.atStartOfDay().plusDays(1))
                            }
                        }
                        .joinToString("\n") { g ->
                            ld.format(DateTimeFormatter.ISO_DATE) + " %5s ".format("#${g.id}") + minutes.joinToString("") { m ->
                                ld.atStartOfDay().plusMinutes(m).let { ldt ->
                                    if (g.sleepLog.any { (s, e) ->
                                            s <= ldt && ldt < e
                                        }) {
                                        "z"
                                    } else {
                                        "."
                                    }
                                }
                            }
                        }.ifEmpty { ld.format(DateTimeFormatter.ISO_DATE) + "  none " + ".".repeat(60) }
                }
        println(out)


    }
}
