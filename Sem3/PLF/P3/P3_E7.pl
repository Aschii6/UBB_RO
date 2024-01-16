% insereaza(E: integer, L: list, R: list)
% E - elementul de inserat (pe toate pozitiile, pe rand)
% L - lista in care se insereaza
% R - rezultatul
% model de flux - (i, i, o) nedeterminist sau (o, o, i), (i, o, i),
% (o, i, o)
insereaza(E,L,[E|L]).
insereaza(E,[H|T],[H|Rez]):-
    insereaza(E,T,Rez).

% aranjamente(L: list, K: integer, R: list)
% L - lista pentru care se fac aranjamente
% K - numarul de elemente care se aranjeaza
% R - aranjamentele
% model de flux - (i, i, o) nedeterminist
aranjamente([H|_], 1, [H]).

aranjamente([_|T], K, R):-
    aranjamente(T, K, R).

aranjamente([H|T], K, R):-
    K > 1,
    K1 is K - 1,
    aranjamente(T, K1, R1),
    insereaza(H, R1, R).

solutii_aranjamente(L, K):-
    findall(R, aranjamente(L, K, R), Sol),
    write(Sol).


