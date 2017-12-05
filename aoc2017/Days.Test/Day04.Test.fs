module ``Tests for Day 04``

open System
open NUnit.Framework
open FsUnit
open Day04

type InitMsgUtils() =
    inherit FSharpCustomMessageFormatter()

let shouldEqual (x : 'a) (y : 'a) =
    Assert.AreEqual(x, y, sprintf "Expected: %A\nActual: %A" x y)

[<Test>]
let ``answer, part 1`` () =
  let data : string array = System.IO.File.ReadAllLines("day04.txt")
  (data |> List.ofArray |> countValid isValidPassphraseDuplicates) |> shouldEqual 477

let splitterData = [
  TestCaseData("aa bb cc dd ee").Returns(["aa";"bb";"cc";"dd";"ee"])
  TestCaseData("aa bb cc dd aa").Returns(["aa";"bb";"cc";"dd";"aa"])
  TestCaseData("aa bb cc dd aaa").Returns(["aa";"bb";"cc";"dd";"aaa"])
  ]

[<TestCaseSource("splitterData")>]
let ``testing the splitter`` (input) =
  let out = wordSplit input
  printfn "given `%s` -> `%A`" input out
  out

let validData = [
  TestCaseData(["aa";"bb";"cc";"dd";"ee"]).Returns(true)
  TestCaseData(["aa";"bb";"cc";"dd";"aa"]).Returns(false)
  TestCaseData(["aa";"bb";"cc";"dd";"aaa"]).Returns(true)
  ]

[<TestCaseSource("validData")>]
let ``testing the validator`` (input) =
  isValidPassphraseDuplicates input

let validData2 = [ 
  TestCaseData(["abcde";"fghij"]).Returns(true)
  TestCaseData(["abcde";"xyz";"ecdab"]).Returns(false)
  TestCaseData(["a";"ab";"abc";"abd";"abf";"abj"]).Returns(true)
  TestCaseData(["iiii";"oiii";"ooii";"oooi";"oooo"]).Returns(true)
]

[<TestCaseSource("validData2")>]
let ``testing the anagram passphrase validator`` (input) =
  isValidPassphraseAnagram input

[<Test>]
let ``answer, part 2`` () =
  let data : string array = System.IO.File.ReadAllLines("day04.txt")
  (data |> List.ofArray |> countValid isValidPassphraseAnagram) |> shouldEqual 477

