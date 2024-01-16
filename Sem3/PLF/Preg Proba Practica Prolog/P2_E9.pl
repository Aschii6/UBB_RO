% stergeConsec(L: list, R: list)
stergeConsec(L, R):-
    stergeConsec_aux(L, 0, R).

stergeConsec_aux([], _, []).

stergeConsec_aux([H1, H2 | T], _, R):-
    H1 is H2 - 1,!,
    stergeConsec_aux([H2 | T], 1, R).

stergeConsec_aux([_ | T], 1, R):-
    stergeConsec_aux(T, 0, R).

stergeConsec_aux([H1 | T], 0, [H1 | R]):-
    stergeConsec_aux(T, 0, R).

