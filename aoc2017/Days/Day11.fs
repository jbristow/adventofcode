module Day11

type Point = int * int * int

let N : Point = (0, 1, -1)
let NW : Point = (-1, 1, 0)
let SW : Point = (-1, 0, 1)
let S : Point = (0, -1, 1)
let SE : Point = (1, -1, 0)
let NE : Point = (1, 0, -1)

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
    | "n" -> N
    | "nw" -> NW
    | "sw" -> SW
    | "s" -> S
    | "se" -> SE
    | "ne" -> NE
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
