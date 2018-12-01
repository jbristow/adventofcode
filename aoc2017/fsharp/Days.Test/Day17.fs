module Tests.Day17

open System
open NUnit.Framework
open Swensen.Unquote
open Day17

let samplePart1Data =
    [ TestCaseData(0, 0, [ 0 ]).Returns((1, 1, [ 0; 1 ]))
      TestCaseData(1, 1, [ 0; 1 ]).Returns((1, 2, [ 0; 2; 1 ]))
      TestCaseData(1, 2, [ 0; 2; 1 ]).Returns((2, 3, [ 0; 2; 3; 1 ]))
      TestCaseData(2, 3, [ 0; 2; 3; 1 ]).Returns((2, 4, [ 0; 2; 4; 3; 1 ]))

      TestCaseData(2, 4, [ 0; 2; 4; 3; 1 ])
          .Returns((1, 5, [ 0; 5; 2; 4; 3; 1 ])) ]

[<TestCaseSource("samplePart1Data")>]
let samplePart1 curr x buff = spin 3 (curr, x, buff)

[<Test>]
let samplePart1Full() =
    let curr, _, buffer = bufferedSpinner 3 |> Seq.item 2017
    buffer.[curr + 1] =! 638

[<Test>]
let answerPart1() =
    let curr, _, buffer = bufferedSpinner 356 |> Seq.item 2017
    buffer.[curr + 1] =! 808

[<Test>]
let answerPart2() =
    let _, _, a = unbufferedSpinner 356 |> Seq.item 50000000
    a =! 47465686
