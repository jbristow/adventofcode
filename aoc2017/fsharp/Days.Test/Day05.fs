module Tests.Day05

open System
open NUnit.Framework
open Swensen.Unquote
open Day05

let zipmap x =
    x
    |> List.mapi (fun i x -> (i, x))
    |> Map.ofList

let stepTestData =
    let data (i : int) (ts : int list) = TestCaseData(i, (ts |> zipmap))
    let returns (i : int option) (ts : int list) (tcd : TestCaseData) =
        tcd.Returns(i, ts |> zipmap)
    [ (data 0 [ 0; 3; 0; 1; -3 ]) |> returns (Some 0) [ 1; 3; 0; 1; -3 ]
      (data 0 [ 1; 3; 0; 1; -3 ]) |> returns (Some 1) [ 2; 3; 0; 1; -3 ]
      (data 1 [ 2; 3; 0; 1; -3 ]) |> returns (Some 4) [ 2; 4; 0; 1; -3 ]
      (data 4 [ 2; 4; 0; 1; -3 ]) |> returns (Some 1) [ 2; 4; 0; 1; -2 ]
      (data 1 [ 2; 4; 0; 1; -2 ]) |> returns (Some 5) [ 2; 5; 0; 1; -2 ]
      (data 5 [ 2; 5; 0; 1; -2 ]) |> returns (None) [ 2; 5; 0; 1; -2 ] ]

[<TestCaseSource("stepTestData")>]
let ``single jump test`` (i, trampolines) = trampolines |> step i

[<Test>]
let ``test day05 data parser``() =
    trampolineMap [| "1"; "2"; "0"; "-2"; "10" |]
    =! ([ 1; 2; 0; -2; 10 ] |> zipmap)

[<Test>]
let ``test for sample data in day05-part01``() =
    [| "0"; "3"; "0"; "1"; "-3" |]
    |> jumpOut
    =! 5

[<Test>]
let ``answer for day05-part01``() =
    let data : string array = System.IO.File.ReadAllLines("day05.txt")
    data
    |> jumpOut
    =! 394829

[<Test>]
let ``test for samples in day05-part02``() =
    [| "0"; "3"; "0"; "1"; "-3" |]
    |> jumpOutStranger
    =! 10

[<Test>]
let ``answer for day05-part02``() =
    let data : string array = System.IO.File.ReadAllLines("day05.txt")
    data
    |> jumpOutStranger
    =! 31150702
