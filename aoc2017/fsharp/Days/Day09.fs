module Day09

let garbageCleaner (input : string) =
    let rec cleaner (s : char list) inGarbage depth score =
        match s with
        | '!' :: _ :: r -> cleaner r inGarbage depth score
        | '}' :: r when not inGarbage ->
            cleaner r inGarbage (depth - 1) (score + depth)
        | '>' :: r -> cleaner r false depth score
        | '{' :: r when not inGarbage -> cleaner r inGarbage (depth + 1) score
        | '<' :: r when not inGarbage -> cleaner r true depth score
        | _ :: r -> cleaner r inGarbage depth score
        | [] -> score
    cleaner (input |> List.ofSeq) false 0 0

let garbageCounter (input : string) =
    let rec cleaner (s : char list) inGarbage count =
        match s with
        | '!' :: _ :: r -> cleaner r inGarbage count
        | '}' :: r when not inGarbage -> cleaner r inGarbage count
        | '>' :: r -> cleaner r false count
        | '{' :: r when not inGarbage -> cleaner r inGarbage count
        | '<' :: r when not inGarbage -> cleaner r true count
        | _ :: r when inGarbage -> cleaner r inGarbage (count + 1)
        | _ :: r -> cleaner r inGarbage count
        | [] -> count
    cleaner (input |> List.ofSeq) false 0
