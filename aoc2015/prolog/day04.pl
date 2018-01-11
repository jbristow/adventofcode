legal_hash(K, ZStr, N) :-
  string_length(ZStr, ZCount),
  integer(N),
  atom_string(N, NStr),
  string_concat(K, NStr, KN),
  md5_hash(KN, KNHash, []),
  sub_string(KNHash, 0, ZCount, _, ZStr).

find_first_legal(K, N, Out) :-
  legal_hash(K, "00000", N) ->
    Out = N;
    (NextN is N + 1,
     find_first_legal(K, NextN, Out)).

find_first_legal6(K, N, Out) :-
  legal_hash(K, "000000", N) ->
    Out = N;
    (NextN is N + 1,
     find_first_legal6(K, NextN, Out)).
