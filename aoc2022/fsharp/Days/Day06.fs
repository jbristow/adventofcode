module Day06

let findFirstUniqueOfSize (size: int) (items: string) =
    let rec loop (cs: char list) (offset: int) (seen: char list) =
        if (seen |> List.length) = size then
            offset
        else
            let head = cs |> List.head
            let index = seen |> List.tryFindIndex (fun c -> c = head)

            let newSeen =
                match index with
                | Some i -> seen |> List.skip (i + 1)
                | None -> seen

            loop (cs |> List.skip 1) (offset + 1) (List.append newSeen [ head ])

    loop (items.ToCharArray() |> List.ofSeq) 0 List.Empty

let part1 (input: string) = input |> findFirstUniqueOfSize 4
let part2 (input: string) = input |> findFirstUniqueOfSize 14
