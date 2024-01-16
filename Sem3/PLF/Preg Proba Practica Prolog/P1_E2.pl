% a. Sa se scrie un predicat care determina cel mai mic multiplu comun
% al elementelor unei liste formate din numere intregi

% cmmmc(L: list, R: integer)
cmmmc([H], H):-!.

cmmmc([H|T], R):-
    cmmmc(T, R1),
    aux(H, R1, R).

% aux(E1: integer, E2: integer, R: integer)
aux(E1, E2, R):-
    cmmdc(E1, E2, R1),
    R is E1 * (E2 / R1).

cmmdc(E1, E2, E1):-
    E1 =:= E2,!.

cmmdc(E1,E2,R):-
    E1 > E2,!,
    E3 is E1 - E2,
    cmmdc(E3, E2, R).

cmmdc(E1, E2, R):-
    E3 is E2 - E1,
    cmmdc(E1, E3, R).

% b. Sa se scrie un predicat care adauga dupa 1-ul, al 2-lea, al 4-lea,
% al 8-lea s.a.m.d element al unei liste o valoare v data

insert2Main(L, V, R):-
    insert2(L, V, 1, R).

% insert2(L: list, V:integer, K: integer, R: list)
insert2([], V, K, [V]):- % cred ca poate fi scos
    pow2(K),!.

insert2([], _, _, []).

insert2([H | T], V, K, [H, V | R1]):-
    pow2(K),!,
    K1 is K + 1,
    insert2(T, V, K1, R1).

insert2([H | T], V, K, [H | R1]):-
    K1 is K + 1,
    insert2(T, V, K1, R1).

pow2(1):-!.

pow2(K):-
    K mod 2 =:= 0,
    K1 is K // 2,
    pow2(K1).






