% vale(L: list, R: integer)
% L - lista de numere primite
% R - rezultatul (va fi 0 sau 1)
% model de flux - (i, o) sau (i, i)
vale([H1, H2 | T], R):-
    H1 > H2,
    vale_aux([H1, H2 | T], 0, R1),
    R is R1.

vale([H1, H2 | _], 0):-!,
    H1 =< H2.

vale(_, 0).

% vale_aux(L: lista, C: integer, R: integer)
% L - lista de nr primite
% C - poate fi 0 sau 1 semnfica daca s-a ajuns la un punct de minim
% local
% R - rezultat
% model de flux - (i,i,o) sau (i,i,i)
vale_aux([], 0, 0):-!. % lista e vida

vale_aux([_ | []], 0, 0):-!. % lista mai are un elem si nu are structura de vale

vale_aux([_ | []], 1, 1):-!. % lista mai are un elem si are structura de vale

vale_aux([E1, E2 | T], 0, R):- % inca nu s-a ajuns la cel mai jos punct din vale
    E1 > E2,
    vale_aux([E2 | T], 0, NewR),
    R is NewR.

vale_aux([E1, E2 | _], 1, 0):- % a inceput sa urce la deal, dar a dat iar de vale
    E1 > E2.

vale_aux([E1, E2 | T], 0, R):- % s-a coborat pana la vale
    E1 < E2,
    vale_aux([E2 | T], 1, NewR),
    R is NewR.

vale_aux([E1, E2 | T], 1, R):- % se urca la deal
    E1 < E2,
    vale_aux([E2 | T], 1, NewR),
    R is NewR.










