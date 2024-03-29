% Sa se determine cea mai lunga secventa de numere pare consecutive
% dintr-o lista (daca sunt mai multe secvente de lungime maxima, una
% dintre ele).

% secvPare(L: list, R: list)
secvPare(L, R):-
    secvPare_aux(L, [], R1),
    my_reverse(R1, [], R).

% secvPare_aux(L: lista data, P: lista de pare cu care merg, R: lista
% cea mai mare)
secvPare_aux([], P, P).

secvPare_aux([H | T], P, R):-
    H mod 2 =:= 0,!,
    secvPare_aux(T, [H | P], R).

secvPare_aux([_ | T], P, P):-
    secvPare_aux(T, [], R),
    dim(P, C1),
    dim(R, C2),
    C1 > C2,!.

secvPare_aux([_ | T], _, R):-
    secvPare_aux(T, [], R).

dim([], 0).

dim([_|T], C):-
    dim(T, C1),
    C is C1 + 1.

my_reverse([], R, R).

my_reverse([H|T], P, R):-
    my_reverse(T, [H|P], R).
