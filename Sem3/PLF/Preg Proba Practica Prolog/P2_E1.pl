% Definiti un predicat care determina suma a doua numere scrise in
% reprezentare de lista.

dim([], 0):-!.

dim([_ | T], R):-
    dim(T, R1),
    R is R1 + 1.

extindeCu0(0, L, L):-!.

extindeCu0(N, L, [0|R]):-
    N > 0,
    N1 is N - 1,
    extindeCu0(N1, L, R).

sumaL(L1, L2, R):-
    dim(L1, D1),
    dim(L2, D2),
    D1 > D2,!,
    D is D1 - D2,
    extindeCu0(D, L2, NewL2),
    sumaPeListe(L1, NewL2, _, R).

sumaL(L1, L2, R):-
    dim(L1, D1),
    dim(L2, D2),
    D is D2 - D1,
    extindeCu0(D, L1, NewL1),
    sumaPeListe(NewL1, L2, _, R).

% sumaPeListe(L1: list, L2: list, C: integer, R: list)
% sumaPeListe([], L2, 0, L2).
% sumaPeListe(L1, [], 0, L1).

sumaPeListe([], [], 0, []).

sumaPeListe([H1 | T1], [H2 | T2], C, [H | R1]):-
    sumaPeListe(T1, T2, C1, R1),
    T is H1 + H2 + C1,
    H is T mod 10,
    C is T // 10.












