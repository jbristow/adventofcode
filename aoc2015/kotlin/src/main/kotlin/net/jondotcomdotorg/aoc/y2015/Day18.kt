package net.jondotcomdotorg.aoc.y2015

import util.AdventOfCode
import util.Point2d

object Day18 : AdventOfCode() {
    
    @JvmStatic
    fun main(args: Array<String>) {
        println("Day 17")
        println("\tPart 1: ${part1(inputFileLineSequence)}")
        println("\tPart 2: ${part2(inputFileLineSequence)}")
    }

    private fun Sequence<String>.toLightMap(): Map<Point2d, Boolean> {
        return flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                Point2d(x, y) to (c == '#')
            }
        }.toMap()
    }

    private fun part1(input: Sequence<String>): Int {
        val lights = input.toLightMap()

        val finalLights = step(lights, 100)

        return finalLights.count { (_, v) -> v }
    }

    private fun neighbors(p: Point2d): Set<Point2d> {
        return setOf(
            Point2d(p.x - 1, p.y - 1),
            Point2d(p.x - 1, p.y),
            Point2d(p.x - 1, p.y + 1),
            Point2d(p.x, p.y - 1),
            Point2d(p.x, p.y + 1),
            Point2d(p.x + 1, p.y - 1),
            Point2d(p.x + 1, p.y),
            Point2d(p.x + 1, p.y + 1)
        )
    }

    private tailrec fun step(lights: Map<Point2d, Boolean>, left: Int): Map<Point2d, Boolean> {
        if (left == 0) {
            return lights
        }

        return step(
            lights.mapValues { (p, lit) ->

                val neighbors = neighbors(p).filter { it in lights }.count { lights.getValue(it) }
                (lit && neighbors == 2) || neighbors == 3
            },
            left - 1
        )
    }

    private tailrec fun stepBroken(
        lights: Map<Point2d, Boolean>,
        xMax: Int,
        yMax: Int,
        left: Int
    ): Map<Point2d, Boolean> {
        if (left == 0) {
            return lights
        }

        return stepBroken(
            lights.mapValues { (p, lit) ->
                val neighbors = neighbors(p).filter { it in lights }.count { lights.getValue(it) }
                (lit && neighbors == 2) ||
                    neighbors == 3 ||
                    p == Point2d(0, 0) ||
                    p == Point2d(xMax, 0) ||
                    p == Point2d(0, yMax) ||
                    p == Point2d(xMax, yMax)
            },
            xMax,
            yMax,
            left - 1
        )
    }

    private fun part2(input: Sequence<String>): Int {
        val lights = input.toLightMap()

        val xMax = lights.keys.maxOf { it.x }
        val yMax = lights.keys.maxOf { it.y }

        val brokenLights = lights + mapOf(
            Point2d(0, 0) to true,
            Point2d(0, yMax) to true,
            Point2d(xMax, 0) to true,
            Point2d(xMax, yMax) to true
        )

        val finalLights = stepBroken(brokenLights, xMax, yMax, 100)

        return finalLights.count { (_, v) -> v }
    }
}
