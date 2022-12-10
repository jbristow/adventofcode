module Day03

let lowercaseMin = 'a' |> int
let lowercaseMax = 'z' |> int


let priority (inputChar: char) =
    match (inputChar |> int) with
    | c when c >= lowercaseMin && c <= lowercaseMax -> c - 96
    | c -> c - 38

let intersect (line: string) =
    let first = line.Substring(0, line.Length / 2) |> set
    let second = line.Substring(line.Length / 2) |> set
    first |> Set.intersect <| second

let part1 (input: string seq) =
    input |> Seq.collect intersect |> Seq.sumBy priority

let part2 (input: string seq) =
    input
    |> Seq.map set
    |> Seq.chunkBySize 3
    |> Seq.collect (Seq.reduce Set.intersect)
    |> Seq.sumBy priority
