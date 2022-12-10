module Test.Day06

open NUnit.Framework
open Swensen.Unquote
open Day06

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllText("day06.txt")

    [<Test>]
    let part1 () = test <@ part1 data = 1987 @>

    [<Test>]
    let part2 () = test <@ part2 data = 3059 @>
