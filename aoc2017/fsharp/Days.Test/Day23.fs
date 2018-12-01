module Tests.Day23

open System
open NUnit.Framework
open Swensen.Unquote
open Day23

[<Test>]
let testAnswerPart1Slow () =
    let data : string array = System.IO.File.ReadAllLines("day23.txt")
    let p,mulN = runProgram true data
    mulN =! 4225

[<Test>]
let testAnswerPart1 () =
  numberOfMulCalls =! 4225

[<Test>]
let testAnswerPart2 () =
  valueOfRegisterH =! 905

