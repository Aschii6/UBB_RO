USE GradinaZoo

CREATE TABLE ZonaCopy
(
cod_zona INT PRIMARY KEY IDENTITY,
denumire_zona VARCHAR(50),
suprafata REAL
);

CREATE TABLE CuscaCopy
(
cod_cusca INT PRIMARY KEY IDENTITY,
mediu VARCHAR(50),
capacitate INT,
cod_zona INT FOREIGN KEY REFERENCES ZonaCopy(cod_zona) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IngrijitorCopy
(
cod_ingrijitor INT PRIMARY KEY IDENTITY,
nume_ingrijitor VARCHAR(50),
salariu_ingrijitor INT
);

INSERT INTO IngrijitorCopy (nume_ingrijitor, salariu_ingrijitor) VALUES ('A', 1), ('B', 2), ('C', 3),
	('D', 4), ('E', 5)

SELECT * FROM IngrijitorCopy

CREATE TABLE ResponsabilitateDeIngrijireCopy
(
cod_ingrijitor INT FOREIGN KEY REFERENCES IngrijitorCopy(cod_ingrijitor) ON UPDATE CASCADE ON DELETE CASCADE,
cod_cusca INT FOREIGN KEY REFERENCES CuscaCopy(cod_cusca) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY (cod_ingrijitor, cod_cusca)
);

-- Tabele care fac parte din testare
INSERT INTO Tables (Name) VALUES ('ZonaCopy'), ('CuscaCopy'), ('ResponsabilitateDeIngrijireCopy')

SELECT * FROM Tables

-- Creez view-uri
GO
CREATE OR ALTER VIEW vwZona
AS
SELECT Z.cod_zona, Z.denumire_zona, Z.suprafata FROM ZonaCopy Z

GO
CREATE OR ALTER VIEW vwCusca
AS
SELECT C.cod_cusca, C.mediu, C.capacitate, Z.denumire_zona as zona
FROM CuscaCopy C
JOIN ZonaCopy Z ON Z.cod_zona = C.cod_zona

GO
CREATE OR ALTER VIEW vwRespIngrijire
AS
SELECT I.nume_ingrijitor, C.mediu
FROM ResponsabilitateDeIngrijireCopy RI
JOIN CuscaCopy C ON RI.cod_cusca = c.cod_cusca
JOIN IngrijitorCopy I ON I.cod_ingrijitor = RI.cod_ingrijitor
GROUP BY C.mediu, I.nume_ingrijitor

GO
SELECT * from vwZona
SELECT * from vwCusca
SELECT * from vwRespIngrijire

-- Viewuri
GO
INSERT INTO Views VALUES ('vwZona'), ('vwCusca'), ('vwRespIngrijire')

SELECT * FROM Views

-- Proceduri Insert Delete
GO
CREATE OR ALTER PROCEDURE InsertZona (@rows INT)
AS
BEGIN
	DECLARE @nume VARCHAR(50)
	DECLARE @suprafata REAL
	DECLARE @i INT

	SET @suprafata = 500
	SET @i = 1

	WHILE @i <= @rows
	BEGIN
		SET @nume = CONCAT('Nume Zona', @i)
		INSERT INTO ZonaCopy (denumire_zona, suprafata) VALUES (@nume, @suprafata)

		SET @i = @i + 1
	END
END

GO
CREATE OR ALTER PROCEDURE InsertCusca (@rows INT)
AS
BEGIN
	-- EXEC DeleteCusca @rows
	DECLARE @i INT = 1
	DECLARE @cod_zona INT
	DECLARE @mediu VARCHAR(50)
	DECLARE @capacitate INT = '50'

	DECLARE cursorZona CURSOR SCROLL FOR SELECT cod_zona FROM ZonaCopy
	OPEN cursorZona
	FETCH FIRST FROM cursorZona INTO @cod_zona

	WHILE @i <= @rows AND @@FETCH_STATUS = 0
	BEGIN
		SET @mediu = CONCAT('Mediu', @i)
		INSERT INTO CuscaCopy (mediu, capacitate, cod_zona) VALUES (@mediu, @capacitate, @cod_zona)
		FETCH NEXT FROM cursorZona INTO @cod_zona
		
		SET @i = @i + 1
	END
	CLOSE cursorZona
	DEALLOCATE cursorZona
END

GO
CREATE OR ALTER PROCEDURE InsertRespIngrijire (@rows INT)
AS
BEGIN
	DELETE FROM ResponsabilitateDeIngrijireCopy
	DECLARE @i INT = 1
	DECLARE @cod_cusca INT
	DECLARE @cod_ingrijitor INT

	DECLARE cursorCusca CURSOR SCROLL FOR SELECT cod_cusca FROM CuscaCopy
	OPEN cursorCusca
	FETCH FIRST FROM cursorCusca INTO @cod_cusca

	WHILE @i <= @rows AND @@FETCH_STATUS = 0
	BEGIN
		SET @cod_ingrijitor = FLOOR(RAND() * 5) + 1
		INSERT INTO ResponsabilitateDeIngrijireCopy (cod_ingrijitor, cod_cusca) VALUES 
			(@cod_ingrijitor, @cod_cusca)
		FETCH NEXT FROM cursorCusca INTO @cod_cusca
		
		SET @i = @i + 1
	END
	CLOSE cursorCusca
	DEALLOCATE cursorCusca
END

-- Tests - informatii configurari de testare
INSERT INTO Tests (Name) VALUES ('Test cu 5000 pe toate')

SELECT * FROM Tests

-- TestTables
SELECT * FROM Tables

INSERT INTO TestTables (TestID, TableID, NoOfRows, Position) VALUES
(1, 1, 5000, 1), (1, 2, 5000, 2), (1, 3, 5000, 3)

SELECT * FROM TestTables

-- TestViews
SELECT * FROM Views

INSERT INTO TestViews (TestID, ViewID) VALUES (1, 1), (1, 2), (1, 3)

SELECT * FROM TestViews

-- testInsert
GO
CREATE OR ALTER PROCEDURE testInsert (@TestID INT)
AS
BEGIN
	DECLARE @testRunID INT
	DECLARE @tableID INT
	DECLARE @rows INT
	DECLARE @position INT

	SELECT TOP 1 @testRunID = TR.TestRunID FROM TestRuns TR ORDER BY TR.TestRunID DESC

	DECLARE cursorTable CURSOR SCROLL FOR
		SELECT TT.TableID, TT.NoOfRows, TT.Position FROM TestTables TT 
		WHERE TT.TestID = @TestID ORDER BY TT.Position ASC
	OPEN cursorTable
	
	FETCH FIRST FROM cursorTable INTO @tableID, @rows, @position
	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt) VALUES (@testRunID, @tableID, GETDATE(), GETDATE())
		DECLARE @tableName VARCHAR(50)
		SELECT @tableName = T.Name FROM Tables T WHERE T.TableID = @tableID

		IF @tableName = 'ZonaCopy'
			EXEC InsertZona @rows
		IF @tableName = 'CuscaCopy'
			EXEC InsertCusca @rows
		IF @tableName = 'ResponsabilitateDeIngrijireCopy'
			EXEC InsertRespIngrijire @rows

		UPDATE TestRunTables SET EndAt = GETDATE() WHERE TestRunID = @testRunID AND TableID = @tableID

		FETCH NEXT FROM cursorTable INTO @tableID, @rows, @position
	END

	CLOSE cursorTable
	DEALLOCATE cursorTable
