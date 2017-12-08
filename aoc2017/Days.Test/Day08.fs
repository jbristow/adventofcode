module Tests.Day08

open System
open NUnit.Framework
open Swensen.Unquote
open Day08

[<Test>]
let ``Parse sample-data line part01`` () =
  parseLine (Map.empty,None) "b inc 5 if a > 1" =! (Map.empty,None)
  parseLine (Map.empty,None) "a inc 1 if b < 5" =! ((["a",1] |> Map.ofSeq),Some 1)

[<Test>]
let ``Parse sample-data all lines part01`` () =
  parseLines Map.empty [| "b inc 5 if a > 1"; "a inc 1 if b < 5"; "c dec -10 if a >= 1"; "c inc -20 if c == 10" |] =! ((["a",1;"c",-10] |> Map.ofSeq),Some 10)

[<Test>]
let ``Answer day08-part01`` () =
    let data : string array = System.IO.File.ReadAllLines("day08.txt")
    let registers,_ = parseLines Map.empty data
    registers |> Map.toSeq |> Seq.sortBy snd |> Seq.last |> snd =! 2971
[<Test>]
let ``Answer day08-part02`` () =
    let data : string array = System.IO.File.ReadAllLines("day08.txt")
    let _, highest = parseLines Map.empty data
    highest =! Some 4254
