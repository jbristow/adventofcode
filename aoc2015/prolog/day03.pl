move('>', [X,Y], [NX,Y]) :- NX is X + 1.
move('<', [X,Y], [NX,Y]) :- NX is X - 1.
move('^', [X,Y], [X,NY]) :- NY is Y + 1.
move('v', [X,Y], [X,NY]) :- NY is Y - 1.

char_to_coords([H|T], Pos, Acc, Out) :-
  move(H, Pos, NextC),
  NAcc = [NextC|Acc],
  char_to_coords(T, NextC, NAcc, Out).

char_to_coords([], _, Acc, Acc).

dual_char_to_coords([SH|[RH|T]], SPos, RPos, Acc, Out) :-
  move(SH, SPos, SNextC),
  move(RH, RPos, RNextC),
  NAcc = [SNextC|[RNextC|Acc]],
  dual_char_to_coords(T, SNextC, RNextC, NAcc, Out).

dual_char_to_coords([], _, _, Acc, Acc).

count_unique_acc([H|T], L, Acc, Final) :- 
  (memberchk(H, L) -> 
    (NL = L, NAcc = Acc);
    (NL = [H|L], NAcc is 1 + Acc)),
  count_unique_acc(T, NL, NAcc, Final).

count_unique_acc([], _, Acc, Acc).

count_unique(Coords, Output) :-
  count_unique_acc(Coords, [], 0, Output).

deliver_presents(Input, Output) :-
  string_chars(Input, CharList),
  char_to_coords(CharList, [0,0], [[0,0]], Coords),
  count_unique(Coords, Output).

dual_deliver_presents(Input, Output) :-
  string_chars(Input, CharList),
  A = [0,0],
  dual_char_to_coords(CharList, A, A, [A], Coords),
  count_unique(Coords, Output).

