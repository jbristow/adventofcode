:- begin_tests(day01).

  test(sample_part01_floor_0, condition(true)) :-
    converter('(())', X),
    X =:= 0.
  test(sample_part01_floor_0, condition(true)) :-
    converter('(((', X),
    X =:= 3.

  test(answer_part01, condition(true)) :-
    read_file_to_string("../resources/day01.input", Input, []),
    converter(Input, X),
    X =:= 232.

  test(answer_part02, condition(true)) :-
    read_file_to_string("../resources/day01.input", Input, []),
    when_basement(Input, X),
    format("~a~n",[X]),
    X =:= 1783.
:- end_tests(day01).
