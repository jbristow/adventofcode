module Day13

let parseLine (line : string) =
    match line.Split([| ": " |], System.StringSplitOptions.None) with
    | [| x; y |] -> (int x, 2 * (int y - 1), int y)
    | _ -> failwith (sprintf "Bad input: `%s`" line)

let toSeverity (depth, sweep, range) =
    if (depth % sweep = 0) then depth * range
    else 0

let notCaughtAt x (depth, sweep, _) = (x + depth) % sweep <> 0
let totalSeverity (lines : string array) =
    lines |> Array.sumBy (parseLine >> toSeverity)

let findFirstZeroSev (lines : string array) =
    let scanners = lines |> Array.map (parseLine)

    let rec findZero x =
        match scanners |> Array.forall (notCaughtAt x) with
        | true -> x
        | false -> findZero (x + 1)
    findZero 0
