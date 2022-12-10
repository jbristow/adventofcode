module Test.Day05

open NUnit.Framework
open Swensen.Unquote
open Day05

[<TestFixture>]
module Answers =
    let data = System.IO.File.ReadAllText("day05.txt")

    [<Test>]
    let part1 () = test <@ part1 data = "SVFDLGLWV" @>

    [<Test>]
    let part2 () = test <@ part2 data = "DCVTCVPCL" @>
