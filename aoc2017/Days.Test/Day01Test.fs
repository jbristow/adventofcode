module Day01Test

open System
open NUnit.Framework
open FsUnit
open Day01

type InitMsgUtils() =
    inherit FSharpCustomMessageFormatter()

[<Test>]
let ``allEq2 should return true if given a tuple with the same chars.``() =
    allEq2 ('a', 'a') |> should be True

[<Test>]
let ``allEq2 should return true if given a tuple with different chars.``() =
    allEq2 ('a', 'b') |> should be False

[<TestCase("1122",ExpectedResult=3)>]
[<TestCase("1111",ExpectedResult=4)>]
[<TestCase("1234",ExpectedResult=0)>]
[<TestCase("91212129",ExpectedResult=9)>]
let ``Testing input.`` (input) =
    antiCaptcha input

[<Test>]
let ``Part 1: problem solver``() =
    let data:string = System.IO.File.ReadAllText("day01.txt")
    antiCaptcha data |> should equal 1144

[<TestCase("1212",ExpectedResult=6)>]
[<TestCase("1221",ExpectedResult=0)>]
[<TestCase("123425",ExpectedResult=4)>]
[<TestCase("123123",ExpectedResult=12)>]
[<TestCase("12131415",ExpectedResult=4)>]
let ``Testing input for part 2.`` (input) =
    antiCaptchaPart2 input

[<Test>]
let ``Part 2: problem solver``() =
    let data:string = System.IO.File.ReadAllText("day01.txt")
    antiCaptchaPart2 data |> should equal 1194
