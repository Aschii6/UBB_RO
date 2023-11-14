% elimina3(L: list, E: integer, R: list)
% L - lista primita
% E - elementul ptr care trebuie eliminate primele 3 aparitii
% R - lista rezultata in urma eliminarii
% model de flux (i, i, o) sau (i, i, i)
elimina3(L, E, R):-
    elimina3_aux(L, E, 3, R).

% elimina3_aux(L: list, E: integer, N: integer, R: list)
% L - lista primita
% E - elem ptr care trb elim primele 3 aparitii
% N - de cate ori mai poate fi eliminat E
% R - lista rezultata in urma eliminarii
% model de flux (i, i, i, o) sau (i, i, i, i)
elimina3_aux([], _, _, []).

elimina3_aux([H | T], E, 0, [H | R]):-!,
    elimina3_aux(T, E, 0, R).

elimina3_aux([E | T], E, N, R):-!,
    N1 is N - 1,
    elimina3_aux(T, E, N1, R).

elimina3_aux([H | T], E, N, [H | R]):-
    elimina3_aux(T, E, N, R).
