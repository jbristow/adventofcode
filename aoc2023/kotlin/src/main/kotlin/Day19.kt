import arrow.core.Ior
import arrow.core.bothIor
import arrow.core.leftIor
import arrow.core.rightIor
import arrow.optics.optics
import util.AdventOfCode

object Day19 : AdventOfCode() {
    @optics
    data class Rating(val x: Long, val m: Long, val a: Long, val s: Long) {
        fun sum(): Long {
            return x + m + a + s
        }

        companion object
    }

    sealed interface RuleOutput {
        data object Rejected : RuleOutput

        data object Accepted : RuleOutput

        data class NextRule(val name: String) : RuleOutput
    }

    sealed interface RuleTest {
        data object Lt : RuleTest

        data object Gt : RuleTest
    }

    sealed interface Rule {
        val output: RuleOutput

        data class Standard(val property: String, val testType: RuleTest, val value: Long, override val output: RuleOutput) : Rule {
            val accessor = property.toAccessor()
            val test =
                when (testType) {
                    RuleTest.Lt -> { r: Rating -> this.accessor(r) < value }
                    RuleTest.Gt -> { r: Rating -> this.accessor(r) > value }
                }

            fun split(currRatingRange: RatingRange): Pair<RatingRange, RatingRange> {
                val r = currRatingRange[this.property]

                val (newRtrue, newRfalse) =
                    when (testType) {
                        RuleTest.Lt -> r.first..<value to value..r.last

                        RuleTest.Gt -> (value + 1)..r.last to r.first..value
                    }

                return currRatingRange.update(property, newRtrue) to currRatingRange.update(property, newRfalse)
            }
        }

        data class Automatic(override val output: RuleOutput) : Rule

        fun run(r: Rating): RuleOutput? {
            return when (this) {
                is Automatic -> this.output
                is Standard ->
                    if (test(r)) {
                        this.output
                    } else {
                        null
                    }
            }
        }
    }

    data class Workflow(val name: String, val rules: List<Rule>) {
        fun run(r: Rating): RuleOutput {
            return rules.fold(null) { last: RuleOutput?, rule ->
                last ?: rule.run(r)
            } ?: throw Exception("No rules left for $this for rating $r")
        }
    }

    private val ruleRe = """(?<accessor>\w+)(?<test>[<>])(?<value>\d+):(?<outcome>\w+)""".toRegex()

    fun String.toAccessor(): (Rating) -> Long {
        return when (this) {
            "x" -> Rating.x::get
            "m" -> Rating.m::get
            "a" -> Rating.a::get
            "s" -> Rating.s::get
            else -> throw Exception("Could not parse $this to accessor.")
        }
    }

    private fun toRule(
        rawAccessor: String,
        rawTest: String,
        rawValue: String,
        rawOutcome: String,
    ): Rule {
        val test =
            when (rawTest) {
                "<" -> RuleTest.Lt
                ">" -> RuleTest.Gt
                else -> throw Exception("Cannot parse test $rawTest$rawValue")
            }
        val outcome =
            when (rawOutcome) {
                "R" -> RuleOutput.Rejected
                "A" -> RuleOutput.Accepted
                else -> RuleOutput.NextRule(rawOutcome)
            }
        return Rule.Standard(rawAccessor, test, rawValue.toLong(), outcome)
    }

    private tailrec fun Rating.process(
        workflow: Map<String, Workflow>,
        currentWorkflowName: String = "in",
    ): RuleOutput {
        val current = workflow.getValue(currentWorkflowName)
        return when (val output = current.run(this)) {
            is RuleOutput.NextRule -> this.process(workflow, output.name)
            else -> output
        }
    }

