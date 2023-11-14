% succesor(L: list, R: list)
succesor(L, R):-
    succesor_aux(L, _, R).

succesor_aux([], 1, []).

succesor_aux([H|T], 0, [H1|R]):-
    succesor_aux(T, C1, R),
    H1 is H + C1,
    H1 =< 9,!.

succesor_aux([_|T], 1, [0|R]):-
     succesor_aux(T, _, R).
