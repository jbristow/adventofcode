module Test.Day04

open NUnit.Framework
open Swensen.Unquote
open Days.Day04

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllText("day04.txt").Split('\n')

    [<Test>]
    let part1 () = test <@ part1 data = 431 @>

    [<Test>]
    let part2 () = test <@ part2 data = 823 @>
