module Day04

open System.Text.RegularExpressions

let isValidPassphrase f words  =
  let rec isValidPassphrase' words seen =
    match words with
    | w :: _ when (seen |> Set.contains (f w)) -> false
    | w :: ws -> (isValidPassphrase' ws (seen |> Set.add (f w)))
    | [] -> true
  isValidPassphrase' words Set.empty

let sorted (word:string) =
  word |> Seq.sort |> System.String.Concat

let isValidPassphraseDuplicates words =
  isValidPassphrase id words

let isValidPassphraseAnagram words =
  isValidPassphrase sorted words

let matchValue (m:Match) = m.Value

let wordSplit passphrase =
  Regex.Matches(passphrase, "(\w+)") |> Seq.cast |> Seq.map matchValue |> List.ofSeq

let countValid f (input:string list) =
  input |> List.map wordSplit |> List.filter f |> List.length
