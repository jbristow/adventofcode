module Day21

type Pattern = char list list

type Rule = string * Pattern

type RuleMap = Map<string, Pattern>

module Pattern =
    let ofString (s : string) : Pattern =
        s.Split([| '/' |])
        |> List.ofArray
        |> List.map (Seq.toList)

    let toString (p : Pattern) : string =
        System.String.Join
            ("/", p |> List.map (fun p' -> System.String.Join("", p')))

    let rotate (p : Pattern) : Pattern =
        let n = p |> List.length
        p
        |> List.mapi
               (fun (i : int) (row : char list) ->
               row |> List.mapi (fun (j : int) (cell : char) -> (i, j, cell)))
        |> List.concat
        |> List.groupBy (fun (_, j, _) -> j)
        |> List.map (fun (j, b) ->
               b
               |> List.map (fun (i, _, ch) -> (n - i - 1), ch)
               |> List.sortBy fst
               |> List.map snd)

    let flipH (p : Pattern) : Pattern = p |> List.map (List.rev)
    let flipV (p : Pattern) : Pattern = p |> List.rev

module Rule =
    let ofString (line : string) : Rule seq =
        let patternStr, outputStr =
            match line.Split([| " => " |], System.StringSplitOptions.None) with
            | [| a; b |] -> (a, b)
            | _ -> failwith (sprintf "Could not parse line '%s'" line)

        let output = outputStr |> Pattern.ofString
        let pattern = patternStr |> Pattern.ofString

        let patternGen start =
            seq {
                let r1 = start |> List.map Pattern.rotate
                let r2 = r1 |> List.map Pattern.rotate
                yield! start
                yield! r1
                yield! r2
                yield! (r2 |> List.map Pattern.rotate)
            }

        let rotatedPatterns =
            patternGen [ pattern
                         pattern |> Pattern.flipH
                         pattern |> Pattern.flipV ]

        rotatedPatterns
        |> Seq.distinct
        |> Seq.map (fun p -> (p |> Pattern.toString, output))

module RuleMap =
    let ofArray (data : string array) =
        data
        |> Seq.collect Rule.ofString
        |> Map.ofSeq

let splitRow n gridRow =
    let rowsize =
        (gridRow
         |> List.head
         |> List.length) / n - 1
    [ 0..rowsize ]
    |> List.map
           (fun i -> (gridRow |> List.map (List.chunkBySize n >> List.item i)))

let split n grid =
    grid
    |> List.chunkBySize n
    |> List.collect (splitRow n)

let rejoinRow gridrow =
    let rowsize =
        (gridrow
         |> List.head
         |> List.length)
        - 1
    [ 0..rowsize ] |> List.map (fun i -> gridrow |> List.collect (List.item i))

let rejoin grids =
    let size = (int (sqrt (float (grids |> Seq.length))))
    grids
    |> List.chunkBySize size
    |> List.collect rejoinRow

let applyRule (ruleMap : RuleMap) (grid : Pattern) =
    ruleMap |> Map.find (grid |> Pattern.toString)

let splitAndApplyRules (ruleMap : RuleMap) n (grid : Pattern) =
    grid
    |> split n
    |> List.map (applyRule ruleMap)
    |> rejoin

let rec gridStep (ruleMap : RuleMap) (grid : Pattern) =
    let transformer =
        match grid |> List.length with
        | 2 | 3 -> applyRule ruleMap
        | x when x % 2 = 0 -> splitAndApplyRules ruleMap 2
        | x when x % 3 = 0 -> splitAndApplyRules ruleMap 3
        | _ -> failwith "Grid Problem"
    grid |> transformer

let run n data =
    let step =
        (data
         |> RuleMap.ofArray
         |> gridStep)

    let initialState = ".#./..#/###" |> Pattern.ofString

    let rec stepper state =
        seq {
            yield state
            yield! stepper (state |> step)
        }
    stepper initialState |> Seq.item n

let onOff c =
    if (c = '#') then 1
    else 0

let countOn lightMap = lightMap |> Seq.sumBy (Seq.sumBy onOff)
