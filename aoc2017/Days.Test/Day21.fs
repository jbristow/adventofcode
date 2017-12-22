module Tests.Day21

open System
open NUnit.Framework
open Swensen.Unquote
open Day21

let data3x3x4 =
    [ [ [ "00"; "01"; "02" ]
        [ "10"; "11"; "12" ]
        [ "20"; "21"; "22" ] ]
      [ [ "03"; "04"; "05" ]
        [ "13"; "14"; "15" ]
        [ "23"; "24"; "25" ] ]
      [ [ "30"; "31"; "32" ]
        [ "40"; "41"; "42" ]
        [ "50"; "51"; "52" ] ]
      [ [ "33"; "34"; "35" ]
        [ "43"; "44"; "45" ]
        [ "53"; "54"; "55" ] ] ]

let data6x6 =
    [ [ "00"; "01"; "02"; "03"; "04"; "05" ]
      [ "10"; "11"; "12"; "13"; "14"; "15" ]
      [ "20"; "21"; "22"; "23"; "24"; "25" ]
      [ "30"; "31"; "32"; "33"; "34"; "35" ]
      [ "40"; "41"; "42"; "43"; "44"; "45" ]
      [ "50"; "51"; "52"; "53"; "54"; "55" ] ]

let data8x8 =
    [ [ "00"; "01"; "02"; "03"; "04"; "05"; "06"; "07" ]
      [ "10"; "11"; "12"; "13"; "14"; "15"; "16"; "17" ]
      [ "20"; "21"; "22"; "23"; "24"; "25"; "26"; "27" ]
      [ "30"; "31"; "32"; "33"; "34"; "35"; "36"; "37" ]
      [ "40"; "41"; "42"; "43"; "44"; "45"; "46"; "47" ]
      [ "50"; "51"; "52"; "53"; "54"; "55"; "56"; "57" ]
      [ "60"; "61"; "62"; "63"; "64"; "65"; "66"; "67" ]
      [ "70"; "71"; "72"; "73"; "74"; "75"; "76"; "77" ] ]

let data4x4x4 =
    [ [ [ "00"; "01"; "02"; "03" ]
        [ "10"; "11"; "12"; "13" ]
        [ "20"; "21"; "22"; "23" ]
        [ "30"; "31"; "32"; "33" ] ]
      [ [ "04"; "05"; "06"; "07" ]
        [ "14"; "15"; "16"; "17" ]
        [ "24"; "25"; "26"; "27" ]
        [ "34"; "35"; "36"; "37" ] ]
      [ [ "40"; "41"; "42"; "43" ]
        [ "50"; "51"; "52"; "53" ]
        [ "60"; "61"; "62"; "63" ]
        [ "70"; "71"; "72"; "73" ] ]
      [ [ "44"; "45"; "46"; "47" ]
        [ "54"; "55"; "56"; "57" ]
        [ "64"; "65"; "66"; "67" ]
        [ "74"; "75"; "76"; "77" ] ] ]

let data4x4 =
    [ [ 1; 2; 3; 4 ]
      [ 5; 6; 7; 8 ]
      [ 9; 10; 11; 12 ]
      [ 13; 14; 15; 16 ] ]

let data2x2x4 =
    [ [ [ 1; 2 ]
        [ 5; 6 ] ]
      [ [ 3; 4 ]
        [ 7; 8 ] ]
      [ [ 9; 10 ]
        [ 13; 14 ] ]
      [ [ 11; 12 ]
        [ 15; 16 ] ] ]

let data2x2x16 =
    [ [ [ "00"; "01" ]
        [ "10"; "11" ] ]
      [ [ "02"; "03" ]
        [ "12"; "13" ] ]
      [ [ "04"; "05" ]
        [ "14"; "15" ] ]
      [ [ "06"; "07" ]
        [ "16"; "17" ] ]
      [ [ "20"; "21" ]
        [ "30"; "31" ] ]
      [ [ "22"; "23" ]
        [ "32"; "33" ] ]
      [ [ "24"; "25" ]
        [ "34"; "35" ] ]
      [ [ "26"; "27" ]
        [ "36"; "37" ] ]
      [ [ "40"; "41" ]
        [ "50"; "51" ] ]
      [ [ "42"; "43" ]
        [ "52"; "53" ] ]
      [ [ "44"; "45" ]
        [ "54"; "55" ] ]
      [ [ "46"; "47" ]
        [ "56"; "57" ] ]
      [ [ "60"; "61" ]
        [ "70"; "71" ] ]
      [ [ "62"; "63" ]
        [ "72"; "73" ] ]
      [ [ "64"; "65" ]
        [ "74"; "75" ] ]
      [ [ "66"; "67" ]
        [ "76"; "77" ] ] ]

[<Test>]
let testRejoin3() = data3x3x4
                    |> rejoin
                    =! data6x6

[<Test>]
let testRejoin4() = data4x4x4
                    |> rejoin
                    =! data8x8

[<Test>]
let testSplit2() = data4x4
                   |> split 2
                   =! data2x2x4

[<Test>]
let testSplit8() = data8x8
                   |> split 2
                   =! data2x2x16

[<Test>]
let testSamplePart1() =
    let input =
        [| "../.# => ##./#../..."; ".#./..#/### => #..#/..../..../#..#" |]

    let expected =
        [ [ '#'; '#'; '.'; '#'; '#'; '.' ]
          [ '#'; '.'; '.'; '#'; '.'; '.' ]
          [ '.'; '.'; '.'; '.'; '.'; '.' ]
          [ '#'; '#'; '.'; '#'; '#'; '.' ]
          [ '#'; '.'; '.'; '#'; '.'; '.' ]
          [ '.'; '.'; '.'; '.'; '.'; '.' ] ]
    (run 2 input) =! expected

[<Test>]
let testAnswerPart1() =
    let data : string array = System.IO.File.ReadAllLines("day21.txt")
    (run 5 data)
    |> countOn
    =! 110

[<Test>]
let testParsePatternRotate2() =
    "#./.."
    |> Pattern.ofString
    |> Pattern.rotate
    =! Pattern.ofString ".#/.."


[<Test>]
let testParsePatternRotate3() =
    "##./#.#/#.."
    |> Pattern.ofString
    |> Pattern.rotate
    |> Pattern.rotate
    |> Pattern.rotate
    =! Pattern.ofString ".#./#../###"

[<Test>]
let testParseRule() =
    "#./.. => ###/###/###"
    |> Rule.ofString
    |> Seq.length
    =! 4

[<Test>]
let testParseRule3() =
    let ruleStr = "#../.../... => ###/###/###"
    Rule.ofString ruleStr
    |> Seq.length
    =! 4

[<Test>]
let testParseRule3Rotation() =
    let ruleStr = ".#./#../### => ###/###/###"
    let rules = Rule.ofString ruleStr
    rules
    |> Seq.length
    =! 8

    rules
    |> Map.ofSeq
    |> Map.find ".#./#../###"
    =! ("###/###/###"|>Pattern.ofString)

[<Test>]
//[<Ignore("too long")>]
let testAnswerPart2() =
    let data : string array = System.IO.File.ReadAllLines("day21.txt")
    run 18 data
    |> countOn
    =! 1277716
