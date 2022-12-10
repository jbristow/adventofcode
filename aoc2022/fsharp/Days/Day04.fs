module Day04

let overlapsCompletely ((firstA: int, lastA: int): int * int) ((firstB: int, lastB: int): int * int) =
    (firstA <= firstB && lastA >= lastB) || (firstA >= firstB && lastA <= lastB)

let overlaps (firstA: int, lastA: int) (firstB: int, lastB: int) =
    (firstA >= firstB && firstA <= lastB) || (firstB >= firstA && firstB <= lastA)

let parseTuple (s: string) =
    let value = s.Split([| '-' |]) |> Array.map int
    (value[0], value[1])

let parseLine (s: string) =
    let tuples = s.Split([| ',' |]) |> Array.map parseTuple
    (tuples[0], tuples[1])

let parse = Seq.map parseLine

let part1 (input: string seq) =
    input
    |> parse
    |> Seq.filter (fun (a, b) -> a |> overlapsCompletely <| b)
    |> Seq.length

let part2 (input: string seq) =
    input |> parse |> Seq.filter (fun (a, b) -> a |> overlaps <| b) |> Seq.length
