module Day25

type Action =
    { Write : int
      Move : int
      NextState : string
      Count : bigint }

type Machine =
    { Cursor : int
      State : string
      Tape : Map<int, int>
      Count : bigint }

module Machine =
    let value machine =
        match machine.Tape |> Map.tryFind machine.Cursor with
        | Some x -> x
        | None -> 0

let step states machine =
    let action =
        states
        |> Map.find machine.State
        |> Map.find (Machine.value machine)
    { Cursor = machine.Cursor + action.Move
      State = action.NextState
      Tape = machine.Tape |> Map.add machine.Cursor action.Write
      Count = machine.Count + action.Count }

let runMachine steps startingState states =
    let rec loop machine =
        if machine.Count < steps then loop (step states machine)
        else machine
    loop { Cursor = 0
           State = startingState
           Tape = [ 0, 0 ] |> Map.ofList
           Count = 0I }
