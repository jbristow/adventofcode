module Day18

open System.Text.RegularExpressions

let getRegisterValue register registers =
    match registers |> Map.tryFind register with
    | Some x -> x
    | None -> 0I

let getValue (s : string) (registers : Map<string, bigint>) =
    match s with
    | x when Regex.IsMatch(x, @"\d+") -> System.Numerics.BigInteger.Parse(x)
    | r -> getRegisterValue r registers
    | _ -> failwith (sprintf "Could not parse `%s`" s)

let (|SndC|_|) (line, registers) =
    if (line |> Array.head) = "snd" then Some(registers |> getValue line.[1])
    else None

let (|SetC|_|) (line, registers) =
    if (line |> Array.head) = "set" then
        Some(line.[1], (registers |> getValue line.[2]))
    else None

let math f a b regs = a, f (regs |> getValue a) (regs |> getValue b)

let (|AddC|_|) (line, registers) =
    if (line |> Array.head) = "add" then
        Some(math (+) line.[1] line.[2] registers)
    else None

let (|MulC|_|) (line, registers) =
    if (line |> Array.head) = "mul" then
        Some(math (*) line.[1] line.[2] registers)
    else None

let (|ModC|_|) (line, registers) =
    if (line |> Array.head) = "mod" then
        Some(math (%) line.[1] line.[2] registers)
    else None

let (|RcvC|_|) (line, registers) =
    if (line |> Array.head) = "rcv" then Some(registers |> getValue line.[1])
    else None

let (|JgzC|_|) (line, registers) =
    if (line |> Array.head) = "jgz" then
        Some
            (registers |> getValue line.[1],
             int (registers |> getValue line.[2]))
    else None

let processLine (registers : Map<string, bigint>) lineNum lastSound
    recoveredSounds line =
    match (line, registers) with
    | SndC sound -> registers, lineNum + 1, sound, recoveredSounds
    | SetC(r, v) | AddC(r, v) | MulC(r, v) | ModC(r, v) ->
        (registers |> Map.add r v), lineNum + 1, lastSound, recoveredSounds
    | RcvC(x) when x <> 0I ->
        registers, lineNum + 1, lastSound, lastSound :: recoveredSounds
    | JgzC(x, y) when x > 0I ->
        registers, lineNum + y, lastSound, recoveredSounds
    | _ -> registers, lineNum + 1, lastSound, recoveredSounds

let runProgram (input : string array) =
    let lines = input |> Array.map (fun s -> s.Split([| ' ' |]))

    let rec runProgram' registers lineNum lastSound
            (recoveredSounds : bigint list) =
        let registers', lineNum', lastSound', recSounds' =
            processLine registers lineNum lastSound recoveredSounds
                lines.[lineNum]
        if lineNum' <= (lines |> Array.length) && lineNum' >= 0
           && (recSounds' |> List.isEmpty) then
            runProgram' registers' lineNum' lastSound' recSounds'
        else (registers, lineNum', lastSound', recSounds')
    runProgram' Map.empty 0 -1I []

type Program =
    { LineNumber : bigint
      InputBuffer : bigint list
      Registers : Map<string, bigint>
      WaitingForInput : bool
      SentCount : int
      Finished : bool
      Id : int }

let createProgram n =
    { Finished = false
      Id = n
      InputBuffer = []
      LineNumber = 0I
      Registers = [ "p", bigint n ] |> Map.ofSeq
      SentCount = 0
      WaitingForInput = false }

let (|SndPC|_|) (line : string array, p) =
    if (line |> Array.head) = "snd" then
        Some
            ({ p with LineNumber = p.LineNumber + 1I
                      SentCount = p.SentCount + 1 },
             Some((p.Registers |> getValue line.[1])))
    else None

let programSetRegister p x xVal =
    { p with LineNumber = p.LineNumber + 1I
             Registers = p.Registers |> Map.add x xVal }

let (|SetPC|_|) (line : string array, p) =
    if (line |> Array.head) = "set" then
        Some(programSetRegister p line.[1] (p.Registers |> getValue line.[2]))
    else None

let updateProgramWithMath p f x y =
    { p with LineNumber = p.LineNumber + 1I
             Registers =
                 (p.Registers
                  |> Map.add x
                         (f (p.Registers |> getValue (x))
                              (p.Registers |> getValue (y)))) }

let (|AddPC|_|) (line : string array, p) =
    if (line |> Array.head) = "add" then
        Some(updateProgramWithMath p (+) line.[1] line.[2])
    else None

let (|MulPC|_|) (line : string array, p) =
    if (line |> Array.head) = "mul" then
        Some(updateProgramWithMath p (*) line.[1] line.[2])
    else None

let (|ModPC|_|) (line : string array, p) =
    if (line |> Array.head) = "mod" then
        Some(updateProgramWithMath p (%) line.[1] line.[2])
    else None

let (|RcvPC|_|) (line : string array, p) =
    match (line |> Array.head) = "rcv", p.InputBuffer with
    | true, input :: restInputBuffer ->
        Some
            ({ programSetRegister p line.[1] input with WaitingForInput = false
                                                        InputBuffer =
                                                            restInputBuffer })
    | true, [] -> Some({ p with WaitingForInput = true })
    | false, _ -> None

let (|JgzPC|_|) (line : string array, p) =
    if (line |> Array.head) = "jgz" then
        match (p.Registers |> getValue line.[1]) with
        | v when (v > 0I) ->
            let jump = (p.Registers |> getValue line.[2])
            Some { p with LineNumber = p.LineNumber + jump }
        | v -> Some { p with LineNumber = p.LineNumber + 1I }
    else None

let processLine2 p (line : string array) : Program * bigint option =
    match (line, p) with
    | SndPC(nextP, toSend) -> (nextP, toSend)
    | SetPC(nextP) | AddPC(nextP) | MulPC(nextP) | ModPC(nextP) | RcvPC(nextP) | JgzPC(nextP) ->
        (nextP, None)
    | line, _ ->
        failwith (sprintf "Bad instruction. Line: %O `%A`" p.LineNumber line)

let run2Programs (input : string array) =
    let lines = input |> Array.map (fun s -> s.Split([| ' ' |]))

    let lineLen =
        lines
        |> Array.length
        |> bigint

    let inBounds lineN = lineN >= 0I && lineN < lineLen

    let runProgram p =
        if (p.Finished || (p.WaitingForInput && p.InputBuffer |> List.isEmpty)) then
            p, None
        else
            let { LineNumber = ln } as nextP, output =
                processLine2 p lines.[int p.LineNumber]
            if inBounds ln then (nextP, output)
            else ({ nextP with Finished = true }, output)

    let rec run2Programs' p1 p2 =
        let nextP1, sendToP2 = runProgram p1
        let nextP2, sendToP1 = runProgram p2

        let nextP1' =
            match sendToP1 with
            | Some i ->
                { nextP1 with InputBuffer = (nextP1.InputBuffer @ [ i ]) }
            | None -> nextP1

        let nextP2' =
            match sendToP2 with
            | Some i ->
                { nextP2 with InputBuffer = (nextP2.InputBuffer @ [ i ]) }
            | None -> nextP2

        if (nextP1'.WaitingForInput && nextP2'.WaitingForInput)
           || (nextP1'.Finished && nextP2'.Finished) then (nextP1', nextP2')
        else run2Programs' nextP1' nextP2'

    run2Programs' (createProgram 0) (createProgram 1)
