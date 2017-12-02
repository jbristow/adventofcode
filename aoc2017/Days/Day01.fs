module Day01

let allEq2 ((x, y) : char * char) : bool = x = y
let fstAsInt (x, _) = int (string x)

let antiCaptchaN (n:int) (input:string) =
    Seq.zip input (input.Substring(n) + input.Substring(0,n))
    |> Seq.filter allEq2
    |> Seq.map fstAsInt
    |> Seq.sum

let antiCaptcha (input:string) =
  let trimmed = input.Trim()
  antiCaptchaN 1 trimmed

let antiCaptchaPart2 (input:string) =
  let trimmed = input.Trim()
  antiCaptchaN ((trimmed |> String.length) / 2) trimmed
