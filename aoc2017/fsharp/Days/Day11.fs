module Day11

type Point = int * int * int

let distance ((ax, ay, az) : Point)  : Point -> int =
    let distanceSubFn ((bx, by, bz) : Point) : int =
      List.max [ bx - ax
                 by - ay
                 bz - az ]
    distanceSubFn

let addPoints ((ax, ay, az) : Point) ((bx, by, bz) : Point) : Point =
    (ax + bx, ay + by, az + bz)

let strToPoint =
    function
    | "n" -> (0, 1, -1)
    | "nw" -> (-1, 1, 0)
    | "sw" -> (-1, 0, 1)
    | "s" -> (0, -1, 1)
    | "se" -> (1, -1, 0)
    | "ne" -> (1, 0, -1)
    | x -> failwith (sprintf "Bad movement. `%s`" x)

let inputToPoints (input : string) : Point list =
    input.Split(',')
    |> List.ofArray
    |> List.map strToPoint

let findFinalDistanceFrom (start : Point) (moves : Point list) : int =
    moves
    |> List.fold addPoints start
    |> distance start

let findMaxDistanceFrom (start : Point) (moves : Point list) : int =
    moves
    |> List.scan addPoints start
    |> List.map (distance start)
    |> List.max
