import java.nio.file.Files
import java.nio.file.Paths

object Day24 {
    /*
     * Units within a group all have the same hit points (amount of damage a
     * unit can take before it is destroyed), attack damage (the amount of
     * damage each unit deals), an attack type, an initiative (higher
     * initiative units attack first and win ties), and sometimes weaknesses or
     * immunities. Here is an example group:
     */
    enum class AttackType {
        Bludgeoning,
        Cold,
        Fire,
        Radiation,
        Slashing
    }

    enum class BattleGroupType {
        ImmuneSystem, Infection
    }

    data class BattleGroup(
        val hp: Int,
        var units: Int,
        val attackDamage: Int,
        val attackType: AttackType,
        val initiative: Int,
        val weakTo: List<AttackType>,
        val immuneTo: List<AttackType>,
        val type: BattleGroupType,
        val id: Int,
        val boost: Int = 0
    ) {
        override fun hashCode(): Int {
            return id.hashCode() + 7 * type.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return other is BattleGroup && other.type == type && other.id == id
        }
    }

    private fun String.toAttackType() = AttackType.valueOf(this)
    private fun String.parseWeakImmune(): Pair<List<AttackType>, List<AttackType>> {

        val split = this.trim('(', ')').split("; ")

        val weakStr = split.find { "weak to" in it } ?: ""
        val immuneStr = split.find { "immune to" in it } ?: ""

        return AttackType.values().filter { it.name in weakStr } to AttackType.values().filter { it.name in immuneStr }

    }

    private val BattleGroup.effectivePower: Int get() = units * (attackDamage + boost)


    private fun BattleGroup.potentialDamage(attacker: BattleGroup) =
        when (attacker.attackType) {
            in immuneTo -> 0L
            in weakTo -> 2L * attacker.effectivePower
            else -> 1L * attacker.effectivePower
        }

    private fun String.parseBattleGroup(
        type: BattleGroupType,
        id: Int,
        boost: Int = 0
    ): BattleGroup {
        //    hit points ((\([^)]\+\))?) with an attack that does (\d+) ([^\s]+) damage at initiative (\d+)
        val re =
            Regex("""(\d+) units each with (\d+) hit points (|(?:\(.*\))?) ?with an attack that does (\d+) (\w+) damage at initiative (\d+)""")

        val data = re.matchEntire(this)!!.groupValues.drop(1)

        val units = data[0].toInt()
        val hp = data[1].toInt()
        val (weakTo, immuneTo) = data[2].parseWeakImmune()
        val attackDamage = data[3].toInt()
        val attackType = data[4].toAttackType()
        val initiative = data[5].toInt()
        return BattleGroup(
            hp,
            units,
            attackDamage,
            attackType,
            initiative,
            weakTo,
            immuneTo,
            type,
            id + 1,
            boost
        )

    }

    @Suppress("unused")
    val sampleInput = """ImmuneSystem System:
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

infection:
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4""".lines()

    @JvmStatic
    fun main(args: Array<String>) {
        val input =
            Files.readAllLines(Paths.get("src/main/resources/day24.txt"))

        println("answer1: ${Day24.answer1(input)}")
        println("answer2: ${Day24.answer2(input)}")
    }

    private fun answer2(
        input: List<String>
    ): Any {

        val outcomes = generateSequence(1) { it + 1 }.map { boost ->
            val (immune, infected) =
                    input.joinToString("\n").split("\n\n").map {
                        it.lines().drop(1)
                    }.let { (a, b) ->
                        a.mapIndexed { i, s ->
                            s.parseBattleGroup(
                                BattleGroupType.ImmuneSystem,
                                i,
                                boost
                            )
                        } to b.mapIndexed { i, s ->
                            s.parseBattleGroup(
                                BattleGroupType.Infection, i
                            )
                        }
                    }

            val outcome = fight(
                0, (immune + infected).toSet()
            )
            println("units left after boost $boost: ${outcome.sumBy { it.units }}")
            outcome.first().type to outcome.sumBy { it.units }
        }
//
        return outcomes.dropWhile { it.first == BattleGroupType.Infection }
            .first().second
    }

