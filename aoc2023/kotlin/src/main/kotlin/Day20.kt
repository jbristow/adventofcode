import util.AdventOfCode
import util.Maths.lcm

object Day20 : AdventOfCode() {
    sealed interface Pulse {
        data object High : Pulse
        data object Low : Pulse
    }

    sealed interface Module {
        var sent: Long
        val dests: List<String>
        fun receive(impulse: Impulse): Map<String, Pulse>

        data class Broadcaster(override val dests: List<String>) : Module {
            override var sent = 0L
            override fun receive(impulse: Impulse): Map<String, Pulse> {
                sent += 1
                return dests.associateWith { impulse.pulse }
            }
        }

        data class FlipFlop(override val dests: List<String>, var state: Boolean = false) : Module {
            override var sent = 0L
            override fun receive(impulse: Impulse): Map<String, Pulse> {
                sent += 1
                val newState = if (impulse.pulse==Pulse.Low) {
                    !this.state
                } else {
                    this.state
                }
                if (newState==this.state) {
                    return mapOf()
                } else {
                    this.state = newState
                    return when {
                        state -> Pulse.High
                        else -> Pulse.Low
                    }.let { p -> dests.associateWith { p } }
                }

            }
        }

        class Conjunction(override val dests: List<String>, inputs: List<String>) : Module {
            val history: MutableMap<String, Pulse> = inputs.associate { it to Pulse.Low }.toMutableMap()
            var reported = false
            var canReport = false
            override var sent = 0L
            override fun receive(impulse: Impulse): Map<String, Pulse> {
                history[impulse.from] = impulse.pulse
                sent += 1
                return if (history.all { (_, v) -> v is Pulse.High }) {
                    canReport = true
                    Pulse.Low
                } else {
                    canReport = false
                    Pulse.High
                }.let { p -> dests.associateWith { p } }

            }
        }


    }

    fun List<String>.toModules(): Map<String, Module> {
        val sourceToDest = this.associate { line ->
            val (source, dests) = line.dropWhile { c -> !c.isLetter() }.split(" -> ")
            source to dests.split(""",\s+""".toRegex())
        }
        val destToSource = sourceToDest.flatMap { (k, v) -> v.map { it to k } }.groupBy({ it.first }, { it.second })

        return this.associate { line ->
            val source = line.split(" -> ").first()
            val name = source.drop(1)
            when {
                '&'==source.first() -> name to Module.Conjunction(sourceToDest.getValue(name), destToSource.getValue(name))
                '%'==source.first() -> name to Module.FlipFlop(sourceToDest.getValue(name))
                "broadcaster"==source -> source to Module.Broadcaster(sourceToDest.getValue(source))
                else -> throw Exception("Could not parse $source")
            }
        }
    }


    private fun part1(input: List<String>): Long {
        val modules = input.toModules()

        return (0..<1000).fold(0L to 0L) { acc, curr ->
            val (low, high) = simulate(modules)
            (acc.first + low) to (acc.second + high)
        }.let { (low, high) -> low * high }
    }

    data class Impulse(val from: String, val to: String, val pulse: Pulse) {
        override fun toString(): String {
            return "$from -$pulse-> $to"
        }
    }

    private tailrec fun simulate(
        modules: Map<String, Module>,
        pulseList: List<Impulse> = listOf(Impulse("button", "broadcaster", Pulse.Low)),
        lowPulses: Long = 0,
        highPulses: Long = 0,

        ): Pair<Long, Long> {
        if (pulseList.isEmpty()) {
            return lowPulses to highPulses
        }
        val currImpulse = pulseList.first()
        val currModule = modules[currImpulse.to]
        val result = currModule?.receive(currImpulse)?.map { (k, v) -> Impulse(currImpulse.to, k, v) } ?: listOf()

        val (low, high) = when (currImpulse.pulse) {
            Pulse.High -> 0 to 1
            Pulse.Low -> 1 to 0
        }

        return simulate(modules, pulseList.drop(1) + result, lowPulses + low, highPulses + high)
    }

    private tailrec fun simulateEscape(
        modules: Map<String, Module>,
        target: String,
        buttonPresses: Long,
        found: MutableList<Long> = mutableListOf(),
        pulseList: List<Impulse> = listOf(Impulse("button", "broadcaster", Pulse.Low)),
    ): Pair<Boolean, MutableList<Long>> {
        if (pulseList.isEmpty()) {
            return false to found
        }
        val currImpulse = pulseList.first()
        if (currImpulse.pulse==Pulse.Low && currImpulse.to==target) {
            return true to found
        }
        val currModule = modules[currImpulse.to]

        val result = currModule?.receive(currImpulse)?.map { (k, v) -> Impulse(currImpulse.to, k, v) } ?: listOf()

        if (currModule is Module.Conjunction && currModule.canReport && !currModule.reported) {
            currModule.reported = true
            found.add(buttonPresses)
        }


        return simulateEscape(modules, target, buttonPresses, found, pulseList.drop(1) + result)
    }

    tailrec fun simulateWithTarget(
        modules: Map<String, Module>,
        target: String,
        excludable: Set<String>,
        found: MutableList<Long> = mutableListOf(),
        buttonPresses: Long = 0,
    ): Long {

        val nextButtonPresses = buttonPresses + 1

        val (done, nextFound) = simulateEscape(modules, target, nextButtonPresses, found)
        // I generated my input with graphviz and noticed that everything feeds into kj, and kj only fires once everything feeds into it.
        // We can sortof generalize and hope here... find the conjunction that feeds rx and lcm the cycle time of its inputs
        return if (modules.filterKeys { it !in excludable }.values.filterIsInstance<Module.Conjunction>().all { m -> m.reported }) {
            nextFound.reduce { a, b -> lcm(a, b) }
        } else if (done) {
            nextButtonPresses
        } else {
            simulateWithTarget(modules, target, excludable, nextFound, nextButtonPresses)
        }
    }

    private fun part2(input: List<String>): Long {
        val modules = input.toModules()

        val excludable = modules.filterValues { it is Module.Conjunction && "rx" in it.dests }.keys
        return simulateWithTarget(modules, "rx", excludable)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 20")
        println("\tPart 1: ${part1(inputFileLines)}")
        println("\tPart 2: ${part2(inputFileLines)}")
    }
}
