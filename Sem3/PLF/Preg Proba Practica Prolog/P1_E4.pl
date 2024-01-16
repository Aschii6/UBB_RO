% Sa se scrie un predicat care substituie intr-o lista un element
% printr-o alta lista.

% subst(L, E, L1, R)
subst([], _, _, []).

subst([H|T], E, L1, [L1 | R]):-
    H =:= E,!,
    subst(T, E, L1, R).

subst([H|T], E, L1, [H | R]):-
    subst(T, E, L1, R).
