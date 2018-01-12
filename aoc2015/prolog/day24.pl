comb(0, _, []).
comb(N, [H|T], [H|Cs]) :-
  N>0,
  NDec is N-1,
  comb(NDec, T, Cs).
comb(N, [_|T], Cs) :-
  N>0,
  comb(N, T, Cs).

mult(A, B, Product) :-
  Product is A * B.

product(L, P) :-
  foldl(mult, L, 1, P).

min_entangle([], A, A).
min_entangle([Combo|Combos], MinP, FinalMinP) :-
  (   product(Combo, P),
      P<MinP
  ->  min_entangle(Combos, P, FinalMinP)
  ;   min_entangle(Combos, MinP, FinalMinP)
  ).


find_min_entanglement([], []).
find_min_entanglement([H|T], MinQEnt) :-
  product(H, P),
  min_entangle(T, P, MinQEnt).

ok_weight(A, L) :-
  sum_list(L, A).

find_first_with_sum(List, [ComboLength|T], OkWeight, ShortestOkCombos) :-
  (   findall(C, comb(ComboLength, List, C), Combos),
      include(ok_weight(OkWeight), Combos, F),
      F\=[]
  ->  ShortestOkCombos=F
  ;   find_first_with_sum(List, T, OkWeight, ShortestOkCombos)
  ).

legal_combos(List, Groups, MinQEnt) :-
  length(List, ListLen),
  findall(Index, between(1, ListLen, Index), Indexes),
  sum_list(List, ListSum),
  OkWeight is ListSum/Groups,
  find_first_with_sum(List, Indexes, OkWeight, ShortestOkCombos),
  find_min_entanglement(ShortestOkCombos, MinQEnt).

