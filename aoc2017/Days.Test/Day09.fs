module Tests.Day09

open System
open NUnit.Framework
open Swensen.Unquote
open Day09

[<TestCase("{}", ExpectedResult = 1)>]
[<TestCase("{{{}}}", ExpectedResult = 6)>]
[<TestCase("{{},{}}", ExpectedResult = 5)>]
[<TestCase("{{{},{},{{}}}}", ExpectedResult = 16)>]
[<TestCase("{<a>,<a>,<a>,<a>}", ExpectedResult= 1)>]
[<TestCase("{{<ab>},{<ab>},{<ab>},{<ab>}}", ExpectedResult = 9)>]
[<TestCase("{{<!!>},{<!!>},{<!!>},{<!!>}}", ExpectedResult = 9)>]
[<TestCase("{{<a!>},{<a!>},{<a!>},{<ab>}}", ExpectedResult = 3)>]
let ``sample data day09-part01`` (input) =
  garbageCleaner input

[<Test>]
let ``answer day09-part01`` () =
    let data : string = System.IO.File.ReadAllText("day09.txt")
    garbageCleaner data =! 16827


[<TestCase("<>", ExpectedResult = 0)>]
[<TestCase("<random characters>", ExpectedResult = 17)>]
[<TestCase("<<<<>", ExpectedResult = 3)>]
[<TestCase("<{!>}>", ExpectedResult = 2)>]
[<TestCase("<!!>", ExpectedResult = 0)>]
[<TestCase("<!!!>>", ExpectedResult = 0)>]
[<TestCase("<{oXi!a,<{i<a>", ExpectedResult = 10)>]
let ``sample data day09-part02`` (input) =
  garbageCounter input

[<Test>]
let ``answer day09-part02`` () =
    let data : string = System.IO.File.ReadAllText("day09.txt")
    garbageCounter data =! 7298