END

-- testView
GO
CREATE OR ALTER PROCEDURE testView (@TestID INT)
AS
BEGIN
	DECLARE @testRunID INT
	DECLARE @viewID INT

	SELECT TOP 1 @testRunID = TR.TestRunID FROM TestRuns TR ORDER BY TR.TestRunID DESC

	SELECT @viewID = TV.ViewID FROM TestViews TV WHERE TV.TestID = @TestID

	DECLARE cursorView CURSOR SCROLL FOR
		SELECT TV.ViewID FROM TestViews TV 
		WHERE TV.TestID = @TestID ORDER BY TV.ViewID ASC
	OPEN cursorView

	FETCH FIRST FROM cursorView INTO @viewID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		DECLARE @viewName VARCHAR(50)
		SELECT @viewName = V.Name FROM Views V WHERE V.ViewID = @viewID

		INSERT INTO TestRunViews VALUES (@testRunID, @viewID, GETDATE(), GETDATE())

		IF @viewName = 'vwZona'
			SELECT * FROM vwZona
		IF @viewName = 'vwCusca'
			SELECT * FROM vwCusca
		IF @viewName = 'vwRespIngrijire'
			SELECT * FROM vwRespIngrijire

		UPDATE TestRunViews SET EndAt = GETDATE() WHERE TestRunID = @testRunID AND ViewID = @viewID

		FETCH NEXT FROM cursorView INTO @viewID
	END

	CLOSE cursorView
	DEALLOCATE cursorView
END

-- testPeTabela
GO
CREATE OR ALTER PROCEDURE executaTest (@TestID INT)
AS
BEGIN
	DECLARE @tableID INT
	DECLARE @position INT 

	DECLARE cursorTable CURSOR SCROLL FOR
		SELECT TT.TableID, TT.Position FROM TestTables TT 
		WHERE TT.TestID = @TestID ORDER BY TT.Position DESC
	OPEN cursorTable

	FETCH FIRST FROM cursorTable INTO @tableID, @position
	WHILE @@FETCH_STATUS = 0
	BEGIN
		DECLARE @tableName VARCHAR(50)
		SELECT @tableName = T.Name FROM Tables T WHERE T.TableID = @tableID

		IF @tableName = 'ZonaCopy'
			DELETE FROM ZonaCopy
		IF @tableName = 'CuscaCopy'
			DELETE FROM CuscaCopy
		IF @tableName = 'ResponsabilitateDeIngrijireCopy'
			DELETE FROM ResponsabilitateDeIngrijireCopy

		FETCH NEXT FROM cursorTable INTO @tableID, @position
	END
	CLOSE cursorTable
	DEALLOCATE cursorTable

	EXEC testInsert @TestID
	EXEC testView @TestID
END

-- executaTeste
GO
CREATE OR ALTER PROCEDURE executaTeste
AS
BEGIN
	DECLARE @testRunID INT

	INSERT INTO TestRuns VALUES ('Rulez toate testele', GETDATE(), GETDATE())

	SELECT TOP 1 @testRunID = TR.TestRunID FROM TestRuns TR ORDER BY TR.TestRunID DESC

	DECLARE @i INT
    SELECT @i = COUNT(*) FROM Tests
	DECLARE @TestID INT

    DECLARE cursorT CURSOR SCROLL FOR
	SELECT TestID FROM Tests
	OPEN cursorT;
	FETCH FIRST FROM cursorT INTO @TestID

    WHILE @i>0 AND @@FETCH_STATUS=0
	BEGIN
		EXEC executaTest @TestID
		FETCH NEXT FROM cursorT INTO @TestID
		SET @i = @i - 1
    END
    CLOSE cursorT
    DEALLOCATE cursorT

	UPDATE TestRuns SET EndAt = GETDATE() WHERE TestRunID = @testRunID
END

GO
EXEC executaTeste

-- Rezultate teste
SELECT * FROM TestRuns
SELECT * FROM TestRunTables
SELECT * FROM TestRunViews

DELETE TestRuns
DELETE TestRunTables
DELETE TestRunViews
