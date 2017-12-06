module Test.Day03

open System
open NUnit.Framework
open Swensen.Unquote
open Day03

let spiral3x3 =
    [ [ (0, 0), 5
        (1, 0), 4
        (2, 0), 3 ]
      [ (0, 1), 6
        (1, 1), 1
        (2, 1), 2 ]
      [ (0, 2), 7
        (1, 2), 8
        (2, 2), 9 ] ]

let spiral5x5 =
    [ [ (0, 0), 17
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

[<Test>]
let ``spiral test placeholder``() = spiralContaining 3 =! spiral3x3

[<Test>]
let ``spiral test placeholder even``() = spiralContaining 10 =! spiral5x5

[<TestCase(1, ExpectedResult = 0)>]
[<TestCase(12, ExpectedResult = 3)>]
[<TestCase(23, ExpectedResult = 2)>]
[<TestCase(1024, ExpectedResult = 31)>]
let ``tests derived from day03-part01 examples`` (input) = findDistance 1 input

[<Test>]
let ``Answer for day03-part01``() = findDistance 1 289326 =! 419

[<Test>]
let ``Answer for day03-part02``() = findGtInMemory 50 289326 =! int64 295229
