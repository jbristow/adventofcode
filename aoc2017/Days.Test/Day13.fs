module Tests.Day13

open System
open NUnit.Framework
open Swensen.Unquote
open Day13

[<TestFixture>]
module Samples =
    let sampleData = [| "0: 3"; "1: 2"; "4: 4"; "6: 4" |]

    [<Test>]
    let part1() = totalSeverity sampleData =! 24

    [<Test>]
    let part2() = findFirstZeroSev sampleData =! 10

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllLines("day13.txt")

    [<Test>]
    let answerPart1() = totalSeverity data =! 2688

    [<Test>]
    let answerPart2() = findFirstZeroSev data =! 3876272
