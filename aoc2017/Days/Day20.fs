module Day20

let add3 (x, y, z) (x', y', z') = (x + x', y + y', z + z')
let sum (x, y, z) = x + y + z

let parse3 (s : string) =
    let strlen = s.Length
    let intarr = s.[3..strlen - 2].Split(',') |> Array.map int
    intarr.[0], intarr.[1], intarr.[2]

let solveQuadratic a b c =
    let d = b * b - 4 * a * c
    match d with
    | x when x > 0 && a > 0 ->
        Some [ (float -b + sqrt (float x)) / (2.0 * float a)
               (-(float b) - sqrt (float x)) / (float (2 * a)) ]
    | x when x = 0 && a > 0 -> Some [ (float -b) / (2.0 * (float a)) ]
    | _ -> None

type Particle =
    { Position : int * int * int
      Velocity : int * int * int
      Acceleration : int * int * int
      Label : int }
    static member DistanceFrom0 { Position = (x, y, z) } = abs (x + y + z)
    static member GetPosition { Position = p } = p

    static member OfString label (s : string) =
        match s.Split([| ", " |], System.StringSplitOptions.None) with
        | [| p; v; a |] ->
            { Label = label
              Position = parse3 p
              Velocity = parse3 v
              Acceleration = parse3 a }
        | _ -> failwith (sprintf "Could not parse '%s'." s)

    static member AtTime t p =
        let (px, py, pz) = p.Position
        let (vx, vy, vz) = p.Velocity
        let (ax, ay, az) = p.Acceleration
        { p with Position =
                     (px + t * vx + ax * t * (t + 1) / 2,
                      py + t * vy + ay * t * (t + 1) / 2,
                      pz + t * vz + az * t * (t + 1) / 2)
                 Velocity = (vx + ax * t, vy + ay * t, vz + az * t) }

    static member CollidesWithAt p1 p2 =
        let (p1x, _, _) = p1.Position
        let (p2x, _, _) = p2.Position
        let (v1x, _, _) = p1.Velocity
        let (v2x, _, _) = p2.Velocity
        let (a1x, _, _) = p1.Acceleration
        let (a2x, _, _) = p2.Acceleration
        if a1x - a2x <> 0 then
            let tso =
                solveQuadratic (a1x - a2x) ((a1x - a2x) + 2 * (v1x - v2x))
                    (2 * (p1x - p2x))
            match tso with
            | Some ts ->
                ts |> List.filter (fun t -> (t >= 0.0) && (t = floor t))
            | None -> []
        else if (v1x - v2x) <> 0 then [ float ((p2x - p1x) / (v1x - v2x)) ]
        else []

let toParticleList (input : string array) =
    input
    |> Array.mapi Particle.OfString
    |> List.ofArray

let findOverallClosest input =
    let plist =
        input
        |> toParticleList
        |> List.sortBy (Particle.DistanceFrom0)
        |> List.rev
    (plist
     |> List.minBy
            (fun { Acceleration = (x, y, z) } -> abs (x) + abs (y) + abs (z)))

let demolitionDerby input =
    let plist = input |> toParticleList

    let rec loop ps colAt =
        match ps with
        | ph :: pt ->
            loop pt ((pt
                      |> List.collect (Particle.CollidesWithAt ph)
                      |> List.distinct
                      |> List.sort)
                     :: colAt)
        | [] -> colAt

    let collisions =
        loop plist []
        |> List.concat
        |> List.map int
        |> List.distinct
        |> List.sort
        |> List.filter (fun t -> t >= 0)

    let rec loop cpoints remaining =
        match cpoints with
        | t :: trest ->
            let uncollided =
                remaining
                |> List.groupBy (fun p ->
                       p
                       |> Particle.AtTime t
                       |> Particle.GetPosition)
                |> List.choose (fun a ->
                       if (snd a
                           |> List.length
                           = 1) then Some(snd a)
                       else None)
            loop trest (uncollided |> List.concat)
        | [] -> remaining

    loop collisions plist
