    module Day02

    open System.Text.RegularExpressions

    let rowDiff (acc : int) (row : int list) =
        let sorted = List.sort row
        acc + (sorted |> List.last) - (sorted |> List.head)

    let corruptionChecksum (input : int list list) = List.fold rowDiff 0 input

    let parseString (input : string array) =
        input
        |> List.ofArray
        |> List.map (fun s ->
              Regex.Matches(s, "(\d+)")
              |> Seq.cast
              |> Seq.map (fun (m : Match) -> System.Int32.Parse m.Value)
              |> List.ofSeq)

    let isEvenlyDiv a b =
        if a > b then ((a % b) = 0)
        else ((b % a) = 0)

    let rec findEvenDiv (acc : int) (row : int list) : int =
        let x : int = row |> List.head
        let xs = row |> List.skip 1
        let evendivs = xs |> List.filter (isEvenlyDiv x)

        match evendivs with
        | n :: [] when n < x -> acc + (x / n)
        | n :: [] when n > x -> acc + (n / x)
        | _ -> findEvenDiv acc xs

    let corruptionChecksumPart2 (input : int list list) =
        List.fold findEvenDiv 0 input
