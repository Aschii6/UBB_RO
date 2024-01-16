% sort(L: list, R: list).
inserare(E,[],[E]):-!.

inserare(E,[H|T],[H|R]):-
    E >= H,!,
    inserare(E,T,R).

inserare(E,L,[E|L]):-!.

my_sort([],[]):-!.

my_sort([H|T],R):-
    my_sort(T,R1),
    \+ member(H, R1),!,
    inserare(H,R1,R).

my_sort([_|T], R):-
    my_sort(T, R).

member(E, [E | _]):-!.

member(E, [_|T]):-
    member(E, T).
