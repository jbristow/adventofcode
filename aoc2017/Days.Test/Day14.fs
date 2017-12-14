module Tests.Day14

open System
open NUnit.Framework
open Swensen.Unquote
open Day14

[<Test>]
let samplePart1() = genSquare "flqrgnkx" =! 8108

[<Test>]
let answerPart1() = genSquare "ffayrhll" =! 8190

[<Test>]
let samplePart2() = regions "flqrgnkx" =! 1242

[<Test>]
let answerPart2() = regions "ffayrhll" =! 1134
