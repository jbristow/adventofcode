module Day03

let rec findSquare (x : int) side =
    if (x > (side * side)) then findSquare x (side + 2)
    else side

let spiralContaining (x : int) =
    let n = findSquare x 1

    let cycle =
        List.replicate n [ 1
                           n
                           -1
                           -n ]
        |> List.concat
        |> List.take (2 * n + 1)
    [ n..(-1)..0 ]
    |> List.collect (List.replicate 2)
    |> List.skip 1
    |> List.map2 (fun c x -> List.replicate x c) cycle
    |> List.concat
    |> List.scan (fun (acc : int) (c : int) -> acc + c) 0
    |> List.skip 1
    |> List.rev
    |> List.mapi (fun i x -> i, x)
    |> List.sortBy snd
    |> List.map (fst >> (+) 1)
    |> List.rev
    |> List.chunkBySize n
    |> List.mapi (fun y row -> row |> List.mapi (fun x v -> ((x, y), v)))

let findDistance ai bi =
    let (a, b) =
        if ai > bi then bi, ai
        else ai, bi

    let spiral = spiralContaining b |> List.concat

    let center =
        spiral
        |> List.find (fun ((x, y), v) -> v = a)
        |> fst

    let memory =
        spiral
        |> List.find (fun ((x, y), v) -> v = b)
        |> fst

    abs (fst center - fst memory) + abs (snd center - snd memory)

let memVal (cx, cy) filled =
    let value =
        filled
        |> List.filter
               (fun (p, v : int64) ->
               p = (cx - 1, cy - 1) || p = (cx - 1, cy) || p = (cx - 1, cy + 1)
               || p = (cx, cy - 1) || p = (cx, cy + 1) || p = (cx + 1, cy - 1)
               || p = (cx + 1, cy) || p = (cx + 1, cy + 1))
        |> List.fold (fun acc (_, c) -> acc + c) (int64 0)
    (cx, cy), value

let rec fillMemory filled unfilled =
    match unfilled with
    | [] -> filled
    | head :: rest -> fillMemory <| ((memVal head filled) :: filled) <| rest

let fillMemoryUpTo (n) =
    let spiral =
        spiralContaining n
        |> List.concat
        |> List.sortBy snd
        |> List.map fst

    let first = ((spiral |> List.head), (int64 1))
    let rest = spiral |> List.skip 1
    fillMemory [ first ] rest

let findGtInMemory (spiralSize : int) (limit : int) : int64 =
    let memory = fillMemoryUpTo spiralSize |> List.rev
    let i = memory |> List.findIndex (fun (_, v) -> v > (int64 limit))
    memory
    |> List.item i
    |> snd
