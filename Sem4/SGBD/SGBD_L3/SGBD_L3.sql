USE GradinaZoo;
GO

CREATE TABLE LogTable
(
cod_log INT PRIMARY KEY IDENTITY,
tip_operatie VARCHAR(20),
nume_tabel VARCHAR(20),
timp_executie DATETIME
);


GO
CREATE OR ALTER FUNCTION valideazaIngrijitor(@nume VARCHAR(50), @salariu INT)
RETURNS VARCHAR(100)
AS
BEGIN
	DECLARE @eroare VARCHAR(100) = ''

	IF LEN(@nume) < 3
		SET @eroare = @eroare + 'Nume invalid '

	IF @salariu < 0
		SET @eroare = @eroare + 'Salariu invalid'

	RETURN @eroare
END

GO
CREATE OR ALTER FUNCTION valideazaCusca(@mediu VARCHAR(50), @capacitate INT, @cod_cusca INT)
RETURNS VARCHAR(100)
AS
BEGIN
	DECLARE @eroare VARCHAR(100) = ''

	IF LEN(@mediu) < 3
		SET @eroare = @eroare + 'Mediu invalid '
		
	IF @capacitate < 0
		SET @eroare = @eroare + 'Capacitate invalid '

	IF NOT EXISTS(SELECT 1 FROM Cusca WHERE cod_cusca = @cod_cusca)
		SET @eroare = @eroare + 'Cod cusca invalid'

	RETURN @eroare
END

GO
CREATE OR ALTER PROCEDURE insertTranzactie1 (@nume_ingrijitor VARCHAR(50), @salariu INT,
	@mediu VARCHAR(50), @capacitate INT, @cod_zona INT)
AS
BEGIN
	BEGIN TRAN
	BEGIN TRY
		DECLARE @eroare VARCHAR(100)
		SET @eroare = dbo.valideazaIngrijitor(@nume_ingrijitor, @salariu)

		IF @eroare <> ''
		BEGIN
			RAISERROR(@eroare, 14, 1)
		END

		INSERT INTO Ingrijitor VALUES (@nume_ingrijitor, @salariu)
		INSERT INTO LogTable VALUES ('INSERT', 'Ingrijitor', CURRENT_TIMESTAMP)

		SET @eroare = dbo.valideazaCusca(@mediu, @capacitate, @cod_zona)

		IF @eroare <> ''
		BEGIN
			RAISERROR(@eroare, 14, 1)
		END

		INSERT INTO Cusca VALUES (@mediu, @capacitate, @cod_zona)
		INSERT INTO LogTable VALUES ('INSERT', 'Cusca', CURRENT_TIMESTAMP)

		DECLARE @cod_ingrijitor INT = (SELECT MAX(cod_ingrijitor) FROM Ingrijitor)
		DECLARE @cod_cusca INT = (SELECT MAX(cod_cusca) FROM Cusca)

		INSERT INTO ResponsabilitateDeIngrijire VALUES (@cod_ingrijitor, @cod_cusca)
		INSERT INTO LogTable VALUES ('INSERT', 'RespDeIngrijire', CURRENT_TIMESTAMP)

		INSERT INTO LogTable VALUES ('COMMIT', '-', CURRENT_TIMESTAMP)
		COMMIT TRAN
	END TRY
	BEGIN CATCH
		PRINT ERROR_MESSAGE()
		ROLLBACK TRAN
		INSERT INTO LogTable VALUES ('ROLLBACK', '-', CURRENT_TIMESTAMP)
		PRINT 'Tranzactia a fost rolled back'
	END CATCH
END

GO
EXEC insertTranzactie1 'Valentin', -1200, 'Padure', 20, 3 -- FAIL LA PRIMA
EXEC insertTranzactie1 'Valentin', 1200, 'Padure', -20, 22 -- FAIL LA A DOUA
EXEC insertTranzactie1 'Valentin', 1200, 'Padure', 20, 3 -- SUCCEED

SELECT * FROM Ingrijitor ORDER BY cod_ingrijitor DESC
SELECT * FROM Cusca ORDER BY cod_cusca DESC
SELECT * FROM ResponsabilitateDeIngrijire ORDER BY cod_ingrijitor DESC, cod_cusca DESC
SELECT * FROM LogTable

GO
CREATE OR ALTER PROCEDURE insertTranzactie2 (@nume_ingrijitor VARCHAR(50), @salariu INT,
    @mediu VARCHAR(50), @capacitate INT, @cod_zona INT)
AS
BEGIN
    BEGIN TRY
        DECLARE @eroare1 VARCHAR(100)
        SET @eroare1 = dbo.valideazaIngrijitor(@nume_ingrijitor, @salariu)

        IF @eroare1 = ''
        BEGIN
			BEGIN TRAN
            INSERT INTO Ingrijitor VALUES (@nume_ingrijitor, @salariu)
			INSERT INTO LogTable VALUES ('INSERT', 'Ingrijitor', CURRENT_TIMESTAMP)
			COMMIT TRAN
        END

		DECLARE @eroare2 VARCHAR(100)
        SET @eroare2 = dbo.valideazaCusca(@mediu, @capacitate, @cod_zona)

        IF @eroare2 <> ''
        BEGIN
            RAISERROR(@eroare2, 14, 1)
        END

		BEGIN TRAN
        INSERT INTO Cusca VALUES (@mediu, @capacitate, @cod_zona)
        INSERT INTO LogTable VALUES ('INSERT', 'Cusca', CURRENT_TIMESTAMP)
        COMMIT TRAN

		IF @eroare1 <> ''
        BEGIN
            RAISERROR(@eroare1, 14, 1)
        END

        BEGIN TRAN
        DECLARE @cod_ingrijitor INT = (SELECT MAX(cod_ingrijitor) FROM Ingrijitor)
        DECLARE @cod_cusca INT = (SELECT MAX(cod_cusca) FROM Cusca)

        INSERT INTO ResponsabilitateDeIngrijire VALUES (@cod_ingrijitor, @cod_cusca)
        INSERT INTO LogTable VALUES ('INSERT', 'RespDeIngrijire', CURRENT_TIMESTAMP)
        COMMIT TRAN

        PRINT 'Tranzactie finalizata cu succes'
    END TRY
    BEGIN CATCH
        PRINT ERROR_MESSAGE()
        PRINT 'Parte din tranzactie a fost rolled back'
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRAN
		END
		INSERT INTO LogTable VALUES ('ROLLBACK', '-', CURRENT_TIMESTAMP)
    END CATCH
END

GO
EXEC insertTranzactie2 'Valentin', -500, 'Padure', -5, 40 -- FAIL LA AMANDOUA
EXEC insertTranzactie2 'Valentin', -1200, 'Padure', 20, 3 -- FAIL LA PRIMA, SUCCEED A DOUA
EXEC insertTranzactie2 'Valentin', 1200, 'Padure', -20, 22 -- SUCCEEED LA PRIMA, FAIL LA A DOUA
EXEC insertTranzactie2 'Valentin', 1200, 'Padure', 20, 3 -- SUCCEED

SELECT * FROM Ingrijitor ORDER BY cod_ingrijitor DESC
SELECT * FROM Cusca ORDER BY cod_cusca DESC
SELECT * FROM ResponsabilitateDeIngrijire ORDER BY cod_ingrijitor DESC, cod_cusca DESC
SELECT * FROM LogTable

DELETE FROM Ingrijitor WHERE nume_ingrijitor = 'Valentin'
DELETE FROM Cusca WHERE mediu = 'Padure'
