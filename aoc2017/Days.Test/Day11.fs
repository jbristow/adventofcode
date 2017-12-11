module Tests.Day11

open System
open NUnit.Framework
open Swensen.Unquote
open Day11

let ZERO:Point = (0,0,0)

[<TestCase("ne,ne,ne", ExpectedResult=3)>]
[<TestCase("ne,ne,sw,sw", ExpectedResult=0)>]
[<TestCase("ne,ne,s,s", ExpectedResult=2)>]
[<TestCase("se,sw,se,sw,sw", ExpectedResult=3)>]
let ``sample derived test day11-part01`` (input) =
  input |> inputToPoints |> findFinalDistanceFrom ZERO

[<Test>]
let ``answer day11-part01`` () =
    let data : string = System.IO.File.ReadAllText("day11.txt")
    data.Trim() |> inputToPoints |> findFinalDistanceFrom ZERO =! 664

[<TestCase("ne,ne,ne", ExpectedResult=3)>]
[<TestCase("ne,ne,sw,sw", ExpectedResult=2)>]
[<TestCase("ne,ne,s,s", ExpectedResult=2)>]
[<TestCase("se,sw,se,sw,sw", ExpectedResult=3)>]
let ``sample derived test day11-part02`` (input) =
  input |> inputToPoints |> findMaxDistanceFrom ZERO

[<Test>]
let ``answer day11-part02`` () =
    let data : string = System.IO.File.ReadAllText("day11.txt")
    data.Trim() |> inputToPoints |> findMaxDistanceFrom ZERO =! 1447




