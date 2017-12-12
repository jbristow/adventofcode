converter_acc([], Acc, Acc).
converter_acc([40|T], Acc, Final) :-
  NewAcc is Acc + 1,
  converter_acc(T, NewAcc, Final), !.
converter_acc([41|T], Acc, Final) :-
  NewAcc is Acc - 1,
  converter_acc(T, NewAcc, Final), !.
converter_acc([_|T], Acc, Final) :-
  converter_acc(T, Acc, Final).

converter(Input, Final) :-
  string_codes(Input, Codes),
  converter_acc(Codes, 0, Final).

when_basement_acc([41|_], 0, Count, Final) :-
  Final is Count + 1.
when_basement_acc([41|T], Floor, Count, Final) :-
  Floor > 0,
  NewFloor is Floor - 1,
  NewCount is Count + 1,
  when_basement_acc(T, NewFloor, NewCount, Final).
when_basement_acc([40|T], Floor, Count, Final) :-
  NewFloor is Floor + 1,
  NewCount is Count + 1,
  when_basement_acc(T, NewFloor, NewCount, Final).

when_basement(Input, Final) :-
  string_codes(Input, Codes),
  when_basement_acc(Codes, 0, 0, Final).
