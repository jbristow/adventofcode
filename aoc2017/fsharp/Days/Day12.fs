module Day12

open System
open System.Text.RegularExpressions

let split (partitioner : string) (s : string) =
    s.Split([| partitioner |], StringSplitOptions.RemoveEmptyEntries)

let parseRhs s =
    split ", " s
    |> Array.map int

let (|PipeMatcher|_|) line =
    let pipeMatch = Regex.Match(line, @"(\d+) <-> (\d+(?:, \d+)*)")
    if pipeMatch.Success then
        Some(pipeMatch.Groups.[1].Value, pipeMatch.Groups.[2].Value)
    else None

let parseLine line : int * int array =
    match line with
    | PipeMatcher(a, b) -> (int a, parseRhs b)
    | _ -> failwith (sprintf "Bad input: %s" line)

let toEdges (x, ys) = ys |> Array.map (fun y -> (x, y))

let rec findRootPath (x : int) m (seen : int list) =
    match m |> Map.tryFind x with
    | Some parent when parent <> x -> findRootPath parent m (x :: seen)
    | Some parent -> (parent :: seen)
    | None -> [ x ]

let changeParentsToRoot (path : int list) m =
    let root = path |> List.head
    path |> List.fold (fun mAcc c -> mAcc |> Map.add c root) m

let djs lines =
    let edges = Array.collect (parseLine >> toEdges) lines

    let rec processEdge (m : Map<int, int>) (x, y) =
        let rootPathX = findRootPath x m []
        let rootPathY = findRootPath y m []

        let m1 =
            m
            |> (changeParentsToRoot rootPathX)
            |> (changeParentsToRoot rootPathY)

        let a, b =
            if List.length rootPathY > List.length rootPathX then y, x
            else x, y

        match m1.TryFind a, m1.TryFind b with
        | Some p, _ when p = a -> m1 |> Map.add b p
        | None, Some p when p = b -> m1 |> Map.add a p
        | Some p, _ -> processEdge m1 (p, b)
        | None, Some p -> processEdge m1 (p, a)
        | None, None -> m1 |> Map.add b a

    let pass1 = edges |> Array.fold processEdge Map.empty
    pass1
    |> Map.toList
    |> List.map (fun (k, v) -> k, v)
    |> List.fold processEdge pass1

let findRootOf x m = findRootPath x m [] |> List.head

let numberInGroupWith x lines =
    let disjs = djs lines
    let root = disjs |> findRootOf x
    disjs
    |> Map.filter (fun k v -> v = root)
    |> Map.toList
    |> List.length
