% b. Sa se scrie un predicat care, primind o lista, intoarce multimea
% tuturor perechilor din lista. De ex, cu [a, b, c, d] va produce
% [[a, b], [a, c], [a, d], [b, c], [b, d], [c, d]].

% perechiToate(L: list, R: list)
perechiToate([], []).

perechiToate([H | T], R):-
    lista_perechi(H, T, R1),
    perechiToate(T, R2),
    inserareP(R1, R2, R).

lista_perechi(_, [], []):-!. % nu stiu de ce da false rosu fara cut aici xd

lista_perechi(E, [H|T], [[E, H] | R]):-
    lista_perechi(E, T, R).

inserareP([], R2, R2):-!.

inserareP([H|T], R2, [H|R]):-
    inserareP(T, R2, R).
