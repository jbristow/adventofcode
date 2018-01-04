module Day18

open System.Text.RegularExpressions

type Registers = Map<string, bigint>

module Registers =
    let getValue register registers =
        match registers |> Map.tryFind register with
        | Some x -> x
        | None -> 0I

    let parseArgument a rs =
        match a with
        | x when Regex.IsMatch(x, @"\d+") -> System.Numerics.BigInteger.Parse(x)
        | r -> rs |> getValue r
        | _ -> failwith (sprintf "Could not parse `%s`" a)

module Instruction =
    let mathF f =
        fun regs (line : string array) ->
            let a = line.[1]
            let b = line.[2]
            a,
            f (regs |> Registers.parseArgument a)
                (regs |> Registers.parseArgument b)

    let argValF n =
        fun regs (line : string array) ->
            regs |> Registers.parseArgument line.[n]

let instruction label registers line output =
    if (line |> Array.head) = label then Some(output registers line)
    else None

let (|SndC|_|) (line, regs) =
    (instruction "snd" regs line (Instruction.argValF 1))
let (|SetC|_|) (line, regs) =
    (instruction "set" regs line
         (fun r l -> l.[1], (r |> Registers.parseArgument l.[2])))
let (|AddC|_|) (line, regs) =
    (instruction "add" regs line (Instruction.mathF (+)))
let (|MulC|_|) (line, regs) =
    (instruction "mul" regs line (Instruction.mathF (*)))
let (|ModC|_|) (line, regs) =
    (instruction "mod" regs line (Instruction.mathF (%)))
let (|RcvC|_|) (line, regs) =
    (instruction "rcv" regs line (Instruction.argValF 1))
let (|JgzC|_|) (line, regs) =
    (instruction "jgz" regs line
         (fun r l ->
         r |> Registers.parseArgument l.[1],
         int (r |> Registers.parseArgument l.[2])))

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
    { LineNumber : int
      InputBuffer : bigint list
      Registers : Map<string, bigint>
      WaitingForInput : bool
      SentCount : int
      Finished : bool
      Id : int }

module Program =
    let jump n p = { p with LineNumber = p.LineNumber + n }
    let nextLine p = p |> jump 1
    let incSentCount p = { p with SentCount = p.SentCount + 1 }
    let updateRegister r v p = { p with Registers = p.Registers |> Map.add r v }

    let registerMath f x y p =
        p
        |> updateRegister x
               (f (p.Registers |> Registers.parseArgument x)
                    (p.Registers |> Registers.parseArgument y))
        |> nextLine

    let init n =
        { Finished = false
          Id = n
          InputBuffer = []
          LineNumber = 0
          Registers = [ "p", bigint n ] |> Map.ofSeq
          SentCount = 0
          WaitingForInput = false }


        
let (|SndPC|_|) (line : string array, p) =
    if (line |> Array.head) = "snd" then
        Some
            (p
             |> Program.incSentCount
             |> Program.nextLine,
             Some((p.Registers |> Registers.parseArgument line.[1])))
    else None

let (|SetPC|_|) (line : string array, p) =
    if (line |> Array.head) = "set" then
        Some(p
             |> Program.updateRegister line.[1]
                    (p.Registers |> Registers.parseArgument line.[2])
             |> Program.nextLine)
    else None

let (|AddPC|_|) (line : string array, p) =
    if (line |> Array.head) = "add" then
        Some(p |> Program.registerMath (+) line.[1] line.[2])
    else None

let (|MulPC|_|) (line : string array, p) =
    if (line |> Array.head) = "mul" then
        Some(p |> Program.registerMath (*) line.[1] line.[2])
    else None

let (|ModPC|_|) (line : string array, p) =
    if (line |> Array.head) = "mod" then
        Some(p |> Program.registerMath (%) line.[1] line.[2])
    else None

let (|RcvPC|_|) (line : string array, p) =
    match (line |> Array.head) = "rcv", p.InputBuffer with
    | true, input :: restInputBuffer ->
        Some({ p with InputBuffer = restInputBuffer
                      WaitingForInput = false }
             |> Program.updateRegister line.[1] input
             |> Program.nextLine)
    | true, [] -> Some({ p with WaitingForInput = true })
    | false, _ -> None

let (|JgzPC|_|) (line : string array, p) =
    if (line |> Array.head) = "jgz" then
        let n = (int (p.Registers |> Registers.parseArgument line.[2]))
        match (p.Registers |> Registers.parseArgument line.[1]) with
        | v when (v > 0I) -> Some(p |> Program.jump n)
        | v -> Some(p |> Program.nextLine)
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
    let lineLen = lines |> Array.length
    let inBounds lineN = lineN >= 0 && lineN < lineLen

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

    run2Programs' (Program.init 0) (Program.init 1)
