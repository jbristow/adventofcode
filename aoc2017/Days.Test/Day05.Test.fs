module ``Tests for Day 05``

open System
open NUnit.Framework
open FsUnit
open Day05

let zipmap x =
    x
    |> List.mapi (fun i x -> (i, x))
    |> Map.ofList

let stepDataTransform (a, b, c, d) =
    TestCaseData(a, b |> zipmap).Returns(c, d |> zipmap)
let testCaseData x = TestCaseData(x |> List.toArray)
let returns y (tcd : TestCaseData) = tcd.Returns(y)

let stepTestData =
    [ testCaseData [ 0
                     [ 0; 3; 0; 1; -3 ] |> zipmap ]
      |> returns (Some 0, [ 1; 3; 0; 1; -3 ] |> zipmap)
      testCaseData [ 0
                     [ 1; 3; 0; 1; -3 ] |> zipmap ]
      |> returns (Some 1, [ 2; 3; 0; 1; -3 ] |> zipmap)
      testCaseData [ 1
                     [ 2; 3; 0; 1; -3 ] |> zipmap ]
      |> returns (Some 4, [ 2; 4; 0; 1; -3 ] |> zipmap)
      testCaseData [ 4
                     [ 2; 4; 0; 1; -3 ] |> zipmap ]
      |> returns (Some 1, [ 2; 4; 0; 1; -2 ] |> zipmap)
      testCaseData [ 1
                     [ 2; 4; 0; 1; -2 ] |> zipmap ]
      |> returns (Some 5, [ 2; 5; 0; 1; -2 ] |> zipmap)
      testCaseData [ 5
                     [ 2; 5; 0; 1; -2 ] |> zipmap ]
      |> returns (None, [ 2; 5; 0; 1; -2 ] |> zipmap) ]

[<TestCaseSource("stepTestData")>]
let ``Test one step`` (i, trampolines) = step i trampolines

[<Test>]
let ``test parser``() =
    trampolineMap [| "1"; "2"; "0"; "-2"; "10" |]
    |> should equal ([ 1; 2; 0; -2; 10 ] |> zipmap)

[<Test>]
let ``full test``() =
    [| "0"; "3"; "0"; "1"; "-3" |]
    |> jumpOut
    |> should equal 5

[<Test>]
let ``answer, part 1``() =
    let data : string array = System.IO.File.ReadAllLines("day05.txt")
    data
    |> jumpOut
    |> should equal 394829

[<Test>]
let ``full test stranger``() =
    [| "0"; "3"; "0"; "1"; "-3" |]
    |> jumpOutStranger
    |> should equal 10

[<Test>]
let ``answer, part 2``() =
    let data : string array = System.IO.File.ReadAllLines("day05.txt")
    data
    |> jumpOutStranger
    |> should equal 31150702
