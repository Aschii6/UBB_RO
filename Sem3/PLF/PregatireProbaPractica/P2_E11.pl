% prim(E: integer)

prim(2):-!.

prim(E):-
    E > 2,
    prim_aux(E, 2).

prim_aux(E, D):-
    E is D + 1,!.

prim_aux(E, D):-
    E mod D =\= 0,
    D1 is D + 1,
    prim_aux(E, D1).

dublarePrime([], []).

dublarePrime([H | T], [H, H | R]):-
    prim(H),!,
    dublarePrime(T, R).

dublarePrime([H | T], [H|R]):-
    dublarePrime(T, R).
