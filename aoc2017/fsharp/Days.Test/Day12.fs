module Tests.Day12

open System
open NUnit.Framework
open Swensen.Unquote
open Day12

[<Test>]
let testLineParser () =
  "2 <-> 0, 3, 4" |> parseLine =! (2,[|0;3;4|])
  "5 <-> 6" |> parseLine =! (5,[|6|])

[<TestFixture>]
module Answer =
  let data = System.IO.File.ReadAllLines("day12.txt")

  [<Test>]
  let part01() =
    data |> numberInGroupWith 0 =! 239

  [<Test>]
  let part02() =
    let data = System.IO.File.ReadAllLines("day12.txt")
    data |> djs |> Map.toList |> List.filter (fun (k,v) -> k=v) |> List.length =! 215

[<TestFixture>]
module Sample =
  let sampleData = [|"0 <-> 2"; "1 <-> 1"; "2 <-> 0, 3, 4"; "3 <-> 2, 4"; "4 <-> 2, 3, 6"; "5 <-> 6"; "6 <-> 4, 5"; |]

  [<Test>]
  let part01 () =
    sampleData |> numberInGroupWith 0 =! 6

  [<Test>]
  let part02 () =
    sampleData |> djs |> Map.toList |> List.filter (fun (k,v) -> k=v) |>List.length =! 2

