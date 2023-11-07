USE GradinaZoo

-- 3 tabele + WHERE + relatie m-n
SELECT A.nume_animal, S.nume_specie, C.mediu
FROM Animal A JOIN Specie S ON A.cod_specie = S.cod_specie
JOIN Cusca C ON A.cod_cusca = C.cod_cusca
WHERE S.nume_specie = 'Capybara'


-- 3 tabele + WHERE + relatie m-n
SELECT I.nume_ingrijitor, C.mediu
FROM Cusca C JOIN ResponabilitateDeIngrijire R ON C.cod_cusca = R.cod_cusca 
JOIN Ingrijitor I ON I.cod_ingrijitor = R.cod_ingrijitor
WHERE C.mediu = 'Savana'


-- 4 tabele + WHERE + DISTINCT (+ relatie m-n)
SELECT DISTINCT I.nume_ingrijitor, Z.denumire_zona
FROM Zona Z JOIN Cusca C ON C.cod_zona = Z.cod_zona
JOIN ResponabilitateDeIngrijire R ON C.cod_cusca = R.cod_cusca 
JOIN Ingrijitor I ON I.cod_ingrijitor = R.cod_ingrijitor
WHERE Z.denumire_zona = 'Ramura de Vest'


-- DISTINCT + 3 tabele + WHERE
SELECT DISTINCT S.nume_specie, C.mediu
FROM Animal A JOIN Specie S ON A.cod_specie = S.cod_specie
JOIN Cusca C ON A.cod_cusca = C.cod_cusca
WHERE c.mediu = 'Padure de foioase'


-- GROUP BY + WHERE
SELECT C.mediu, AVG(CLC.cost) AS costAvg
FROM Cusca C JOIN CosturiLunareCusca CLC ON CLC.cod_cusca = C.cod_cusca
GROUP BY C.mediu
HAVING AVG(CLC.cost) > 3500


-- 3 tabele + WHERE
SELECT V.nume_vizitator, C.mediu, RC.rating
FROM Vizitator V JOIN RatingCusca RC ON RC.cod_vizitator = V.cod_vizitator
JOIN Cusca C ON C.cod_cusca = RC.cod_cusca
WHERE RC.rating <= 3


-- GROUP BY + HAVING
SELECT C.mediu, AVG(RC.rating) AS avgRating
FROM Cusca C
JOIN RatingCusca RC ON RC.cod_cusca = C.cod_cusca
GROUP BY C.mediu
HAVING AVG(RC.rating) = 5


-- GROUP BY + HAVING + 3 tabele
SELECT C.mediu, S.nume_specie, COUNT(*) AS numSpecies
FROM Cusca C
JOIN Animal A ON C.cod_cusca = A.cod_cusca
JOIN Specie S ON A.cod_specie = S.cod_specie
GROUP BY C.cod_cusca, S.nume_specie, C.mediu


-- 3 tabele
SELECT G.nume_ghid, C.mediu
FROM GhidTuristic G
JOIN Zona Z ON Z.cod_zona = G.cod_zona
JOIN Cusca C ON C.cod_zona = Z.cod_zona


-- BONUS
SELECT Animal.nume_animal FROM Animal
WHERE Animal.cod_specie = 5

