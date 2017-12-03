module ``Tests for Day 02``

open System
open NUnit.Framework
open FsUnit
open Day02

type InitMsgUtils() =
    inherit FSharpCustomMessageFormatter()

let rowDiffTestData =
    [ TestCaseData([ 5; 1; 9; 5 ]).Returns(8)
      TestCaseData([ 7; 5; 3 ]).Returns(4)
      TestCaseData([ 2; 4; 6; 8 ]).Returns(6) ]

[<TestCaseSource("rowDiffTestData")>]
let ``testing individual rows`` (row) = rowDiff 0 row

let smallSample =
    [ [ 5; 1; 9; 5 ]
      [ 7; 5; 3 ]
      [ 2; 4; 6; 8 ] ]

[<Test>]
let ``test small sample``() = corruptionChecksum smallSample |> should equal 18

[<Test>]
let ``test parsing``() =
    parseString [| "5 1 9 5"; "7 5 3"; "2 4 6 8" |] |> should equal smallSample

[<Test>]
let ``Part 1: problem solver``() =
    let data : string array = System.IO.File.ReadAllLines("day02.txt")
    data
    |> parseString
    |> corruptionChecksum
    |> should equal 34925

let evenDivTestData =
    [ TestCaseData([ 5; 9; 2; 8 ]).Returns(4)
      TestCaseData([ 9; 4; 7; 3 ]).Returns(3)
      TestCaseData([ 3; 8; 6; 5 ]).Returns(2) ]

[<TestCaseSource("evenDivTestData")>]
let FindEvenDiv(row) = findEvenDiv 0 row

[<Test>]
let ``Part 2: problem solver``() =
    let data : string array = System.IO.File.ReadAllLines("day02.txt")
    data
    |> parseString
    |> corruptionChecksumPart2
    |> should equal 221
