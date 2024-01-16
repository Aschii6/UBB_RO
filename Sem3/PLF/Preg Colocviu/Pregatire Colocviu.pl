% 2. Fiind dat un numar n pozitiv, se cere sa se determine toate
% descompunerile sale ca suma de numere prime distincte.

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

candidat(N, E):-
    candidat_aux(N, 2, E).

candidat_aux(_, M, M):-
    prim(M).

candidat_aux(N, M, E):-
    M =< N,
    M1 is M + 1,
    candidat_aux(N, M1, E).

descomp(N, R):-
    descomp_aux(N, N, R).

descomp_aux(N, N, []).

descomp_aux(N, S, [E]):-
    candidat(N, E),
    S1 is S + E,
    descomp_aux(N, S1, []).

descomp_aux(N, S, [E, H|T]):-
    candidat(N, E),
    S1 is S + E,
    S1 =< N,
    descomp_aux(N, S1, [H|T]),
    E < H.

% uhh sunt ceva mai multe probleme + ar fi trebuit refactorizata sincer

% 10. Se da sirul a1,..., an cu elemente numere intregi distincte. Se cere sa se
% determine toate submultimile avand suma divizibila cu n.

subm2(L, N, R):-
    candidat3(E, L),
    subm2_aux(L, N, E, [E], R).

subm2_aux(_, N, S, Col, Col):-
    S mod N =:= 0.

subm2_aux(L, N, S, [H|T], R):-
    candidat3(E, L),
    E < H,
    S1 is S + E,
    subm2_aux(L, N, S1, [E, H|T], R).

% Să se scrie un program PROLOG care generează lista permutărilor
% mulţimii 1..N, cu proprietatea că valoarea absolută a diferenţei între
% 2 valori consecutive din permutare este >=2. Se vor scrie modelele
% matematice și modelele de flux pentru predicatele folosite. Exemplu-
% pentru N=4 ⇒ [[3,1,4,2], [2,4,1,3]] (nu neapărat în această ordine)

genereazaLista(N, N, [N]):-!.
genereazaLista(I, N, [I|R]):-
 I < N,
 I1 is I + 1,
 genereazaLista(I1, N, R).

perm(L, K, R):-
    candidat3(E, L),
    perm_aux(L, K, [E], R).

perm_aux(_, 0, Col, Col).

perm_aux(L, K, [H|T], R):-
    candidat3(E, L),
    \+ candidat3(E, [H|T]),
    abs(H - E) >= 2,
    K1 is K - 1,
    perm_aux(L, K1, [E, H|T], R).

permMain(N, R):-
    genereazaLista(1, N, L),
    N1 is N - 1, % in apelul de la perm pun deja un elem
    findall(X, perm(L, N1, X), R).

% sub 1 2023
% lista aranj de k elem dintr o lista, avand produs P dat
% [2, 5, 3, 4, 10], k = 2 => [[2,10], [10, 2], [5, 4], [4, 5]]
% specif com flux ...

aranj(L, K, P, R):-
    candidat3(E, L),
    K1 is K - 1,
    aranj_aux(L, K1, P, E, [E], R).

aranj_aux(_, 0, P, P, Col, Col).

aranj_aux(L, K, P, PP, Col, R):-
    K > 0,
    candidat3(E, L),
    \+ candidat3(E, Col),
    NewPP is PP * E,
    NewPP =< P,
    K1 is K - 1,
    aranj_aux(L, K1, P, NewPP, [E|Col], R).

% sub 2 2023
% se da lista, subm cu minim n elem avand suma div cu 3
% [2, 3, 4] N = 1 => [[3], [2,4], [2,3,4]]

candidat3(E,[E|_]).
candidat3(E,[_|T]):-
    candidat3(E,T).

submultimi3(L,N,R):-
    candidat3(E,L),
    submultimi_aux(L,N,1,E,[E],R).

submultimi_aux(_,N,Nr,Sum,Col,Col):-
    Nr>=N,
    mod(Sum,3)=:=0.

submultimi_aux(L,N,Nr,Sum,[H|T],R):-
    candidat3(E,L),
    E<H,
    Nr1 is Nr+1,
    Sum1 is Sum+E,
    submultimi_aux(L,N,Nr1,Sum1,[E|[H|T]],R).

submMain3(L,N,R):-
    findall(C,submultimi3(L,N,C),R).
