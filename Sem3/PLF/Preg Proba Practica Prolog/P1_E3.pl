% b.Sa se scrie o functie care descompune o lista de numere intr-o lista
% de forma [ lista-de-numere-pare lista-de-numere-impare](deci lista cu
% doua elemente care sunt liste de intregi), si va intoarce si numarul
% elementelor pare si impare.

% descomp(L: list, R: list, P: integer, I: integer)
descomp(L, [R1, R2], P, I):- % [R1 | [R2]]
    descomp1(L, R1, P),
    descomp2(L, R2, I).

descomp1([], [], 0).

descomp1([H|T], [H|R1], P):-
    H mod 2 =:= 0,!,
    descomp1(T, R1, P1),
    P is P1 + 1.

descomp1([_|T], R, P):-
    descomp1(T, R, P).

descomp2([], [], 0).

descomp2([H|T], [H|R2], I):-
    H mod 2 =:= 1,!,
    descomp2(T, R2, I1),
    I is I1 + 1.

descomp2([_|T], R, I):-
    descomp2(T, R, I).

