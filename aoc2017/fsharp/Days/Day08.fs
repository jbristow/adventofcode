module Day08

open System.Text.RegularExpressions

let instrPattern = @"([a-z]+) (inc|dec) (-?\d+) if ([a-z]+) ([<>=!]=?) (-?\d+)"

let (|Increment|Decrement|) (groups : string list) =
    match groups.[1] with
    | "inc" -> Increment(groups.[0], (int groups.[2]))
    | "dec" -> Decrement(groups.[0], (int groups.[2]))
    | _ -> failwith (sprintf "Bad instruction %s" groups.[1])

let (|Lt|Lte|Gt|Gte|Eq|Neq|) (groups : string list) =
    let value = int groups.[5]
    match groups.[4] with
    | "<" -> Lt(groups.[3], value)
    | "<=" -> Lte(groups.[3], value)
    | ">" -> Gt(groups.[3], value)
    | ">=" -> Gte(groups.[3], value)
    | "==" -> Eq(groups.[3], value)
    | "!=" -> Neq(groups.[3], value)
    | _ -> failwith (sprintf "Bad comparator %s" groups.[4])

let parseLine (registers : Map<string, int>, highest : int option)
    (line : string) : Map<string, int> * int option =
    let groups =
        Regex.Match(line, instrPattern).Groups
        |> Seq.cast
        |> Seq.map (fun (g : Group) -> g.Value)
        |> List.ofSeq
        |> List.tail

    let (opRegister, opValue) =
        match groups with
        | Increment(r, v) -> (r, v)
        | Decrement(r, v) -> (r, -v)

    let (compRegister, compFunc) =
        match groups with
        | Lt(r, v) -> (r, (>) v)
        | Lte(r, v) -> (r, (>=) v)
        | Gt(r, v) -> (r, (<) v)
        | Gte(r, v) -> (r, (<=) v)
        | Eq(r, v) -> (r, (=) v)
        | Neq(r, v) -> (r, (<>) v)

    let opRegisterCurrent =
        match registers.TryFind opRegister with
        | Some x -> x
        | None -> 0

    let compRegisterCurrent =
        match registers.TryFind compRegister with
        | Some x -> x
        | None -> 0

    let outputRegisters =
        if (compRegisterCurrent |> compFunc) then
            (registers |> Map.add opRegister (opRegisterCurrent + opValue))
        else registers

    let currentMax =
        if (outputRegisters |> Map.isEmpty) then None
        else
            Some(outputRegisters
                 |> Map.toSeq
                 |> Seq.sortBy snd
                 |> Seq.last
                 |> snd)

    let outputMax =
        match (highest, currentMax) with
        | Some x, Some cmx when x > cmx -> Some x
        | Some x, Some cmx -> Some cmx
        | None, Some cmx -> Some cmx
        | Some x, None -> Some x
        | None, None -> None

    outputRegisters, outputMax

let parseLines (registers : Map<string, int>) (lines : string array) : Map<string, int> * int option =
    lines
    |> List.ofArray
    |> List.fold parseLine (registers, None)
