module Day06

let inputToMap (input : string) =
    input.Split '\t'
    |> Seq.ofArray
    |> Seq.mapi (fun i x -> i, int x)
    |> Map.ofSeq

let mostBlocks (map : Map<int, int>) =
    map
    |> Map.toSeq
    |> Seq.maxBy snd

let step (map : Map<int, int>) =
    let (i, blocks) = mostBlocks map
    let n = map |> Map.count
    let out : Map<int, int> = map |> Map.add i 0

    let update =
        [ i + 1..i + blocks ]
        |> List.groupBy (fun k -> k % n)
        |> List.map (fun t -> (fst t), (snd t |> List.length))
        |> List.fold (fun m (i, a) -> m |> Map.add i ((m |> Map.find i) + a))
               out
    update

let mapToString (input : Map<int, int>) =
    sprintf "%s" <| System.String.Join("\t",
                                       (input
                                        |> Map.toList
                                        |> List.map snd))

let loop input =
    let rec loop' (input : Map<int, int>) seen =
        let sInput = mapToString input
        let seenInputBefore a = a = sInput
        match seen |> List.tryFindIndex seenInputBefore with
        // since this is effectively a stack, the last instance of this item
        // will be `cycle-count` from the top and the number of steps before we
        // know there's a loop is just the number of different items we've
        // seen total.
        | Some x -> seen |> List.length, x + 1
        | _ -> loop' (step input) (sInput :: seen)
    loop' input List.empty
