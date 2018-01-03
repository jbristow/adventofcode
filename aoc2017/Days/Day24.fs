module Day24

type Piece = int * int

module Piece =
    let strength ((a, b) : Piece) = a + b

    let ofString (s : string) : Piece =
        let ab = s.Split('/') |> Array.map int
        ab.[0], ab.[1]

    let equals (a, b) (a', b') = (a = a' && b = b') || (a = b' && b = a')
    let canConnectTo x path ((a, b) as piece) =
        (x = a || x = b) && not <| (path |> List.contains piece)

    let availablePort x (a, b) =
        if x = a then b
        else a

let parseInput (input : string array) : Piece list =
    input
    |> Array.map Piece.ofString
    |> Array.toList

let build pieces =
    let rec buildTree exposedPort path =
        let strength =
            match path with
            | [] -> 0
            | h :: _ -> h |> Piece.strength

        let children =
            pieces
            |> List.filter (Piece.canConnectTo exposedPort path)
            |> List.map
                   (fun c ->
                   buildTree (c |> Piece.availablePort exposedPort) (c :: path))
            |> List.sortBy (fun (str, _, _) -> -str)

        let childStrength, (longestChildLength, longestChildStrength) =
            match children with
            | [] -> 0, (path |> List.length, 0)
            | _ ->
                children
                |> List.map (fun (a, _, _) -> a)
                |> List.head,
                children
                |> List.map (fun (_, b, c) -> (b, c))
                |> List.maxBy (fun (len, _) -> len)

        (strength + childStrength, longestChildLength,
         strength + longestChildStrength)
    buildTree 0 []
