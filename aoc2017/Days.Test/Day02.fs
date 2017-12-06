module Tests.Day02

open System
open NUnit.Framework
open Swensen.Unquote
open Day02

let rowDiffTestData =
    [ TestCaseData([ 5; 1; 9; 5 ]).Returns(8)
      TestCaseData([ 7; 5; 3 ]).Returns(4)
      TestCaseData([ 2; 4; 6; 8 ]).Returns(6) ]

[<TestCaseSource("rowDiffTestData")>]
let ``tests derived from day02-part01 examples`` (row) = rowDiff 0 row

let smallSample =
    [ [ 5; 1; 9; 5 ]
      [ 7; 5; 3 ]
      [ 2; 4; 6; 8 ] ]

[<Test>]
let ``test small sample``() = test <@ corruptionChecksum smallSample = 18 @>

[<Test>]
let ``input parsing``() =
    test <@ (parseString [| "5 1 9 5"; "7 5 3"; "2 4 6 8" |]) = smallSample @>

[<Test>]
let ``Answer for day02-part01``() =
    let data : string array = System.IO.File.ReadAllLines("day02.txt")
    test <@ (data |> parseString |> corruptionChecksum) = 34925 @>

let evenDivTestData =
    [ TestCaseData([ 5; 9; 2; 8 ]).Returns(4)
      TestCaseData([ 9; 4; 7; 3 ]).Returns(3)
      TestCaseData([ 3; 8; 6; 5 ]).Returns(2) ]

[<TestCaseSource("evenDivTestData")>]
let ``tests derived from day02-part02 examples``(row) = findEvenDiv 0 row

[<Test>]
let ``Answer for day02-part02``() =
    let data : string array = System.IO.File.ReadAllLines("day02.txt")
    test <@ (data |> parseString |> corruptionChecksumPart2) = 221 @>
