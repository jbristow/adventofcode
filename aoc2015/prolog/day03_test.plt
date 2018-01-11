:-  open("../resources/day03.input", read, InStream),
    read_string(InStream, "\n", "", _, String), 
    nb_setval('FileInput', String).

:- begin_tests(day03).


  test(sample_part1_1) :-
    deliver_presents(">",X),
    assertion(2 =:= X).
  test(sample_part1_2) :-
    deliver_presents("^>v<",X),
    assertion(4 =:= X).
  test(sample_part1_3) :-
    deliver_presents("^v^v^v^v^v",X),
    assertion(2 =:= X).
  test(answer_part1_4) :-
    nb_getval('FileInput', Input),
    deliver_presents(Input,X),
    assertion(2572 =:= X).

  test(sample_part2_1) :-
    dual_deliver_presents("^v",X),
    assertion(3 =:= X).
  test(sample_part2_2) :-
    dual_deliver_presents("^>v<",X),
    assertion(3 =:= X).
  test(sample_part2_3) :-
    dual_deliver_presents("^v^v^v^v^v",X),
    assertion(11 =:= X).
  test(answer_part2_4) :-
    nb_getval('FileInput', Input),
    dual_deliver_presents(Input,X),
    assertion(2631 =:= X).
:- end_tests(day03).
