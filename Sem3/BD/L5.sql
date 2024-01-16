USE GradinaZoo
GO

-- Operatii pe Vizitator, Cusca, Rating Cusca
SELECT * FROM Zona
SELECT * FROM Cusca
SELECT * FROM Vizitator
SELECT * FROM RatingCusca

-- Functii de validare
GO
CREATE OR ALTER FUNCTION validateCusca(
@mediu VARCHAR(50), @capacitate INT)
RETURNS BIT
AS
BEGIN
	IF @mediu IS NULL
		RETURN 1
	IF LEN(@mediu) < 3
		RETURN 1

	IF @capacitate IS NULL
		RETURN 1
	IF @capacitate <= 0
		RETURN 1

	RETURN 0
END

GO
CREATE OR ALTER FUNCTION validateVizitator(
@nume_vizitator VARCHAR(50), @data_nasterii VARCHAR(10))
RETURNS BIT AS
BEGIN
	IF @nume_vizitator IS NULL
		RETURN 1
	IF LEN(@nume_vizitator) < 3
		RETURN 1
	IF @data_nasterii IS NULL
		RETURN 1

	DECLARE @expectedFormat VARCHAR(10) = 'yyyy-MM-dd';
    DECLARE @formattedDate DATE = TRY_CONVERT(DATE, @data_nasterii, 120)

    IF @formattedDate IS NULL
		RETURN 1
	RETURN 0
END

GO
CREATE OR ALTER FUNCTION validateRatingCusca(
@rating INT)
RETURNS BIT AS
BEGIN
	IF @rating IS NULL
		RETURN 1

	RETURN 0
END

-- Proceduri CRUD
GO
CREATE OR ALTER PROCEDURE insertCusca(
@mediu VARCHAR(50), @capacitate INT, @cod_zona INT)
AS
BEGIN
	IF dbo.validateCusca(@mediu, @capacitate) = 1
	BEGIN
		PRINT 'Date Cusca Invalide'
		RETURN
	END
	INSERT INTO Cusca (mediu, capacitate, cod_zona) VALUES (@mediu, @capacitate, @cod_zona)
END

GO
CREATE OR ALTER PROCEDURE selectCusca
AS
BEGIN
	SELECT * FROM Cusca
END

GO
CREATE OR ALTER PROCEDURE updateCusca(
@cod_cusca INT, @new_mediu VARCHAR(50), @new_capacitate INT, @new_cod_zona INT)
AS
BEGIN
	IF dbo.validateCusca(@new_mediu, @new_capacitate) = 1
	BEGIN
		PRINT 'Date Cusca Invalide'
		RETURN
	END
	UPDATE Cusca SET mediu = @new_mediu, capacitate = @new_capacitate, cod_zona = @new_cod_zona
	WHERE cod_cusca = @cod_cusca;
END

GO
CREATE OR ALTER PROCEDURE deleteCusca(
@cod_cusca INT)
AS
BEGIN
	DELETE FROM Cusca WHERE cod_cusca = @cod_cusca
END

GO
CREATE OR ALTER PROCEDURE insertVizitator(
@nume_vizitator VARCHAR(50), @data_nasterii VARCHAR(10))
AS
BEGIN
	IF dbo.validateVizitator(@nume_vizitator, @data_nasterii) = 1
	BEGIN
		PRINT 'Date Vizitator Invalide'
		RETURN
	END
	INSERT INTO Vizitator(nume_vizitator, data_nasterii) VALUES (@nume_vizitator, 
	CONVERT(DATE, @data_nasterii, 120))
END

GO
CREATE OR ALTER PROCEDURE selectVizitator
AS
BEGIN
	SELECT * FROM Vizitator
END

GO
CREATE OR ALTER PROCEDURE updateVizitator(
@cod_vizitator INT, @new_nume_vizitator VARCHAR(50), @new_data_nasterii DATE)
AS
BEGIN
	IF dbo.validateVizitator(@new_nume_vizitator, @new_data_nasterii) = 1
	BEGIN
		PRINT 'Date Vizitator Invalide'
		RETURN
	END
	UPDATE Vizitator SET nume_vizitator = @new_nume_vizitator, data_nasterii = @new_data_nasterii
	WHERE cod_vizitator = @cod_vizitator
