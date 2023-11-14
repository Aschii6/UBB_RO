% Sa se construiasca lista (m, ..., n), adica multimea numerelor intregi
% din intervalul [m, n].

%interval(M: integer, N: integer, R: list)
interval(M, N, []):-
    M is N + 1,!.

interval(M, N, [M | R]):-
    M =< N,
    M1 is M + 1,
    interval(M1, N, R).
