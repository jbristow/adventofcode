module Day10

let range maxn = [ 0..maxn - 1 ]
let rotate n col = (List.skip n col) @ (List.take n col)

let unrotate n col =
    let len = col |> List.length
    (List.skip (len - n) col) @ (List.take (len - n) col)

let mutate (start, skip, col) length =
    let rotated = rotate start col

    let revved =
        rotated
        |> List.take length
        |> List.rev

    let rest = rotated |> List.skip length
    ((start + length + skip) % (List.length col), skip + 1,
     (revved @ rest) |> unrotate start)

let hashByInput n (input : string) =
    let _, _, output =
        input.Split(',')
        |> List.ofArray
        |> List.map int
        |> List.fold mutate (0, 0, range n)
    output

let strToInt (input : string) =
    let output =
        input
        |> List.ofSeq
        |> List.map int
    [ 17; 31; 73; 47; 23 ] |> List.append output

let intToHexStr x = sprintf "%02x" x

let hashByInputAscii input =
    let lengths =
        strToInt input
        |> List.replicate 64
        |> List.concat

    let _, _, sparse = lengths |> List.fold mutate (0, 0, range 256)

    let dense =
        sparse
        |> List.chunkBySize 16
        |> List.map ((List.reduce (^^^)) >> intToHexStr)
    System.String.Join("", dense)
