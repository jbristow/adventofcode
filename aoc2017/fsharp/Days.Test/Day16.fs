module Tests.Day16

open System
open NUnit.Framework
open Swensen.Unquote
open Day16

[<Test>]
let sampleSpinPart01 () =
  "abcde" |> spin 3 =! "cdeab"
[<Test>]
let sampleExchangePart01 () =
  "eabcd" |> exchange 3 4 =! "eabdc"
let data = "abcdefghijklmnopqrstuvwxyz"

[<TestCase(0,25,ExpectedResult = "zbcdefghijklmnopqrstuvwxya")>]
[<TestCase(1,24,ExpectedResult = "aycdefghijklmnopqrstuvwxbz")>]
[<TestCase(2,4,ExpectedResult = "abedcfghijklmnopqrstuvwxyz")>]
let sampleTest (a, b) =
  data |> exchange a b

[<Test>]
let sampleAllTest () =
  "s1,x3/4,pe/b" |> dance "abcde" =! "baedc"

[<TestFixture>]
module answersDay16 =
  let data : string = System.IO.File.ReadAllText("day16.txt")

  [<Test>]
  let answerPart1 () =
      data |> dance "abcdefghijklmnop" =! "ociedpjbmfnkhlga"

  [<Test>]
  let answerPart2 () =
    data |> billionPosition "abcdefghijklmnop" =! "gnflbkojhicpmead"

