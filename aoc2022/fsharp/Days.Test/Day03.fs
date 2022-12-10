module Test.Day03

open NUnit.Framework
open Swensen.Unquote
open Day03

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllText("day03.txt").Split('\n')

    [<Test>]
    let part1 () = test <@ part1 data = 7850 @>

    [<Test>]
    let part2 () = test <@ part2 data = 2581 @>