    private fun answer1(
        input: List<String>
    ): Any {

        val (immune, infected) =
                input.joinToString("\n").split("\n\n").map {
                    it.lines().drop(1)
                }.let { (a, b) ->
                    a.mapIndexed { i, s ->
                        s.parseBattleGroup(
                            BattleGroupType.ImmuneSystem,
                            i
                        )
                    } to b.mapIndexed { i, s ->
                        s.parseBattleGroup(
                            BattleGroupType.Infection, i
                        )
                    }
                }

        val outcome = fight(
            0, (immune + infected).toSet()
        )
        return outcome.sumBy { it.units }
    }


    private tailrec fun fight(
        round: Int,
        groups: Set<BattleGroup>
    ): Collection<BattleGroup> {
//        println("round $round, ${groups.filter { it.type == BattleGroupType.Infection }.sumBy { it.units }} infection vs ${groups.filter { it.type == BattleGroupType.Immune }.sumBy { it.units }} immune")
        if (groups.isEmpty()) {
            println("EVERYONE DEAD")
            return groups
        } else if (groups.none { groups.first().type != it.type }) {
            println("Victory: ${groups.first().type}")
            return groups
        }
        val count = groups.sumBy { it.units }

        val groupToTarget = targetSelection(
            groups
                .sortedByDescending { it.initiative }
                .sortedByDescending { it.effectivePower },
            groups
                .sortedByDescending { it.initiative }
                .sortedByDescending { it.effectivePower },
            emptyList(),
            emptyList()
        )

        val survivors =
            attack(
                groups,
                groupToTarget.sortedByDescending { it.first.initiative }
            )


        if (count == survivors.sumBy { it.units }) {
            println("STALEMATE")
            return survivors.sortedByDescending { it.type }
        }
        return fight(round + 1, survivors)
    }

    private tailrec fun attack(
        groups: Set<BattleGroup>,
        groupsToTarget: List<Pair<BattleGroup, BattleGroup?>>
    ): Set<BattleGroup> {
        if (groupsToTarget.isEmpty()) {
            return groups.filterNot { it.units < 1 }.toSet()
        }

        val (current, target) = groupsToTarget.first()


        if (target == null || current.units < 1) {
            return attack(groups, groupsToTarget.drop(1))
        }

//        println(
//            "${current.type} group ${current.id} attacks defending group ${target.id}, killing ${target.potentialDamage(
//                current
//            ) / target.hp}/${target.units} units"
//        )

        target.units -= (target.potentialDamage(current)).toInt() / target.hp

        return attack(groups, groupsToTarget.drop(1))


    }

    private tailrec fun targetSelection(
        groups: List<BattleGroup>,
        targets: List<BattleGroup>,
        groupToTarget: List<Pair<BattleGroup, BattleGroup?>>,
        seen: List<BattleGroup>
    ): List<Pair<BattleGroup, BattleGroup?>> {

        if (groups.isEmpty()) {
            return groupToTarget
        }

        val current = groups.sortedWith(Comparator { a, b ->
            if (a.effectivePower == b.effectivePower) {
                -1 * a.initiative.compareTo(b.initiative)
            } else {
                -1 * a.effectivePower.compareTo(b.effectivePower)
            }
        }).first()
        val remaining = groups.drop(1)

        val potentialTargets = targets
            .filterNot {
                it.type == current.type || it.potentialDamage(current) < 1
            }
            .sortedWith(Comparator { a, b ->
                if (a.potentialDamage(current) == b.potentialDamage(current)) {
                    if (a.effectivePower == b.effectivePower) {
                        -1 * a.initiative.compareTo(b.initiative)
                    } else {
                        -1 * a.effectivePower.compareTo(b.effectivePower)
                    }
                } else {
                    -1 * a.potentialDamage(current).compareTo(
                        b.potentialDamage(
                            current
                        )
                    )
                }

            })

        val target =
            potentialTargets
                .firstOrNull()
//        println(
//            "${current.type} group ${current.id} would deal defending group ${target?.id} (${target?.hp}) ${target?.potentialDamage(
//                current
//            )} damage"
//        )
        if (target != null && target.potentialDamage(current) > 0) {
            return targetSelection(
                remaining,
                targets.filterNot { t ->
                    (target == t)
                },
                groupToTarget + (current to target),
                seen + current
            )
        } else {
            return targetSelection(
                remaining,
                targets,
                groupToTarget,
                seen + current
            )
        }
    }
}





