module Day07

open System.Text.RegularExpressions

type Tree =
    | Branch of string * int * Tree list
    | Leaf of string * int

let parseLine line =
    let re = "^([a-z]+) \((\d+)\)(?: -> (\w+(?:, \w+)*))?"

    let parsed =
        Regex.Match(line, re).Groups
        |> Seq.cast
        |> Seq.map (fun (g : Group) -> g.Value)
        |> List.ofSeq
        |> List.tail
    match parsed with
    | [ x; y; "" ] -> (x, (int y), List.empty)
    | [ x; y; z ] ->
        (x, (int y),
         (z.Split([| ", " |], System.StringSplitOptions.None) |> List.ofArray))
    | _ -> failwith (sprintf "bad row `%s`" line)

let parseLines (lines : string array) =
    lines
    |> List.ofArray
    |> List.map parseLine

let nodesWithChildren (nodeTuples : (string * int * string list) list) : string list =
    nodeTuples
    |> List.filter (fun (node, _, children) ->
           children
           |> List.isEmpty
           |> not)
    |> List.map (fun (a, _, _) -> a)

let nodesWithParents (nodeTuples : (string * int * string list) list) : string list =
    nodeTuples |> List.collect (fun (_, _, children) -> children)

let findBottom input =
    let nodeTuples = input |> parseLines
    Set.difference (nodeTuples
                    |> nodesWithChildren
                    |> Set.ofList) (nodeTuples
                                    |> nodesWithParents
                                    |> Set.ofList)
    |> Seq.head

let rec totalWeight root =
    match root with
    | Branch(_, w, c) -> w + (c |> List.sumBy totalWeight)
    | Leaf(_, w) -> w

let getWeight = function
    | Branch(_, w, _) | Leaf(_, w) -> w

let getChildren =
    function
    | Branch(_, _, c) -> c
    | Leaf(_, _) -> []

let buildTree input =
    let nodeTuples = input |> parseLines

    let nodeMap =
        nodeTuples
        |> List.map (fun (node, weight, children) -> (node, (weight, children)))
        |> Map.ofSeq

    let bottom = findBottom input

    let rec buildTree' label =
        match nodeMap |> Map.find label with
        | weight, [] -> Leaf(label, weight)
        | weight, children ->
            Branch(label, weight, children |> List.map buildTree')
    bottom |> buildTree'

let lengthOfSnd (_, y) = y |> List.length

let rec findAnomaly (root : Tree) : int option =
    let childrenByWeight =
        root
        |> getChildren
        |> List.groupBy totalWeight
    if childrenByWeight
       |> List.length
       <= 1 then None
    else
        let outOfBalanceGroup = childrenByWeight |> List.minBy lengthOfSnd
        let outOfBalanceWeight, _ = outOfBalanceGroup
        let outOfBalanceNode = snd outOfBalanceGroup |> List.head
        let outOfBalanceNodeWeight = outOfBalanceNode |> getWeight
        let balancedWeight, _ = (childrenByWeight |> List.maxBy lengthOfSnd)
        match outOfBalanceNode |> findAnomaly with
        | None ->
            Some(balancedWeight - outOfBalanceWeight + outOfBalanceNodeWeight)
        | Some x -> Some x
