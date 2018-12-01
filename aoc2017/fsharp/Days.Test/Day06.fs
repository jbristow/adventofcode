module Tests.Day06

open System
open NUnit.Framework
open Swensen.Unquote
open Day06

[<Test>]
let ``Test parsing``() =
    inputToMap "1\t2\t3\t5" =! (Map.ofSeq [ 0, 1
                                            1, 2
                                            2, 3
                                            3, 5 ])

[<Test>]
let ``MostBlocks test``() =
    "0\t2\t7\t0"
    |> inputToMap
    |> mostBlocks
    =! (2, 7)

let stepTestData =
    let returns actual (tcd : TestCaseData) = tcd.Returns(actual |> Map.ofSeq)
    [ TestCaseData("0\t2\t7\t0") |> returns [ 0, 2
                                              1, 4
                                              2, 1
                                              3, 2 ]
      TestCaseData("2\t4\t1\t2") |> returns [ 0, 3
                                              1, 1
                                              2, 2
                                              3, 3 ]
      TestCaseData("3\t1\t2\t3") |> returns [ 0, 0
                                              1, 2
                                              2, 3
                                              3, 4 ] ]

[<TestCaseSource("stepTestData")>]
let ``block redistribution test`` (input) = step (inputToMap input)

[<Test>]
let ``test derived from sample for day05``() =
    "0\t2\t7\t0"
    |> inputToMap
    |> loop
    =! (5, 4)

[<Test>]
let ``Answer for day05-part01 and day05-part02``() =
    let data : string = System.IO.File.ReadAllText("day06.txt")
    data
    |> inputToMap
    |> loop
    =! (11137, 1037)
