% Sa se scrie un predicat care intoarce diferenta a doua multimi

% member(E: integer, L: list)
member(E, [H | _]):-
    E is H,!.

member(E, [_ | T]):-
    member(E, T).

% dif(L1: list, L2: list, R: rez)
dif([], _, []).

dif([H | T], L2, R):-
    member(H, L2),!, % remove asta
    dif(T, L2, R).

dif([H | T], L2, [H | R]):-
    dif(T, L2, R). % not member(H, L2)

% Sa se scrie un predicat care adauga intr-o lista dupa fiecare element par
% valoarea 1.

% insert1(L: list, R: list)
insert1([], []).

insert1([H|T], [H, 1 | R]):-
    mod(H, 2) =:= 0,!,
    insert1(T, R).

insert1([H | T], [H | R]):-
    insert1(T, R).
