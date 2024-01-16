member2(E, [E | _]):-!.

member2(E, [_|T]):-
    member(E, T).

% interclasare(L1, L2, R).

interclasare(L1, [], L1):-!.

interclasare([], L2, L2).

interclasare([H1|T1], [H2|T2], [H1|R]):-
    H1 < H2,
    interclasare(T1, [H2|T2], R),
    \+ member2(H1, R),!.

interclasare([H1|T1], [H2|T2], R):-
    H1 < H2,!,
    interclasare(T1, [H2|T2], R).


interclasare([H1|T1], [H2|T2], [H2|R]):-
    interclasare([H1|T1], T2, R),
    \+ member2(H2, R),!.

interclasare([H1|T1], [_|T2], R):-
    interclasare([H1|T1], T2, R).
