% listaDiv(E: integer, R: list)
listaDiv(E, R):-
    E > 2,!,
    listaDiv_aux(E, 2, R).

listaDiv(_, []).

listaDiv_aux(E, D, []):-
    E is D + 1,!.

listaDiv_aux(E, D, [D | R]):-
    E mod D =:= 0,!,
    D1 is D + 1,
    listaDiv_aux(E, D1, R).

listaDiv_aux(E, D, R):-
    D1 is D + 1,
    listaDiv_aux(E, D1, R).

insereazaDiv([], []).

insereazaDiv([H|T], [H|R]):-
    insereazaDiv(T, R1),
    listaDiv(H, R2),
    insereaza(R2, R1, R).

insereaza([], R2, R2).

insereaza([H|T], R2, [H|R]):-
    insereaza(T, R2, R).
