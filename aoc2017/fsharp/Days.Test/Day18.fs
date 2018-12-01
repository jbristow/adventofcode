module Tests.Day18

open System
open NUnit.Framework
open Swensen.Unquote
open Day18

[<Test>]
let samplePart1() =
    let data =
        [| "set a 1"; "add a 2"; "mul a a"; "mod a 5"; "snd a"; "set a 0"; 
           "rcv a"; "jgz a -1"; "set a 1"; "jgz a -2" |]
    let _, _, _, recov = data |> runProgram
    recov =! [ 4I ]

[<Test>]
let answerPart1() =
    let data : string array = System.IO.File.ReadAllLines("day18.txt")
    let _, _, _, (recov : bigint list) = (data |> runProgram)
    recov =! [ 4601I ]

[<Test>]
let samplePart2() =
    let data =
        [| "snd 1"; "snd 2"; "snd p"; "rcv a"; "rcv b"; "rcv c"; "rcv d" |]
    let _, p1 = data |> run2Programs
    p1.SentCount =! 3

[<Test>]
let answerPart2() =
    let data : string array = System.IO.File.ReadAllLines("day18.txt")
    let _, p1 = data |> run2Programs
    p1.SentCount =! 6858
