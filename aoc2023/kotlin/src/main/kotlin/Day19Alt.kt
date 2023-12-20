import arrow.core.*
import arrow.optics.optics
import util.AdventOfCode

object Day19Alt : AdventOfCode("day19.txt") {

    sealed interface RuleTest {
        data object Lt : RuleTest
        data object Gt : RuleTest
    }

    sealed interface RuleOutput {
        data object Accepted : RuleOutput
        data object Rejected : RuleOutput
        data class Rule(val name: String) : RuleOutput
    }

    fun LongRange.size(): Long {
        return when (this.first) {
            -1L -> 0
            else -> (this.last - this.first) + 1
        }

    }

    sealed interface Rule {


        val output: RuleOutput

        data class Automatic(override val output: RuleOutput) : Rule
        data class Standard(val property: String, val test: RuleTest, val value: Long, override val output: RuleOutput) : Rule {

            fun split(currRatingRange: RatingRange): Pair<RatingRange, RatingRange> {
                val r = currRatingRange[this.property]

                val (newRtrue, newRfalse) = when (test) {

                    RuleTest.Lt -> r.first..<value to value..r.last

                    RuleTest.Gt -> (value + 1)..r.last to r.first..value
                }

                return currRatingRange.update(property, newRtrue) to currRatingRange.update(property, newRfalse)
            }
        }
    }

    @optics
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

        fun update(k: String, v: LongRange): RatingRange {
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

        val workflows = input.takeWhile { it.isNotBlank() }.associate { line ->
            val name = line.takeWhile { it != '{' }
            val data = line.drop(name.length + 1).dropLast(1).split(",")

            name to data.map { raw ->
                when (raw) {
                    "A" -> Rule.Automatic(RuleOutput.Accepted)
                    "R" -> Rule.Automatic(RuleOutput.Rejected)
                    else -> {
                        when {
                            ":" !in raw -> Rule.Automatic(RuleOutput.Rule(raw))
                            else -> {
                                val (test, dest) = raw.split(":")
                                val (prop, value) = test.split("<", ">")
                                Rule.Standard(
                                    prop,
                                    if (">" in test) {
                                        RuleTest.Gt
                                    } else {
                                        RuleTest.Lt
                                    },
                                    value.toLong(),
                                    when (dest) {
                                        "A" -> RuleOutput.Accepted
                                        "R" -> RuleOutput.Rejected
                                        else -> RuleOutput.Rule(dest)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }

        return WorkflowHandler(workflows).crop()

    }


    class WorkflowHandler(private val workflows: Map<String, List<Rule>>) {

        fun handleStandard(rule: Rule.Standard, sum: Long, rr: RatingRange): Ior<Long, RatingRange> {
            val (rrt, rrf) = rule.split(rr)
            val newSum: Long = sum + when (val o = rule.output) {
                RuleOutput.Accepted -> rrt.size()
                is RuleOutput.Rule -> crop(rrt, o.name)
                else -> 0L
            }
            return (newSum to rrf).bothIor()
        }

        fun handleAuto(rule: Rule.Automatic, sum: Long, rr: RatingRange): Ior<Long, RatingRange> {
            return (sum + when (val o = rule.output) {
                RuleOutput.Accepted -> rr.size()
                is RuleOutput.Rule -> crop(rr, o.name)
                else -> 0L
            }).leftIor()
        }

        fun crop(ratingRange: RatingRange = RatingRange(), wfName: String = "in"): Long {
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
        println(part2(inputFileLines))
    }
}
