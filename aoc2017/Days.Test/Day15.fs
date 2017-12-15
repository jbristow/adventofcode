module Tests.Day15

open System
open NUnit.Framework
open Swensen.Unquote
open Day15

[<Test>]
let samplePart1GeneratorA() =
    generator 16807 65
    |> Seq.take 5
    |> Seq.toList
    =! [ 1092455UL; 1181022009UL; 245556042UL; 1744312007UL; 1352636452UL ]

[<Test>]
let samplePart1GeneratorB() =
    generator 48271 8921
    |> Seq.take 5
    |> Seq.toList
    =! [ 430625591UL; 1233683848UL; 1431495498UL; 137874439UL; 285222916UL ]

[<Test>]
let samplePart1CompareTrue() = test <@ compare (245556042UL, 1431495498UL) @>

[<Test>]
let samplePart1CompareFalse() =
    test <@ not <| compare (1352636452UL, 285222916UL) @>

[<Test>]
let samplePart1() = matchCount 65 8921 40000000
                    |> Seq.length
                    =! 588

[<Test>]
let answerPart1() =
    let answer = (matchCount 783 325 40000000)
    answer
    |> Seq.length
    =! 650

[<Test>]
let samplePart2Generators() =
    Seq.zip (generatorBeta 16807 65 4) (generatorBeta 48271 8921 8)
    |> Seq.take 5
    |> Seq.toList
    =! [ 1352636452UL, 1233683848UL
         1992081072UL, 862516352UL
         530830436UL, 1159784568UL
         1980017072UL, 1616057672UL
         740335192UL, 412269392UL ]

[<Test>]
let samplePart2() = matchCountBeta 65 8921 5000000
                    |> Seq.length
                    =! 309

[<Test>]
let answerPart2() = matchCountBeta 783 325 5000000
                    |> Seq.length
                    =! 336
