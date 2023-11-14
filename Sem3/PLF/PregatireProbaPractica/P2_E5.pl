% pozMax(L: list, R: list)
pozMax(L, R):-
    max(L, M),
    pozMax_aux(L, M, 1, R).

pozMax_aux([], _, _, []).

pozMax_aux([H|T], M, P, [P|R]):-
    P1 is P + 1,
    H =:= M,!,
    pozMax_aux(T, M, P1, R).

pozMax_aux([_|T], M, P, R):-
    P1 is P + 1,
    pozMax_aux(T, M, P1, R).


max([], 0).

max([H|T], H):-
    max(T, M1),
    H > M1,!.

max([_|T], M):-
    max(T, M).









