my_insert([], L, L).

my_insert([H|T], L, [H|R]):-
    my_insert(T, L, R).

% inloc(L: list, E: integer, L1: list, R: list)
% L - lista de nr intregi
% E - elementul care trb inlocuit din lista
% L1 - lista cu care trebuie inlocuita E in L
% R - lista rezultat
% model de flux - (i, i, i, o) sau (i, i, i, i)
inloc([], _, _, []). % cand lista e vida

inloc([H|T], E, L1, [H|R]):- % H != E
    H =\= E,
    inloc(T, E, L1, R).

inloc([H|T], E, L1, R):- % H = E
    H =:= E,
    inloc(T, E, L1, R1),
    my_insert(L1, R1, R).


first_elem([], 0).

first_elem([H|_], H).
% inloc2(L: list, L1: list, R: list)
% L - lista eterogena
% L1 - lista cu care sa inlocuiesc primul element din fiecare sublista
% din L
% R - rezultatul
% model de flux - (i, i, o) sau (i, i, i)
inloc2([], _, []). % lista e vida

inloc2([H|T], L1, [H|R]):- % H nu este lista
    \+is_list(H),
    inloc2(T, L1, R).

inloc2([H|T], L1, [R1|R2]):- % H e lista
    is_list(H),
    first_elem(H, E),
    inloc(H, E, L1, R1),
    inloc2(T, L1, R2).













