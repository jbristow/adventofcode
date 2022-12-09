module Day01

open System

let calorieSum (input: string) =
    input.Split('\n') |> Seq.sumBy (fun l -> l |> int)

let elfCarryingMost (input: string) =
    input.Split([| "\n\n" |], StringSplitOptions.RemoveEmptyEntries)
    |> Seq.map calorieSum
    |> Seq.max

let top3Elves (input: string) =
    input.Split([| "\n\n" |], StringSplitOptions.RemoveEmptyEntries)
    |> Seq.map calorieSum
    |> Seq.sort
    |> Seq.rev
    |> Seq.take 3
    |> Seq.sum
