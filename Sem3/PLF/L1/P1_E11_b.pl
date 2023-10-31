% suma_alternanta(List: list, Sum: integer)
% List - lista de numere ptr care calculam suma alternanta
% Sum - rezultatul
% model de flux - (i, o) sau (i, i)
suma_alternanta(List, Sum) :- suma_alternanta_aux(List, 0, Sum).

% suma_alternanta_aux(List : list, C: integer, Sum: integer)
% C - 0 semnifica adunare, 1 semnifica scadere
% Sum - rezultatul
% model de flux - (i, i, o) sau (i, i, i)
suma_alternanta_aux([], _, 0):-!. % cand lista este vida


suma_alternanta_aux([FirstElem|RestOfList], 0, Sum) :-!,
    suma_alternanta_aux(RestOfList, 1, NewSum),
    Sum is FirstElem + NewSum.


suma_alternanta_aux([FirstElem|RestOfList], 1, Sum) :-!,
    suma_alternanta_aux(RestOfList, 0, NewSum),
    Sum is NewSum - FirstElem.

