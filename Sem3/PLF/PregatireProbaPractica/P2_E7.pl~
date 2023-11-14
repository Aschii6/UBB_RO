% produs(L: list, C: integer, R: list)
produs(L, C, R):-
    produs_aux(L, C, _, R).

produs_aux([], _, 0, []).

produs_aux([H|T], C, Carry, [F|R]):-
    produs_aux(T, C, Carry1, R),
    E is H * C + Carry1,
    F is E mod 10,
    Carry is E //10.
