module Day05

open System
open System.Text.RegularExpressions
open FSharpx.Text

type Instruction = { N: int; From: int; To: int }

let replace (m: Map<int, char list>) (fromCol: int) (toCol: int) ((fromList, toList): char list * char list) =
    m |> Map.add fromCol fromList |> Map.add toCol toList

let transfer' (num: int) (fromCol: char list) (toCol: char list) =
    let rec loop n f t =
        if (n > 0) then
            let newFrom = (f |> List.skip 1)
            let newTo = List.Cons(f.Head, t)
            loop (n - 1) newFrom newTo
        else
            (f, t)

    loop num fromCol toCol

let transfer (n: int) (fromCol: char list option) (toCol: char list option) =
    match fromCol, toCol with
    | Some f, Some t -> transfer' n f t
    | _, _ -> failwith "Unknown error invoking transfer"


type Crane =
    | CrateMover9000
    | CrateMover9001

    member this.DoWork (stacks: Map<int, char list>) (instruction: Instruction) =
        match this with
        | CrateMover9000 ->
            transfer instruction.N (stacks.TryFind instruction.From) (stacks.TryFind instruction.To)
            |> replace stacks instruction.From instruction.To
        | CrateMover9001 ->
            let fromCol, tempStack =
                transfer instruction.N (stacks.TryFind instruction.From) (Some <| List.empty)

            let _, toCol =
                transfer instruction.N (Some tempStack) (stacks.TryFind instruction.To)

            (fromCol, toCol) |> replace stacks instruction.From instruction.To


type CargoArea =
    { Stacks: Map<int, char list>
      Instructions: Instruction seq
      Crane: Crane }

let instrRegex = Regex(@"move (\d+) from (\d+) to (\d+)", RegexOptions.Compiled)
let colRegex = Regex(@"\d ", RegexOptions.Compiled)

let toInstruction (input: string) =
    let m = (input |> instrRegex.Match)

    { N = (int m.Groups[1].Value)
      From = (int m.Groups[2].Value)
      To = (int m.Groups[3].Value) }

let stackExtractor (col: int) (lines: string seq) =
    lines
    |> Seq.rev
    |> Seq.skip 1
    |> Seq.map (fun line -> line[col])
    |> Seq.filter (fun c -> c <> ' ')
    |> Seq.rev
    |> Seq.toList

let toKVTuples (lines: string seq) (label: int, col: int) : int * char list = (label, stackExtractor col lines)

let toStacks (diagram: string seq) =
    seq {
        for m in colRegex.Matches(diagram |> Seq.last) do
            yield m.Value.Trim() |> int, m.Index
    }
    |> Seq.map (toKVTuples diagram)
    |> Map.ofSeq

let partitionInput (input: string) =
    let inputParts =
        input.Split([| "\n\n" |], StringSplitOptions.RemoveEmptyEntries)
        |> Array.map Strings.toLines
        |> Array.take 2

    if inputParts |> Array.length <> 2 then
        failwith "inputParts has less than 2 elements"
    else
        inputParts[0], inputParts[1]

let parse (input: string) (crane: Crane) : CargoArea =
    let diagram, instrInput = partitionInput input

    { Stacks = toStacks diagram
      Instructions = (instrInput |> Seq.map toInstruction)
      Crane = crane }

let processInstructions (cargoArea: CargoArea) =
    cargoArea.Instructions |> Seq.fold cargoArea.Crane.DoWork cargoArea.Stacks

let part1 (input: string) =
    (parse input CrateMover9000)
    |> processInstructions
    |> Map.values
    |> Seq.map (fun cs -> (cs |> Seq.head).ToString())
    |> String.concat ""

let part2 (input: string) =
    (parse input CrateMover9001)
    |> processInstructions
    |> Map.values
    |> Seq.map (fun cs -> (cs |> Seq.head).ToString())
    |> String.concat ""
