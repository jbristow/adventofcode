module Tests.Day24

open System
open NUnit.Framework
open Swensen.Unquote
open Day24

[<TestFixture>]
type Sample() =

    let (strongest, _, strengthOfLongest) =
        [| "0/2"; "2/2"; "2/3"; "3/4"; "3/5"; "0/1"; "10/1"; "9/10" |]
        |> parseInput
        |> build

    [<Test>]
    member this.TestSamplePart1() = strongest =! 31

    [<Test>]
    member this.TestSamplePart2() = strengthOfLongest =! 19

[<TestFixture>]
type Answer() =

    let (strongest, _, strengthOfLongest) =
        System.IO.File.ReadAllLines("day24.txt")
        |> parseInput
        |> build

    [<Test>]
    member this.TestAnswerPart1() = strongest =! 1868

    [<Test>]
    member this.TestAnswerPart2() = strengthOfLongest =! 1841
