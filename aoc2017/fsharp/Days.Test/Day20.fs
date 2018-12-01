module Tests.Day20

open System
open NUnit.Framework
open Swensen.Unquote
open Day20

[<Test>]
let testUtilityAdd3 () = add3 (1, 2, 3) (2, 3, 4) =! (3, 5, 7)

let parse3TestData =
    [ TestCaseData("p=<1,2,3>").Returns(1, 2, 3)
      TestCaseData("v=<-1,2,-3>").Returns(-1, 2, -3)
      TestCaseData("a=<123,-234,-345>").Returns(123, -234, -345) ]

[<TestCaseSource("parse3TestData")>]
let testParse3 (s) = parse3 s

[<Test>]
let testSimpleParticleParse() =
    "p=<-11104,1791,5208>, v=<-6,36,-84>, a=<19,-5,-4>"
    |> Particle.OfString 0
    =! { Label = 0
         Position = (-11104, 1791, 5208)
         Velocity = (-6, 36, -84)
         Acceleration = (19, -5, -4) }
[<Test>]
let testSamplePart1 () =
  let closest = [| "p=<3,0,0>, v=<2,0,0>, a=<-1,0,0>"; "p=<4,0,0>, v=<0,0,0>, a=<-2,0,0>" |] |> findOverallClosest
  closest.Label =! 0

[<Test>]
let testAnswerPart1 () =
    let data : string array = System.IO.File.ReadAllLines("day20.txt")
    let closest = data |> findOverallClosest
    printfn "%A" closest
    closest.Label =! 300

[<Test>]
let testSamplePart2 () =
  let data = [| "p=<-6,0,0>, v=<3,0,0>, a=<0,0,0>"; "p=<-4,0,0>, v=<2,0,0>, a=<0,0,0>"; "p=<-2,0,0>, v=<1,0,0>, a=<0,0,0>"; "p=<3,0,0>, v=<1,0,0>, a=<0,0,0>"; |]
  let left = data |> demolitionDerby
  left |> List.length =! 1

[<Test>]
let testAnswerPart2 () =
    let data : string array = System.IO.File.ReadAllLines("day20.txt")
    let left = data |> demolitionDerby
    printfn "%A" (left |> List.head)
    left |> List.length =! -1
