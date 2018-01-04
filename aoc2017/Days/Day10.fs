module Day10

let range maxn = [ 0..maxn - 1 ]

let mutate (indexMap:Map<int,int>) (start,skip,length,listlen) =
    let endI = start + length - 1
    [start .. endI]
    |> List.mapi (fun i x -> (x % listlen), ((endI - i) % listlen))
    |> List.fold (fun m (a,b) ->
      m |> Map.add a (indexMap |> Map.find b) |> Map.add b (indexMap |> Map.find a)) indexMap

let prepDataStep (start,skip,prevLen,size) length =
      ((start+prevLen+skip)%size,skip+1,length,size)

let hashByInput n (input : string) =
    let lengths =
        input.Split(',')
        |> Array.map int
    let vals = lengths|> Array.tail |> Array.scan prepDataStep (0,0,Array.head lengths,n)
    vals |> Array.fold mutate (range n |> List.zip (range n) |> Map.ofList)

let stringEnder = [|17; 31; 73; 47; 23|] |> Array.map char |> System.String.Concat

let strToInt (input : string) = input |> Seq.map int |> Seq.toList

let intToHexStr x = sprintf "%02x" x

let secondXor kv = kv |> List.map snd |> List.reduce (^^^)

let hashByInputAscii input =
    let lengths =
        (input+stringEnder)
        |> String.replicate 64
        |> strToInt
    let vals = lengths |> List.tail |> List.scan prepDataStep (0,0,(lengths |> List.head),256)
    let numberMap = range 256 |> List.zip (range 256) |> Map.ofList
    let sparse = vals |> List.fold mutate numberMap

    let dense =
        sparse
        |> Map.toList
        |> List.chunkBySize 16
        |> List.map (secondXor >> intToHexStr)
    System.String.Join("", dense)
