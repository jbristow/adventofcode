module ``Tests for Day 03``

open System
open NUnit.Framework
open FsUnit
open Day03

type InitMsgUtils() =
    inherit FSharpCustomMessageFormatter()

let shouldEqual (x : 'a) (y : 'a) =
    Assert.AreEqual(x, y, sprintf "Expected: %A\nActual: %A" x y)

[<Test>]
let ``spiral test placeholder``() =
    spiralContaining 3 |> shouldEqual [ [ (0, 0), 5
                                          (1, 0), 4
                                          (2, 0), 3 ]
                                        [ (0, 1), 6
                                          (1, 1), 1
                                          (2, 1), 2 ]
                                        [ (0, 2), 7
                                          (1, 2), 8
                                          (2, 2), 9 ] ]

[<Test>]
let ``spiral test placeholder even``() =
    spiralContaining 10 |> shouldEqual [ [ (0, 0), 17
                                           (1, 0), 16
                                           (2, 0), 15
                                           (3, 0), 14
                                           (4, 0), 13 ]
                                         [ (0, 1), 18
                                           (1, 1), 5
                                           (2, 1), 4
                                           (3, 1), 3
                                           (4, 1), 12 ]
                                         [ (0, 2), 19
                                           (1, 2), 6
                                           (2, 2), 1
                                           (3, 2), 2
                                           (4, 2), 11 ]
                                         [ (0, 3), 20
                                           (1, 3), 7
                                           (2, 3), 8
                                           (3, 3), 9
                                           (4, 3), 10 ]
                                         [ (0, 4), 21
                                           (1, 4), 22
                                           (2, 4), 23
                                           (3, 4), 24
                                           (4, 4), 25 ] ]

[<TestCase(1, ExpectedResult = 0)>]
[<TestCase(12, ExpectedResult = 3)>]
[<TestCase(23, ExpectedResult = 2)>]
[<TestCase(1024, ExpectedResult = 31)>]
let ``Testing input.`` (input) = findDistance 1 input

[<Test>]
let ``Answer 1:``() = findDistance 1 289326 |> shouldEqual 419

[<Test>]
let ``fillMemory test``() =
    findGtInMemory 50 289326 |> shouldEqual (int64 295229)
