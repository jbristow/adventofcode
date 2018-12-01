module Day15

let modNumber = 2147483647UL

let compare ((a : uint64), (b : uint64)) = (65535UL &&& a) = (65535UL &&& b)

let generator factor (start : int) =
    let rec loop prev =
        let next = (uint64 factor * uint64 prev) % modNumber
        seq {
            yield next
            yield! loop next
        }
    loop (uint64 start)

let matchCount aStart bStart n =
    let a = generator 16807 aStart
    let b = generator 48271 bStart
    Seq.zip a b
    |> Seq.take n
    |> Seq.filter compare

let generatorBeta factor (start : int) (mult : int) =
    let rec loop prev =
        let next = (uint64 factor * uint64 prev) % modNumber
        if next % uint64 mult = 0UL then
            seq {
                yield next
                yield! loop next
            }
        else loop next
    loop (uint64 start)

let matchCountBeta aStart bStart n =
    let a = generatorBeta 16807 aStart 4
    let b = generatorBeta 48271 bStart 8
    Seq.zip a b
    |> Seq.take n
    |> Seq.filter compare
