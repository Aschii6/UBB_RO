% Sa se sorteze o lista cu pastrarea dublurilor. De ex: [4 2 6 2 3 4] =>
% [2 2 3 4 4 6]

% sort(L: list, R: list).
inserare(E,[],[E]):-!.

inserare(E,[H|T],[H|R]):-
    E >= H,!,
    inserare(E,T,R).

inserare(E,L,[E|L]):-!.

my_sort([],[]):-!.

my_sort([H|T],R):-
    my_sort(T,R1),
    inserare(H,R1,R).
