
:- begin_tests(day24).
  rlan_acc(_, Final, -1, "", Final).

  rlan_acc(Stream, Acc, 10, String, Final) :-
    atom_number(String, Number),
    read_string(Stream, "\n", "", NewEnd, NewString),
    rlan_acc(Stream, [Number|Acc], NewEnd, NewString, Final).

  read_lines_as_numbers(FileName, Output) :-
    open(FileName, read, InStream),
    read_string(InStream, "\n", "", End, String),
    rlan_acc(InStream, [], End, String, Output).

  :-  read_lines_as_numbers("../resources/day24.input", Numbers),
      nb_setval('FileInput', Numbers).

  test(sample_part1) :-
    legal_combos([1,2,3,4,5,7,8,9,10,11],3,QEnt),
    assertion(QEnt =:= 99).
  test(answer_part1) :-
    nb_getval('FileInput', FileInput),
    legal_combos(FileInput,3,QEnt),
    assertion(QEnt =:= 11846773891).

  test(sample_part2) :-
    legal_combos([1,2,3,4,5,7,8,9,10,11],4,QEnt),
    assertion(QEnt =:= 44).
  test(answer_part2) :-
    nb_getval('FileInput', FileInput),
    legal_combos(FileInput,4,QEnt),
    assertion(QEnt =:= 80393059).

:- end_tests(day24).
