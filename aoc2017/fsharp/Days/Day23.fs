module Day23

open System.Text.RegularExpressions
open Day18

let (|SubPC|_|) (line : string array, (p : Program)) =
    if (line |> Array.head) = "sub" then 
        Some(p |> Program.registerMath (-) line.[1] line.[2])
    else None

let (|JnzPC|_|) (line : string array, (p : Program)) =
    if (line |> Array.head) = "jnz" then 
        let n = int (p.Registers |> Registers.parseArgument line.[2])
        match (p.Registers |> Registers.parseArgument line.[1]) with
        | v when (v <> 0I) -> Some(p |> Program.jump n)
        | v -> Some(p |> Program.nextLine)
    else None

let processLine (program : Program) mulN line : Program * int =
    match (line, program) with
    | SetPC(nextP) | SubPC(nextP) | JnzPC(nextP) -> (nextP, mulN)
    | MulPC(nextP) -> (nextP, mulN + 1)
    | _ -> failwith (sprintf "Unknown line: %s" (System.String.Join(" ", line)))

let runProgram runAsDebug (input : string array) =
    let lines = input |> Array.map (fun s -> s.Split([| ' ' |]))
    
    let rec runProgram' program mulN =
        let program', mulN = processLine program mulN lines.[program.LineNumber]
        match program' with
        | { LineNumber = ln } as p when ln < (lines |> Array.length) && ln >= 0 -> 
            runProgram' p mulN
        | p -> p, mulN
    
    let programState =
        if runAsDebug then Program.init 0
        else { Program.init 0 with Registers = [ "a", 1I ] |> Map.ofList }
    
    runProgram' programState 0

let numberOfMulCalls = (67 - 2) * (67 - 2)

let valueOfRegisterH =
    let b = 67 * 100 + 100000
    let c = b + 17000
    [ b..17..c ]
    |> List.filter (fun b -> [ 2..b - 1 ] |> List.exists (fun d -> b % d = 0))
    |> List.length
