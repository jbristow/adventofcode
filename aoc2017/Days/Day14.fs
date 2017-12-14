module Day14

let numSquares =
    function
    | '0' -> 0 | '1' -> 1 | '2' -> 1 | '3' -> 2 | '4' -> 1 | '5' -> 2
    | '6' -> 2 | '7' -> 3 | '8' -> 1 | '9' -> 2 | 'a' -> 2 | 'b' -> 3
    | 'c' -> 2 | 'd' -> 3 | 'e' -> 3 | 'f' -> 4
    | _ -> failwith "Bad digit"

let charToBin =
    function
    | '0' -> "0000" | '1' -> "0001" | '2' -> "0010" | '3' -> "0011"
    | '4' -> "0100" | '5' -> "0101" | '6' -> "0110" | '7' -> "0111"
    | '8' -> "1000" | '9' -> "1001" | 'a' -> "1010" | 'b' -> "1011"
    | 'c' -> "1100" | 'd' -> "1101" | 'e' -> "1110" | 'f' -> "1111"
    | _ -> failwith "Bad digit"

let genSquare input =
    [ 0..127 ]
    |> List.sumBy
           (fun i ->
           Day10.hashByInputAscii (input + "-" + string i)
           |> Seq.sumBy numSquares)

let changeRegion m toReg fromReg =
    m |> Map.map (fun _ currRegion ->
             if currRegion = fromReg then toReg
             else currRegion)

let addCell (x, y) n (regMap : Map<int * int, int>) =
    let neighbors =
        List.map regMap.TryFind [ (x + 1, y)
                                  (x - 1, y)
                                  (x, y + 1)
                                  (x, y - 1) ]
        |> List.filter Option.isSome
        |> List.map Option.get
    match neighbors with
    | [] -> regMap |> Map.add (x, y) n, n + 1
    | [ a ] -> (regMap |> Map.add (x, y) a), n
    | rs ->
        let minRegion = List.min rs
        List.fold
            (fun (m : Map<int * int, int>) (r : int) ->
            changeRegion m minRegion r) (regMap |> Map.add (x, y) minRegion) rs,
        n

let rec regionFind regMap keys currRegion =
    match keys with
    | h :: r ->
        let nextRegionMap, nextRegion = (regMap |> addCell h currRegion)
        regionFind nextRegionMap r nextRegion
    | [] -> regMap

let regions input =
    let keys =
        [ 0..127 ]
        |> List.map (fun i ->
               Day10.hashByInputAscii (input + "-" + string i)
               |> Seq.collect charToBin
               |> Seq.toList)
        |> List.mapi
               (fun y row -> (row |> List.mapi (fun x cell -> (x, y), cell)))
        |> List.concat
        |> List.filter (fun (_, v) -> v = '1')
        |> List.map fst

    regionFind Map.empty keys 0
    |> Map.toList
    |> List.map snd
    |> List.distinct
    |> List.length
