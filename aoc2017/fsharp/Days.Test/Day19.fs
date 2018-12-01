module Tests.Day19

open System
open NUnit.Framework
open Swensen.Unquote
open Day19

let sampleData = [|
  "     |          ";
  "     |  +--+    ";
  "     A  |  C    ";
  " F---|----E|--+ ";
  "     |  |  |  D ";
  "     +B-+  +--+ ";
|]

[<Test>]
let testSampleDataPart1 () =
  pathfind sampleData =! "ABCDEF"

[<Test>]
let testSampleDataPart2 () =
  distance sampleData =! 38

[<TestFixture>]
module answersDay19 =

  let data : string array = System.IO.File.ReadAllLines("day19.txt")

  [<Test>]
  let testAnswerPart1 () =
    pathfind data =! "HATBMQJYZ"

  [<Test>]
  let testAnswerPart2 () =
    distance data =! 16332


