module Tests.Day04

open System
open NUnit.Framework
open Swensen.Unquote
open Day04

let splitterData =
    [ TestCaseData("aa bb cc dd ee").Returns([ "aa"; "bb"; "cc"; "dd"; "ee" ])
      TestCaseData("aa bb cc dd aa").Returns([ "aa"; "bb"; "cc"; "dd"; "aa" ])
      TestCaseData("aa bb cc dd aaa").Returns([ "aa"; "bb"; "cc"; "dd"; "aaa" ]) ]

[<TestCaseSource("splitterData")>]
let ``testing the splitter`` (input) = wordSplit input

let validData =
    [ TestCaseData([ "aa"; "bb"; "cc"; "dd"; "ee" ]).Returns(true)
      TestCaseData([ "aa"; "bb"; "cc"; "dd"; "aa" ]).Returns(false)
      TestCaseData([ "aa"; "bb"; "cc"; "dd"; "aaa" ]).Returns(true) ]

[<TestCaseSource("validData")>]
let ``test derived from day04-part01 `` (input) =
    isValidPassphraseDuplicates input

let validData2 =
    [ TestCaseData([ "abcde"; "fghij" ]).Returns(true)
      TestCaseData([ "abcde"; "xyz"; "ecdab" ]).Returns(false)
      TestCaseData([ "a"; "ab"; "abc"; "abd"; "abf"; "abj" ]).Returns(true)
      TestCaseData([ "iiii"; "oiii"; "ooii"; "oooi"; "oooo" ]).Returns(true) ]

[<TestCaseSource("validData2")>]
let ``test derived from day04-part02 examples`` (input) =
    isValidPassphraseAnagram input

[<Test>]
let ``Answer day04-part01``() =
    let data : string array = System.IO.File.ReadAllLines("day04.txt")
    data
    |> List.ofArray
    |> countValid isValidPassphraseDuplicates
    =! 477

[<Test>]
let ``Answer day04-part02``() =
    let data : string array = System.IO.File.ReadAllLines("day04.txt")
    data
    |> List.ofArray
    |> countValid isValidPassphraseAnagram
    =! 167
