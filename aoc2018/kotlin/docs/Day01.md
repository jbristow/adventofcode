## Day 1 - Chronal Calibration

Let's strap on our personal time machines and help save christmas again!

As is my custom, I began by making sure I knew how to read files in the
language I'm using this year.


```kotlin
object Day01 {
  fun answer1(input:List<String>) = "todo"

  fun answer2(input:List<String>) = "todo"

  @JvmStatic
  fun main(args: Array<String>) {
    val input = File("src/main/resources/day01.txt").readLines()
    println("answer 1: ${this.answer1(input)}")
    println("answer 2: ${this.answer2(input)}")
  }
}

```

This setup lets me start immediately without worrying about the types I'll need
coming out of `answer1` and `answer2`. I've wrapped everything in an `object`
because I don't want to have to change more than one or two characters when I
copy/paste into the net day's file. (Yes, I could make functions like
`day01answer1()` but I prefer to write things like `Day01.answer1()` instead.
Additionally, this means that each file can have its own `main` function and I
won't have to bother with kotlin/java packages.) 

Notes:
* `@JvmStatic` - This is required to drop a main function in a Kotlin
      `object`. Otherwise the JRE doesn't know how to find it.
* Due to the nature of the web input of the Advent of Code, all answers are
    probably going to be a `String` or an `Int`. I think it could possibly be a
    `Double`, but I don't remember any significant problem having floating
    point making a prominent appearance.
* It's important to be able to cycle quickly in AOC. Having the code open in
    one window while using another window to run the code really helps. I found
    myself moving fastest in Clojure using a repl to speed this up even
    further. In kotlin, I'm mostly going to be using `println` and some junit
    when things start getting complicated.

Ok, on to the first part.

### Part 1

Looking at the prompt, we see that we'll be getting input that looks like `+1
-2 +3 +1 -4`. From there, we have to start from zero and figure out what
"frequency" we end up on.

As a person who has participated in every AdventOfCode so far, day 1 tends to
be a starter question to get you used to the format. Compared to previous
years, this one is just a hair more straightforward, 2016 had the hardest one
so far, but 2015 is almost the same question hiding behind parentheses.

Ok, let's break this one down.

There are two problems to solve here:

* Parsing the input into numbers.
* Finding what frequency we end up on.

For parsing, I considered reaching for a regex, but I quickly realized that
this input was so regular that I could just use array indexing instead. Just
grab the first character, decide if we're positive or negative, and then just
convert the rest into a number. I was tempted to limit it, but a quick glance
at my input showed me a couple 2-4 digit numbers hiding amid the vast majority
of single digit
ones.
```kotlin
private fun String.parseN() = let { (head, tail) ->
    when (head) {
        '+' -> 1
        '-' -> -1
        else -> throw Exception("Illegal input!")
    } * tail.toInt()
}
```

You may have noticed my non-standard kotlin destructuring there. I got so used
to dealing with haskell and clojure that I feel a little lost when splitting a
list into its head and tail is more than a simple function. From now on, assume
that I've added the following for almost every list like thing we come across:

```kotlin
operator fun CharSequence.component1(): Char = head
operator fun CharSequence.component2(): CharSequence = tail
operator fun String.component2(): String = tail
```

Now that we have that out of the way, it's just a matter of stringing this together in a loop!

Wait! For-loops are evil. Let's use `map` instead!

```kotlin
input.map{ it.parseN() }.sum()
```

Double wait! Kotlin has a `sumBy` saving us a step!

```kotlin
input.sumBy{ it.parseN() }
```

And that's it! We now have our answer for part 1!

### Part 2

Now that we know where our frequency ends up after our input, the prompt for
question 2 informs us that our 993 line input cycles infinitely, so our answer
will be the first time that we see a repeated frequency. I reach for my old
functional standby `scan`... and I notice that kotlin doesn't have it.

Now `scan` is just a `fold` that keeps every accumulation step into a list. So
I whip that one up quick to see if I can just brute-force my way through.

```kotlin
fun <A, B> Sequence<A>.scan(initial: B, operation: (B, A) -> B) =
    fold(sequenceOf(initial)) { scanSequence, curr ->
        scanSequence + operation(scanSequence.last(), curr)
}
```

Oof. my code is lost to the fog of war surrounding my rush to get done.
Essentially it was trying to keep a record of everything seen so far, and
checking each step one-at-a-time. This didn't work, there were too many
comparisons being made. I hate to say that I was running down some very
unproductive ratholes, but I wasn't thinking too clearly about what was
happening here from a math perspective. After about an hour, seething in
frustration, I started printing out the first 5 rows and dropping them into
excel. This gave me two key insights:

* Since the cycle never changes, every 993 steps would add the answer to Part 1
    to my current total.  For example: `1,-2,3,-4,5` yields `1,-1,2,-2,3`.
    The second and third are: `4,2,5,1,6` `7,5,8,4,9`. If you want to see the
    137th iteration, you just add 3*137 to each item: `412,410,413,409,415`
* The min and max of each round wouldn't cross until 140+ iterations.

I switched my sequence generator from having to hold state at every step into
just holding the last set of 993. Then I saved everything I had already seen in
a `Set` (nicely backed by a O(1) access time `HashSet`). I then used the
`Set.intersect` function to quickly detect any overlaps, and since `intersect`
returns a set, I could just iterate up through the current round until I found
a member of the intersection.

```kotlin
fun answer2(input: List<String>): Int {
    val ns = input.map(String::parseN).scan(Int::plus)
    val ending = ns.last
    return (sequenceOf(listOf(0)) + generateSequence(ns) {
        it.map(ending::plus)
    }).findFirstDupe(emptySet())
}

tailrec fun Sequence<List<Int>>.findFirstDupe(seen: Set<Int>): Int {
    val (head, tail) = this
    val intersections = head intersect seen
    return when {
        intersections.isNotEmpty() -> head.find { it in intersections }!!
        else -> tail.findFirstDupe(seen + head)
    }
}
```

I used tailrec because this specific kind of recursive function can be
implemented by the compiler in a way that discards every other bit of scope as
it recurses. That way you're not leaving a potentially massive number of
function stacks behind you waiting until you finish the recursion to be garbage
collected. The `generateSequence` is very useful when doing coding puzzles. It
(and its siblings, like `iterate`) lazily generates an infinite series of
items based upon a function or seed list. I also only ever do the `scan` once,
since from there I just need to add the last value to everything.

From there, it gave me the answer.  
