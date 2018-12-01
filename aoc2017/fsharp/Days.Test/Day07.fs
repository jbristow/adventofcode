module Tests.Day07

open System
open NUnit.Framework
open Swensen.Unquote
open Day07

[<Test>]
let ``Testing single-line parsing`` () =
  let line = "gcricg (135) -> oowec, vfsonrs, fcszioo"
  parseLine line =! ("gcricg",135,["oowec";"vfsonrs";"fcszioo"])

  "rmxzhgl (39)" |> parseLine =! ("rmxzhgl",39,[])

[<Test>]
let ``Testing multi-line parsing`` () =
  [|
    "fotck (62)"; "gnkzi (8441) -> cqcmf, bhsmhi, lkisrer, wkwylw, xunaojn";
    "aokaek (55) -> hqzsf, iraqghm"; "kfwcftc (66)"|]
  |> parseLines =! [("fotck",62,[]);("gnkzi",8441,["cqcmf";"bhsmhi";"lkisrer";"wkwylw";"xunaojn"]); ("aokaek",55,["hqzsf";"iraqghm"]);("kfwcftc",66,[])]

let sampleData = [| "pbga (66)"; "xhth (57)"; "ebii (61)"; "havc (66)"; "ktlj (57)"; "fwft (72) -> ktlj, cntj, xhth"; "qoyq (66)"; "padx (45) -> pbga, havc, qoyq"; "tknk (41) -> ugml, padx, fwft"; "jptl (61)"; "ugml (68) -> gyxo, ebii, jptl"; "gyxo (61)"; "cntj (57)";|]

[<Test>]
let ``Testing sample for day07-part01`` ()=
  sampleData |> findBottom =! "tknk"

[<Test>]
let ``Answer for day07-part01`` () =
    let data : string array = System.IO.File.ReadAllLines("day07.txt")
    data |> findBottom =! "vvsvez"
[<Test>]
let ``Testing sample for day07-part02`` () =
  sampleData |> buildTree |> findAnomaly =! Some 60

[<Test>]
let ``Answer for day07-part02`` ()=
    let data : string array = System.IO.File.ReadAllLines("day07.txt")
    data |> buildTree |> findAnomaly  =! Some 362
     
