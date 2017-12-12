paper(L, W, H, Total) :-
  min_list([L*W, L*H, W*H], M),
  Total is 2 * L * W + 2 * W * H + 2 * H * L + M.

ribbon(L, W, H, Total) :-
  min_list([L+W, L+H, W+H], M),
  Total is L*W*H + 2*M.

parse_line(Goal, Input, Total) :-
  split_string(Input, "x", "", S),
  maplist(atom_number, S, [A,B,C]),
  call(Goal,A,B,C,Total).

answer_acc(_, _, Final, -1, "", Final) :- !.

answer_acc(Goal, Stream, Acc, 10, String, Final) :-
  parse_line(Goal, String, X),
  NewAcc is X + Acc,
  read_string(Stream, "\n", "", NewEnd, NewString),
  answer_acc(Goal, Stream, NewAcc, NewEnd, NewString, Final).

answer(Goal, Output) :-
  open("../resources/day02.input", read, InStream),
  read_string(InStream, "\n", "", End, String),
  answer_acc(Goal, InStream, 0, End, String, Output).

answer_day02_part1(Output) :-
  answer(paper, Output).

answer_day02_part2(Output) :-
  answer(ribbon, Output).

