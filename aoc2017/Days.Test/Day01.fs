module Tests.Day01

open System
open NUnit.Framework
open Swensen.Unquote
open Day01

[<Test>]
let ``allEq2 returns true if a 2-tuple has identical items ``() =
    test <@ allEq2 ('a', 'a') @>

[<Test>]
let ``allEq2 returns false if a 2-tuple has different items ``() =
    test <@ not <| allEq2 ('a', 'b') @>

[<TestCase("1122",ExpectedResult=3)>]
[<TestCase("1111",ExpectedResult=4)>]
[<TestCase("1234",ExpectedResult=0)>]
[<TestCase("91212129",ExpectedResult=9)>]
let ``Test derived from day01-part01 examples`` (input) =
    antiCaptcha input

[<Test>]
let ``Answer for day01-part01``() =
    let data:string = System.IO.File.ReadAllText("day01.txt")
    test <@ antiCaptcha data = 1144 @>

[<TestCase("1212",ExpectedResult=6)>]
[<TestCase("1221",ExpectedResult=0)>]
[<TestCase("123425",ExpectedResult=4)>]
[<TestCase("123123",ExpectedResult=12)>]
[<TestCase("12131415",ExpectedResult=4)>]
let ``Test derived from day01-part02 examples.`` (input) =
    antiCaptchaPart2 input

[<Test>]
let ``Answer for day01-part02``() =
    let data:string = System.IO.File.ReadAllText("day01.txt")
    test <@ antiCaptchaPart2 data = 1194 @>
