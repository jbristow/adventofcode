module Day19

let linesToGrid (lines : string array) =
    lines
    |> Array.mapi (fun y row -> row |> Seq.mapi (fun x cell -> (x, y), cell))
    |> Seq.concat
    |> Seq.filter (fun (_, b) -> b <> ' ')
    |> Map.ofSeq

let findEntry grid =
    grid
    |> Map.toSeq
    |> Seq.find (fun ((x, y), v) -> y = 0 && v = '|')
    |> fst

type Direction =
    | Up
    | Down
    | Left
    | Right

let (|Alpha|_|) co =
    match co with
    | Some c when c >= 'A' && c <= 'Z' -> Some c
    | _ -> None

let (|UpDown|_|) c =
    match c with
    | Some '|' -> Some()
    | _ -> None

let (|LeftRight|_|) c =
    match c with
    | Some '-' -> Some()
    | _ -> None

let (|Corner|_|) c =
    match c with
    | Some '+' -> Some()
    | _ -> None


let nextCoord x y d =
    match d with
    | Some Up -> Some(x, y - 1)
    | Some Down -> Some(x, y + 1)
    | Some Left -> Some(x - 1, y)
    | Some Right -> Some(x + 1, y)
    | None -> None

let neighbor x y direction =
    match direction with
    | Some Up | Some Down ->
        (fun (kx, ky) v -> (ky = y) && ((kx = x + 1) || (kx = x - 1)))
    | Some Left | Some Right ->
        (fun (kx, ky) v -> (kx = x) && ((ky = y + 1) || (ky = y - 1)))
    | None -> (fun (_, _) _ -> false)

let chooseNextDir x y grid d =
    match (grid |> Map.tryFindKey (neighbor x y d)) with
    | Some(xn, _) when xn = (x + 1) -> Some Right
    | Some(xn, _) when xn = (x - 1) -> Some Left
    | Some(_, yn) when yn = (y + 1) -> Some Down
    | Some(_, yn) when yn = (y - 1) -> Some Up
    | _ -> None

let rec move seen (grid : Map<int * int, char>) direction ((x, y) as coord)
        count =
    let current = grid |> Map.tryFind coord

    let nextSeen, nextDirection =
        match (direction, current) with
        | Some d, (Alpha c) -> (c :: seen), Some d
        | Some d, Corner -> seen, chooseNextDir x y grid (Some d)
        | Some d, _ -> seen, Some d
        | _ -> seen, None

    match current, (nextCoord x y nextDirection), count with
    | (Some c, Some nc, _) -> move nextSeen grid nextDirection nc (count + 1)
    | _ -> nextSeen, count

let pathfind input =
    let grid = input |> linesToGrid
    let entry = grid |> findEntry
    let seen, _ = move [] grid (Some(Down)) entry 0
    seen
    |> List.rev
    |> List.fold (fun a c -> a + string c) ""

let distance input =
    let grid = input |> linesToGrid
    let entry = grid |> findEntry
    let _, count = move [] grid (Some(Down)) entry 0
    count
