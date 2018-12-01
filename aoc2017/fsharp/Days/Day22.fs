module Day22

type Direction =
    | North
    | South
    | East
    | West

type State =
    | Clean
    | Weakened
    | Infected
    | Flagged

module State =
    let update =
        function
        | Some Clean | None -> Weakened
        | Some Weakened -> Infected
        | Some Infected -> Flagged
        | Some Flagged -> Clean

    let toChar =
        function
        | Some Clean | None -> '.'
        | Some Weakened -> 'W'
        | Some Infected -> '#'
        | Some Flagged -> 'F'

type Coordinate = int * int

type Carrier =
    { Direction : Direction
      Position : int * int
      Infected : int }

module Direction =
    let delta =
        function
        | North -> (0, -1)
        | South -> (0, 1)
        | East -> (1, 0)
        | West -> (-1, 0)

    let turnRight =
        function
        | North -> East
        | East -> South
        | South -> West
        | West -> North

    let turnLeft =
        function
        | North -> West
        | East -> North
        | South -> East
        | West -> South

    let reverse =
        function
        | North -> South
        | South -> North
        | East -> West
        | West -> East

module Coordinate =
    let add (x1, y1) (x2, y2) = (x1 + x2, y1 + y2)

module Carrier =
    let init : Carrier =
        { Direction = North
          Position = 0, 0
          Infected = 0 }

    let updateInfected doInfect c : Carrier =
        if doInfect then { c with Infected = c.Infected + 1 }
        else c

    let updateDirection direction c : Carrier = { c with Direction = direction }
    let moveForward c : Carrier =
        { c with Position =
                     Coordinate.add c.Position (c.Direction |> Direction.delta) }

module Part1 =
    let burst grid carrier =
        let { Direction = d; Position = (x, y) } = carrier

        let newDirection, willInfect =
            match grid |> Map.tryFind (x, y) with
            | Some true -> d |> Direction.turnRight, false
            | Some false | None -> d |> Direction.turnLeft, true

        let nextGrid = grid |> Map.add (x, y) willInfect

        let nextCarrier =
            carrier
            |> Carrier.updateInfected willInfect
            |> Carrier.updateDirection newDirection
            |> Carrier.moveForward
        nextGrid, nextCarrier

    let isInfected c = c = '#'

    let parseInput (data : string array) =
        let size = data |> Array.length
        data
        |> Array.mapi
               (fun y row ->
               row
               |> Seq.mapi
                      (fun x node ->
                      ((x - size / 2, y - size / 2), isInfected node)))
        |> Seq.concat
        |> Map.ofSeq

    let simulate maxn data =
        let grid = data |> parseInput
        let carrier = Carrier.init

        let rec runner (g : Map<Coordinate, bool>) (c : Carrier) (n : int) =
            match n with
            | x when x < maxn ->
                let nextG, nextC = burst g c
                runner nextG nextC (n + 1)
            | _ -> (g, c)

        let _, finalC = runner grid carrier 0
        finalC

module Part2 =
    let burst grid carrier =
        let { Direction = d; Position = (x, y); Infected = inf } = carrier
        let nodeState = grid |> Map.tryFind (x, y)

        let newDirection =
            match nodeState with
            | Some Clean | None -> d |> Direction.turnLeft
            | Some Weakened -> d
            | Some Infected -> d |> Direction.turnRight
            | Some Flagged -> d |> Direction.reverse

        let nextNodeState = (nodeState |> State.update)
        let nextGrid = grid |> Map.add (x, y) nextNodeState

        let addInfected =
            if (nextNodeState = Infected) then inf + 1
            else inf

        let nextCarrier =
            { carrier with Infected = addInfected
                           Direction = newDirection
                           Position =
                               (x, y)
                               |> Coordinate.add
                                      (newDirection |> Direction.delta) }

        nextGrid, nextCarrier

    let isInfected c =
        if c = '#' then Some(Infected)
        else None

    let parseInput (data : string array) =
        let size = data |> Array.length
        data
        |> Array.mapi (fun y row ->
               row
               |> Seq.mapi (fun x node -> ((x, y), isInfected node))
               |> Seq.filter (fun z -> z
                                       |> snd
                                       <> None)
               |> Seq.map (fun (a, b) -> a, Option.get b))
        |> Seq.concat
        |> Map.ofSeq

    let display g =
      let minX = g |> Map.toList |> List.minBy (fst >> fst) |> (fst >> fst) |> (+) -3
      let maxX = g |> Map.toList |> List.maxBy (fst >> fst) |> (fst >> fst) |> (+) 3
      let minY = g |> Map.toList |> List.minBy (fst >> snd) |> (fst >> snd) |> (+) -3
      let maxY = g |> Map.toList |> List.maxBy (fst >> snd) |> (fst >> snd) |> (+) 3

      let dispg = [minY..maxY] |> List.map (fun y -> [minX..maxX] |> List.map (fun x -> State.toChar (g |> Map.tryFind (x,y))))
      sprintf "(%d,%d)->(%d,%d)\n%s" minX minY maxX maxY (System.String.Join("\n",dispg |> List.map (fun row -> System.String.Join("", row))))

    let simulate maxn data =
        let grid = data |> parseInput
        let size = data |> Array.length
        let carrier = { Carrier.init with Position = (size / 2, size / 2) }

        let rec runner (g : Map<Coordinate, State>) (c : Carrier) (n : int) =
            match n with
            | x when x < maxn ->
                let nextG, nextC = burst g c
                runner nextG nextC (n + 1)
            | _ -> (g, c)

        let finalG, finalC = runner grid carrier 0
        System.IO.File.WriteAllText("outputDay22.txt", display finalG)
        finalC
