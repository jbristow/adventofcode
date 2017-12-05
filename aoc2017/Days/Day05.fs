module Day05

type trampolines = Map<int, int>

let step i (tsMap : trampolines) =
    match tsMap |> Map.tryFind i with
    | Some x -> Some(i + x), tsMap |> Map.add i (x + 1)
    | _ -> None, tsMap

let stepStranger i (tsMap : trampolines) =
    match tsMap |> Map.tryFind i with
    | Some x when x < 3 -> Some(i + x), tsMap |> Map.add i (x + 1)
    | Some x -> Some(i + x), tsMap |> Map.add i (x - 1)
    | _ -> None, tsMap

let trampolineMap (input : string array) =
    input
    |> List.ofArray
    |> List.mapi (fun i x -> (i, int x))
    |> Map.ofList

let rec jumpOutVia f i steps tsMap =
    match f i tsMap with
    | (Some nextI), nextTsMap -> nextTsMap |> jumpOutVia f nextI (steps + 1)
    | None, _ -> steps

let jumpOut input =
    (trampolineMap input) |> jumpOutVia step 0 0

let jumpOutStranger input =
    (trampolineMap input) |> jumpOutVia stepStranger 0 0
