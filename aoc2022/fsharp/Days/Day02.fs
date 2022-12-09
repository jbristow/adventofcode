module Day02

open FSharpx

type Outcome =
    | Lose
    | Draw
    | Win

    member this.Points =
        match this with
        | Lose -> 0
        | Draw -> 3
        | Win -> 6

type Move =
    | Rock
    | Paper
    | Scissors

    member this.Points: int =
        match this with
        | Rock -> 1
        | Paper -> 2
        | Scissors -> 3

    member this.Beats: Move =
        match this with
        | Rock -> Scissors
        | Paper -> Rock
        | Scissors -> Paper

    member this.LosesTo: Move =
        match this with
        | Rock -> Paper
        | Paper -> Scissors
        | Scissors -> Rock

    member this.WillBeat(m: Move) : bool = this.Beats = m
    member this.WillLoseTo(m: Move) : bool = this.LosesTo = m

let toMove (m: string) =
    match m with
    | "A"
    | "X" -> Rock
    | "B"
    | "Y" -> Paper
    | "C"
    | "Z" -> Scissors
    | _ -> failwith $"`{m}` is not a valid move."

let toOutcome (m: string) =
    match m with
    | "X" -> Lose
    | "Y" -> Draw
    | "Z" -> Win
    | _ -> failwith $"`{m}` is not a valid outcome."

let computeOutcome (theirs: Move) (mine: Move) =
    match theirs, mine with
    | t, m when (t = m) -> Draw
    | t, m when t.WillBeat m -> Lose
    | t, m when t.WillLoseTo m -> Win
    | t, m -> failwith $"I don't know what to do with {t} and {m}"


let score (theirs: Move) (mine: Move) : int =
    (computeOutcome theirs mine).Points + mine.Points

let scoreByResult (theirs: Move) (result: Outcome) : int =
    let mine =
        match result with
        | Lose -> theirs.Beats
        | Draw -> theirs
        | Win -> theirs.LosesTo

    mine.Points + result.Points

let sumFn (txA: string -> 'A) (txB: string -> 'B) (transform: 'A -> 'B -> int) (line: string list) : int =
    match line with
    | a :: b :: _ -> transform (txA a) (txB b)
    | _ -> failwith $"Could not process line {line}"

let split (line: string) =
    line |> String.splitChar [| ' ' |] |> Array.toList

let part1 (input: string[]) =
    input |> Seq.map split |> Seq.sumBy (sumFn toMove toMove score)

let part2 (input: string[]) =
    input |> Seq.map split |> Seq.sumBy (sumFn toMove toOutcome scoreByResult)
