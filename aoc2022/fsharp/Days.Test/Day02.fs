module Tests.Day02

open System
open NUnit.Framework
open Swensen.Unquote
open Day02

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllText("day02.txt").Split('\n')

    [<Test>]
    let part1 () = test <@ part1 data = 13675 @>

    [<Test>]
    let part2 () = test <@ part2 data = 14184 @>