END

GO
CREATE OR ALTER PROCEDURE deleteVizitator(
@cod_vizitator INT)
AS
BEGIN
	DELETE FROM Vizitator WHERE cod_vizitator = @cod_vizitator
END

GO
CREATE OR ALTER PROCEDURE insertRatingCusca(
@cod_cusca INT, @cod_vizitator INT, @rating INT)
AS
BEGIN
	IF dbo.validateRatingCusca(@rating) = 1
	BEGIN
		PRINT 'Date Rating Cusca Invalide'
		RETURN
	END
	INSERT INTO RatingCusca(cod_cusca, cod_vizitator, rating) VALUES (@cod_cusca, @cod_vizitator, @rating)
END

GO
CREATE OR ALTER PROCEDURE selectRatingCusca
AS
BEGIN
	SELECT * FROM RatingCusca
END

GO
CREATE OR ALTER PROCEDURE updateRatingCusca(
@cod_cusca INT, @cod_vizitator INT, @new_rating INT)
AS
BEGIN
	IF dbo.validateRatingCusca(@new_rating) = 1
	BEGIN
		PRINT 'Date Rating Cusca Invalide'
		RETURN
	END
	UPDATE RatingCusca SET rating = @new_rating WHERE cod_cusca = @cod_cusca AND cod_vizitator = @cod_vizitator
END

GO
CREATE OR ALTER PROCEDURE deleteRatingCusca(
@cod_cusca INT, @cod_vizitator INT)
AS
BEGIN
	DELETE FROM RatingCusca WHERE cod_cusca = @cod_cusca AND cod_vizitator = @cod_vizitator
END

-- Views si Indecsi
GO
CREATE OR ALTER VIEW viewCusca AS
	SELECT C.cod_cusca, Z.denumire_zona, C.mediu, C.capacitate FROM Cusca C
	INNER JOIN Zona Z ON Z.cod_zona = C.cod_zona

GO
CREATE INDEX IX_Cusca_cod_zona_asc ON Cusca(cod_zona)

GO
CREATE OR ALTER VIEW viewVizitator AS
	SELECT V.cod_vizitator, V.nume_vizitator, V.data_nasterii FROM Vizitator V

GO
CREATE INDEX IX_Vizitator_nume_data_nasterii_asc ON Vizitator(nume_vizitator ASC, data_nasterii ASC)

GO
CREATE OR ALTER VIEW viewRatingCusca AS
	SELECT C.mediu, V.nume_vizitator, RC.rating FROM RatingCusca RC
	INNER JOIN Cusca C ON RC.cod_cusca = C.cod_cusca
	INNER JOIN Vizitator V ON V.cod_vizitator = RC.cod_vizitator

GO
CREATE INDEX IX_RatingCusca_rating_asc ON RatingCusca(rating)

-- Apeluri
SELECT * FROM viewCusca
SELECT * FROM viewVizitator
SELECT * FROM viewRatingCusca

EXEC insertCusca 'Mediu Test', 40, 3

EXEC insertCusca '', -3, 3

EXEC insertCusca 'Okk', 22, 9999

EXEC updateCusca 10, 'Mediu Test Nou', 45, 3

EXEC deleteCusca 10

EXEC selectCusca

EXEC insertVizitator 'Lorin Fortuna', '2022-10-22'

EXEC updateVizitator 1005, 'Lorin Fortuna!', '2000-11-15'

EXEC updateVizitator 10101010, 'Proba test', '2011-09-26'

EXEC insertVizitator 'Proba test 2', '2011-096ev'

EXEC deleteVizitator 1005

EXEC selectVizitator

EXEC insertRatingCusca 4, 5, 5

EXEC updateRatingCusca 4, 4, 3

EXEC updateRatingCusca 4, 4, 22

EXEC insertRatingCusca 5, 1003, 6

EXEC deleteRatingCusca 4, 5

EXEC selectRatingCusca