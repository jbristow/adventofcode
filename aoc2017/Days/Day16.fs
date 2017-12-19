module Day16

open System.Text.RegularExpressions

let spin (size : int) (programs : string) : string =
    let len = String.length programs
    let newIndexes = [ size..size + len - 1 ] |> Seq.map (fun x -> x % len)
    Seq.zip newIndexes programs
    |> Seq.sortBy fst
    |> Seq.map snd
    |> Seq.fold (fun acc c -> acc + string c) ""

let exchange (a : int) (b : int) (programs : string) : string =
    if a = b then programs
    else 
        let x, y =
            if a > b then b, a
            else a, b
        
        let part1 = programs |> Seq.take x
        
        let xitem =
            programs
            |> Seq.skip x
            |> Seq.take 1
        
        let part2 =
            programs
            |> Seq.skip (x + 1)
            |> Seq.take (y - x - 1)
        
        let yitem =
            programs
            |> Seq.skip y
            |> Seq.take 1
        
        let part3 = programs |> Seq.skip (y + 1)
        Seq.concat [ part1; yitem; part2; xitem; part3 ] 
        |> Seq.fold (fun acc c -> acc + string c) ""

let partner (a : string) (b : string) (programs : string) : string =
    if a = b then programs
    else 
        let indexA = programs |> Seq.findIndex (fun c -> c = (a |> Seq.head))
        let indexB = programs |> Seq.findIndex (fun c -> c = (b |> Seq.head))
        exchange indexA indexB programs

let (|SpinMove|_|) moveStr =
    let re = Regex.Match(moveStr, @"s(\d+)")
    if re.Success then Some(int re.Groups.[1].Value)
    else None

let (|ExchangeMove|_|) moveStr =
    let re = Regex.Match(moveStr, @"x(\d+)/(\d+)")
    if re.Success then Some(int re.Groups.[1].Value, int re.Groups.[2].Value)
    else None

let (|PartnerMove|_|) moveStr =
    let re = Regex.Match(moveStr, @"p(\D)/(\D)")
    if re.Success then Some(re.Groups.[1].Value, re.Groups.[2].Value)
    else None

let parseMove (moveStr : string) (state : string) : string =
    match moveStr with
    | SpinMove x -> spin x state
    | ExchangeMove(a, b) -> exchange a b state
    | PartnerMove(a, b) -> partner a b state
    | _ -> failwith (sprintf "Unknown move '%s'" moveStr)

let dance (initial : string) (data : string) =
    data.Split(',') 
    |> Array.fold (fun acc moveStr -> (parseMove moveStr acc)) initial

let billionPosition (initial : string) (data : string) =
    let rec loopHunter startPos seen =
        match data |> dance startPos with
        | x when seen |> List.contains x -> 
            seen |> List.findIndex (fun a -> a = x), seen
        | x -> loopHunter x (x :: seen)
    
    let dupeN, seen = loopHunter initial [ initial ]
    let indexOfFinalState = ((dupeN - ((1000000000 - dupeN) % (dupeN + 1))) + 1)
    seen.[indexOfFinalState]
