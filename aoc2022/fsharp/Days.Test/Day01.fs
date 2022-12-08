module Tests.Day01

open System
open NUnit.Framework
open Swensen.Unquote
open Day01

[<TestFixture>]
module Answers =
    [<Test>]
    let part1 () =
        let data: string = System.IO.File.ReadAllText("day01.txt")
        test <@ elfCarryingMost data = 64929 @>

    [<Test>]
    let part2 () =
        let data: string = System.IO.File.ReadAllText("day01.txt")
        test <@ top3Elves data = 193697 @>