    private fun List<String>.toWorkflows(): List<Workflow> {
        return takeWhile { it.isNotBlank() }.map { line ->
            val name = line.takeWhile { c -> c != '{' }
            val rawRules = line.dropLast(1).dropWhile { c -> c != '{' }.drop(1).split(",")
            Workflow(
                name,
                rawRules.map {
                    if (it == "R") {
                        Rule.Automatic(RuleOutput.Rejected)
                    } else if (it == "A") {
                        Rule.Automatic(RuleOutput.Accepted)
                    } else {
                        val mr = ruleRe.matchEntire(it)

                        if (mr == null) {
                            Rule.Automatic(RuleOutput.NextRule(it))
                        } else {
                            toRule(
                                mr.groups["accessor"]!!.value,
                                mr.groups["test"]!!.value,
                                mr.groups["value"]!!.value,
                                mr.groups["outcome"]!!.value,
                            )
                        }
                    }
                },
            )
        }
    }

    private fun List<String>.toRatings(): List<Rating> {
        return dropWhile { it.isNotBlank() }.drop(1).map {
            it.drop(1).dropLast(1).split(",")
                .associate { rs ->
                    rs.split("=").let { (k, v) -> k to v.toLong() }
                }.let { map -> Rating(map.getValue("x"), map.getValue("m"), map.getValue("a"), map.getValue("s")) }
        }
    }

    private fun part1(input: List<String>): Long {
        val workflows = input.toWorkflows().associateBy { it.name }
        val results =
            input.toRatings().mapNotNull {
                when (it.process(workflows)) {
                    RuleOutput.Accepted -> it
                    else -> null
                }
            }

        return results.sumOf { it.sum() }
    }

    fun LongRange.size(): Long {
        return when (this.first) {
            -1L -> 0
            else -> (this.last - this.first) + 1
        }
    }

    data class RatingRange(
        val x: LongRange = 1..4000L,
        val m: LongRange = 1..4000L,
        val a: LongRange = 1..4000L,
        val s: LongRange = 1..4000L,
    ) {
        operator fun get(k: String): LongRange {
            return when (k) {
                "x" -> x
                "m" -> m
                "a" -> a
                "s" -> s
                else -> throw Exception("unknown key $k")
            }
        }

        fun update(
            k: String,
            v: LongRange,
        ): RatingRange {
            return when (k) {
                "x" -> copy(x = v)
                "m" -> copy(m = v)
                "a" -> copy(a = v)
                "s" -> copy(s = v)
                else -> throw Exception("unknown key $k")
            }
        }

        fun size() = x.size() * m.size() * a.size() * s.size()
    }

    fun part2(input: List<String>): Long {
        val workflows = input.toWorkflows()
        return WorkflowHandler(workflows.associate { it.name to it.rules }).crop()
    }

    class WorkflowHandler(private val workflows: Map<String, List<Rule>>) {
        private fun handleStandard(
            rule: Rule.Standard,
            sum: Long,
            rr: RatingRange,
        ): Ior<Long, RatingRange> {
            val (rrt, rrf) = rule.split(rr)
            val newSum: Long =
                sum +
                    when (val o = rule.output) {
                        RuleOutput.Accepted -> rrt.size()
                        is RuleOutput.NextRule -> crop(rrt, o.name)
                        else -> 0L
                    }
            return (newSum to rrf).bothIor()
        }

        private fun handleAuto(
            rule: Rule.Automatic,
            sum: Long,
            rr: RatingRange,
        ): Ior<Long, RatingRange> {
            return (
                sum +
                    when (val o = rule.output) {
                        RuleOutput.Accepted -> rr.size()
                        is RuleOutput.NextRule -> crop(rr, o.name)
                        else -> 0L
                    }
            ).leftIor()
        }

        fun crop(
            ratingRange: RatingRange = RatingRange(),
            wfName: String = "in",
        ): Long {
            return workflows.getValue(wfName)
                .fold(ratingRange.rightIor() as Ior<Long, RatingRange>) { ior, rule ->
                    ior.fold(
                        fa = { it.leftIor() },
                        fb = {
                            when (rule) {
                                is Rule.Standard -> handleStandard(rule, 0L, it)
                                is Rule.Automatic -> handleAuto(rule, 0L, it)
                            }
                        },
                        fab = { left, right ->
                            when (rule) {
                                is Rule.Standard -> handleStandard(rule, left, right)
                                is Rule.Automatic -> handleAuto(rule, left, right)
                            }
                        },
                    )
                }.leftOrNull() ?: 0L
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 19")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
