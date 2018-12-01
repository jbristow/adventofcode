module Tests.Day25

open System
open NUnit.Framework
open Swensen.Unquote
open Day25

[<TestFixture>]
type Sample() =

    let sampleStates =
        [ "A",
          [ 0,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "B" }
            1,
            { Write = 0
              Move = -1
              Count = 1I
              NextState = "B" } ]
          |> Map.ofList
          "B",
          [ 0,
            { Write = 1
              Move = -1
              Count = 1I
              NextState = "A" }
            1,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "A" } ]
          |> Map.ofList ]
        |> Map.ofList

    [<Test>]
    member this.Part1() =
        let machine = runMachine 6I "A" sampleStates
        machine.Tape
        |> Map.toList
        |> List.sumBy snd
        =! 3

[<TestFixture>]
type Answer() =

    let answerStates =
        [ "A",
          [ 0,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "B" }
            1,
            { Write = 0
              Move = -1
              Count = 1I
              NextState = "F" } ]
          |> Map.ofList
          "B",
          [ 0,
            { Write = 0
              Move = 1
              Count = 1I
              NextState = "C" }
            1,
            { Write = 0
              Move = 1
              Count = 1I
              NextState = "D" } ]
          |> Map.ofList
          "C",
          [ 0,
            { Write = 1
              Move = -1
              Count = 1I
              NextState = "D" }
            1,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "E" } ]
          |> Map.ofList
          "D",
          [ 0,
            { Write = 0
              Move = -1
              Count = 1I
              NextState = "E" }
            1,
            { Write = 0
              Move = -1
              Count = 1I
              NextState = "D" } ]
          |> Map.ofList
          "E",
          [ 0,
            { Write = 0
              Move = 1
              Count = 1I
              NextState = "A" }
            1,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "C" } ]
          |> Map.ofList
          "F",
          [ 0,
            { Write = 1
              Move = -1
              Count = 1I
              NextState = "A" }
            1,
            { Write = 1
              Move = 1
              Count = 1I
              NextState = "A" } ]
          |> Map.ofList ]
        |> Map.ofList

    [<Test>]
    member this.Part1() =
        let machine = runMachine 12994925I "A" answerStates
        machine.Tape
        |> Map.toList
        |> List.sumBy snd
        =! 2846
