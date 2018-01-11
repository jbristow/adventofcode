:- begin_tests(day04).

  test(sample_part1_abcdef) :-
    find_first_legal("abcdef", 0, N),
    assertion(609043 =:= N).
  test(sample_part1_pqrstuv) :-
    find_first_legal("pqrstuv", 0, N),
    assertion(1048970 =:= N).
  test(answer_part1) :-
    find_first_legal("yzbqklnj", 0, N),
    assertion(282749 =:= N).
  test(answer_part2) :-
    find_first_legal6("yzbqklnj", 0, N),
    assertion(9962624 =:= N).

:- end_tests(day04).
