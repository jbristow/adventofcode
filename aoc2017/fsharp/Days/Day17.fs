module Day17

let spin n (curr, lastn, buffer) =
    let splitVal = ((curr + n) % (lastn + 1)) + 1
    let a, b = buffer |> List.splitAt splitVal
    splitVal, lastn + 1, a @ [ lastn + 1 ] @ b

let noBuffSpin n (curr, lastn, last1) =
    let splitVal = ((curr + n) % (lastn + 1)) + 1

    let nextLast1 =
        if splitVal = 1 then lastn + 1
        else last1
    splitVal, lastn + 1, nextLast1

let spinner n f initial =
    let rec loop prev =
        let next = f n prev
        seq {
            yield next
            yield! loop next
        }
    seq {
        yield initial
        yield! loop initial
    }

let bufferedSpinner n : seq<int * int * int list> = spinner n spin (0, 0, [ 0 ])
let unbufferedSpinner n : seq<int * int * int> = spinner n noBuffSpin (0, 0, 0)
