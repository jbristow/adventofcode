module Tests.Day10

open System
open NUnit.Framework
open Swensen.Unquote
open Day10

[<Test>]
let ``Test range func``() = range 5 =! [ 0; 1; 2; 3; 4 ]

let sampleDay10Part01Data =
    [ TestCaseData(0, 0, [ 0; 1; 2; 3; 4 ], 3).Returns([ 0, 2; 1, 1; 2, 0; 3, 3; 4, 4 ] |> Map.ofList)
      TestCaseData(3, 1, [ 2; 1; 0; 3; 4 ], 4).Returns([ 0, 4; 1, 3; 2, 0; 3, 1; 4, 2 ] |> Map.ofList)
      TestCaseData(3, 2, [ 4; 3; 0; 1; 2 ], 1).Returns([ 0, 4; 1, 3; 2, 0; 3, 1; 4, 2 ] |> Map.ofList)
      TestCaseData(1, 3, [ 4; 3; 0; 1; 2 ], 5).Returns([ 0, 3; 1, 4; 2, 2; 3, 1; 4, 0 ] |> Map.ofList) ]

[<TestCaseSource("sampleDay10Part01Data")>]
let sampleDay10Part01 (start, skip, col, length) =
    mutate (col
            |> List.mapi (fun i x -> i, x)
            |> Map.ofList) (start, skip, length, 5)

[<Test>]
let sampleDay10Part01All() =
    hashByInput 5 "3,4,1,5"
    |> Map.toList
    |> List.map snd
    =! [ 3; 4; 2; 1; 0 ]

[<TestCase("", ExpectedResult = "a2582a3a0e66e6e86e3812dcb672a272")>]
[<TestCase("AoC 2017", ExpectedResult = "33efeb34ea91902bb2f59c9920caa6cd")>]
[<TestCase("1,2,3", ExpectedResult = "3efbe78a8d82f29979031a4aa0b16a9d")>]
[<TestCase("1,2,4", ExpectedResult = "63960835bcdc130f0b66d7ff4f6a5a8e")>]
let sampleDay10Part02 (input) = hashByInputAscii input

[<Test>]
let testXorReduce() =
    List.reduce (^^^) [ 65; 27; 9; 1; 4; 3; 40; 50; 91; 7; 6; 0; 2; 5; 68; 22 ]
    =! 64

[<Test>]
let testStrToInt() = strToInt "1,2,3" =! [ 49; 44; 50; 44; 51 ]

[<TestFixture>]
type AnswerDay10() =
    let data : string = System.IO.File.ReadAllText("day10.txt").Trim()

    [<Test>]
    member this.AnswerDay10Part01() =
        let value = data |> hashByInput 256
        value.[0] * value.[1] =! 212

    [<Test>]
    member this.AnswerDay10Part02() =
        hashByInputAscii data =! "96de9657665675b51cd03f0b3528ba26"
