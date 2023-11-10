USE GradinaZoo
GO
CREATE PROCEDURE modificaTip
AS
BEGIN
	ALTER TABLE Zona
	ALTER COLUMN Suprafata INT
END

GO
CREATE PROCEDURE modificaTipUndo
AS
BEGIN
	ALTER TABLE Zona
	ALTER COLUMN Suprafata REAL
END

GO
CREATE PROCEDURE adaugaConstrangereValoareImplicita
AS
BEGIN
	ALTER TABLE Ingrijitor
	ADD CONSTRAINT df_salariu_ingrijitor DEFAULT 1900 FOR salariu_ingrijitor
END

GO
CREATE PROCEDURE adaugaConstrangereValoareImplicitaUndo
AS
BEGIN
	ALTER TABLE Ingrijitor
	DROP CONSTRAINT df_salariu_ingrijitor
END

GO
CREATE PROCEDURE adaugaTabela
AS
BEGIN
	CREATE TABLE PerechiAnimale
	(
	id1 INT FOREIGN KEY REFERENCES Animal(cod_animal),
	id2 INT FOREIGN KEY REFERENCES Animal(cod_animal),
	PRIMARY KEY (id1, id2)
	);
END

GO
CREATE PROCEDURE adaugaTabelaUndo
AS
BEGIN
	IF OBJECT_ID('PerechiAnimale', 'U') IS NOT NULL
	BEGIN
		DROP TABLE PerechiAnimale
	END
END

GO
CREATE PROCEDURE adaugaCamp
AS
BEGIN
	ALTER TABLE Vizitator
	ADD numar_vizite INT
END

GO
CREATE PROCEDURE adaugaCampUndo
AS
BEGIN
	ALTER TABLE Vizitator
	DROP COLUMN numar_vizite
END

GO
CREATE PROCEDURE stergeConstrangereCheieStraina
AS
BEGIN
	ALTER TABLE GhidTuristic
	DROP CONSTRAINT FK_GhidTuristic_cod_zona_Contraint
END

GO
CREATE PROCEDURE stergeConstrangereCheieStrainaUndo
AS
BEGIN
	ALTER TABLE GhidTuristic
	ADD CONSTRAINT FK_GhidTuristic_cod_zona_Contraint
	FOREIGN KEY(cod_zona) REFERENCES Zona(cod_zona) ON UPDATE CASCADE ON DELETE CASCADE
END

GO
CREATE TABLE Versiuni
(
versiune SMALLINT,
descriere VARCHAR(100)
);

INSERT INTO Versiuni (versiune, descriere) VALUES (0, 'versiunea de baza'), (1, 'vers. 0 + Zona.suprafata REAL -> INT'),
(2, 'vers. 1 + Ingrijitor.salariu valoare default 1900'), (3, 'vers. 2 + tabela nou PerechiAnimale'),
(4, 'vers. 3 + camp nou Vizitator.numar_vizite'), (5, 'vers. 4 + sterge constrangere FK GhidTuristic.cod_zona')

SELECT * FROM Versiuni

GO
CREATE PROCEDURE modificaVersiune @versiune SMALLINT
AS
BEGIN
	BEGIN TRY
		EXEC modificaTipUndo
		EXEC adaugaConstrangereValoareImplicitarUndo
		EXEC adaugaTabelaUndo
		EXEC adaugaCampUndo
		EXEC stergeConstrangereCheieStrainaUndo
	END TRY
	BEGIN CATCH
	END CATCH

	IF @versiune > 5 RETURN

	IF @versiune >= 1 EXEC modificaTip
	IF @versiune >= 2 EXEC adaugaConstrangereValoareImplicita
	IF @versiune >= 3 EXEC adaugaTabela
	IF @versiune >= 4 EXEC adaugaCamp
	IF @versiune >= 5 EXEC stergeConstrangereCheieStraina
END

EXEC modificaVersiune @versiune = 0
