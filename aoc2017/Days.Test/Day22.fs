module Tests.Day22

open System
open NUnit.Framework
open Swensen.Unquote
open Day22

[<Test>]
let testSimpleParse() =
  Part1.parseInput [|"...";"...";"..."|] =! Map.ofList  [((-1, -1), false); ((-1, 0), false); ((-1, 1), false); ((0, -1), false); ((0, 0), false); ((0, 1), false); ((1, -1), false); ((1, 0), false); ((1, 1), false)]

[<Test>]
let testSamplePart1_70() =
  let c = Part1.simulate 70 [| "..#"; "#.."; "..."; |]
  printfn "%A" c
  c.Infected =! 41

[<Test>]
let testSamplePart1_10k() =
  let c = Part1.simulate 10000 [| "..#"; "#.."; "..."; |]
  printfn "%A" c
  c.Infected =! 5587

[<Test>]
let testAnswerPart1() =
  let data : string array = System.IO.File.ReadAllLines("day22.txt")

  let c = data |> Part1.simulate 10000
  printfn "%A" c
  c.Infected =! 5462

[<Test>]
let testSamplePart2_100() =
  let c = Part2.simulate 100 [| "..#"; "#.."; "..."; |]
  printfn "%A" c
  c.Infected =! 26

[<Test>]
let testSamplePart2_10m() =
  let c = Part2.simulate 10000000 [| "..#"; "#.."; "..."; |]
  printfn "%A" c
  c.Infected =! 2511944

[<Test>]
let testAnswerPart2() =
  let data : string array = System.IO.File.ReadAllLines("day22.txt")

  let c = data |> Part2.simulate 10000000
  printfn "%A" c
  c.Infected =! 2512135
