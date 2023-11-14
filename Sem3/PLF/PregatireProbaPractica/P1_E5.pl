% a. Sa se scrie un predicat care sterge toate aparitiile unui anumit
% atom dintr-o lista.
delete_atom(_, [], []):-!.

delete_atom(X, [X | T], R):-!,
    delete_atom(X, T, R).

delete_atom(X, [H | T], [H | R]):-
    delete_atom(X, T, R).

% b. Definiti un predicat care, dintr-o lista de atomi, produce o lista
% de perechi (atom n), unde atom apare in lista initiala de n ori. De
% ex: numar([1, 2, 1, 2, 1, 3, 1], X) va produce X = [[1, 4], [2, 2],
% [3, 1]].

% perechi(L: list, R: list)
perechi([], []):-!.

perechi([H|T], [[H, 1] | R2]):-
    % o functie de numar aparatii inlocuiesc 1
    delete_atom(H, T, R1),
    perechi(R1, R2).
