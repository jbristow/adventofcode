:- begin_tests(day02).

  test(paper_2x3x4) :-
    paper(2,3,4,X),
    assertion(58 =:= X).
  test(paper_1x1x10) :-
    paper(1,1,10,X),
    assertion(43 =:= X).

  test(ribbon_2x3x4) :-
    ribbon(2,3,4,X),
    assertion(34 =:= X).
  test(ribbon_1x1x10) :-
    ribbon(1,1,10,X),
    assertion(14 =:= X).

  test(simple_parse_line) :-
    ribbon(2,3,4,X),
    parse_line(ribbon, "2x3x4", Y),
    assertion(X =:= Y).

  test(answer_part01) :-
    assertion(answer_day02_part1(1606483)).
  test(answer_part02) :-
    assertion(answer_day02_part2(3842356)).

:- end_tests(day02).
