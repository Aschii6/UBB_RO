USE master
GO
IF(EXISTS(SELECT * FROM sys.databases WHERE name='PracticS1'))
	DROP DATABASE PracticS1;
GO
CREATE DATABASE PracticS1;
GO
USE PracticS1;
GO

CREATE TABLE Autori
(
cod_autor INT PRIMARY KEY IDENTITY,
nume VARCHAR(50),
prenume VARCHAR(50),
data_nasterii date
);
CREATE TABLE Carti
(
cod_carte INT PRIMARY KEY IDENTITY,
titlu VARCHAR(200),
numar_pagini int,
anul_aparitiei int,
pret real,
cod_autor INT FOREIGN KEY REFERENCES Autori(cod_autor)
ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE Tipografii
(
cod_tipografie INT PRIMARY KEY IDENTITY,
nume VARCHAR(200),
adresa varchar(200)
);
CREATE TABLE Echipamente
(
cod_echipament INT PRIMARY KEY IDENTITY,
nume VARCHAR(200),
capacitate int,
pret real,
nume_producator varchar(50),
cod_tipografie INT FOREIGN KEY REFERENCES Tipografii(cod_tipografie)
ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE Tiraje
(
cod_tipografie INT FOREIGN KEY REFERENCES Tipografii(cod_tipografie)
ON UPDATE CASCADE ON DELETE CASCADE,
cod_carte INT FOREIGN KEY REFERENCES Carti(cod_carte)
ON UPDATE CASCADE ON DELETE CASCADE,
numar_copii INT,
data_livrarii date,
CONSTRAINT pk_Tiraje PRIMARY KEY (cod_tipografie, cod_carte)
);
--Autori
INSERT INTO Autori(prenume, nume, data_nasterii) VALUES
('William', 'Shakespeare','1564-04-23'), 
('Jane', 'Austen', '1775-12-16'), 
('Mark', 'Twain', '1835-11-30');

--Carti
INSERT INTO Carti(titlu, numar_pagini, anul_aparitiei, pret, cod_autor) VALUES
('Hamlet', 345, 1600, 99.99, 1), 
('Romeo si Julieta', 195, 1597, 75.50,1),
('Macbeth', 130, 1623, 82, 1),
('Emma', 475, 1815, 37.75, 2),
('Mandrie si prejudecata', 435, 1813, 109.99,2),
('Aventurile lui Huckleberry Finn', 370, 1884, 39.5, 3);
--Tipografii
INSERT INTO Tipografii(nume, adresa) VALUES 
('Penguin Random House', 'New York'),
('HarperCollins Publishers', 'New York'),
('Simon & Schuster', 'New York');
--Echipamente 
INSERT INTO Echipamente(nume, capacitate, pret, nume_producator, cod_tipografie) VALUES
('Xerox Versant 280', 80, 80000, 'Xerox Corporation',1),
('Canon imagePRESS C10000VP', 100, 150000, 'Canon Inc.',1),
('Konica Minolta AccurioPress C14000', 140, 200000, 'Konica Minolta, Inc.',1),
('Ricoh Pro C9200', 135, 180000, 'Ricoh Company, Ltd.',2),
('HP Indigo 12000 Digital Press', 4000, 750000, 'HP Inc.',2),
('Komori Lithrone GX40', 16000, 15000.50, 'Komori Corporation',2),
('Heidelberg Speedmaster XL 106', 18000, 1999999.99, 'Heidelberg Druckmaschinen AG',3),
('Mitsubishi Diamond V3000LX', 16000, 18000.50, 'Mitsubishi Heavy Industries, Ltd.',3),
('Roland 700 Evolution', 16000, 17000.70, 'Roland DG Corporation',3);
--Tiraje
INSERT INTO Tiraje(cod_carte, cod_tipografie, numar_copii, data_livrarii) VALUES
(1,1,1000, '2024-06-01'),
(1, 3, 500, '2024-06-15'),
(2,2, 250, '2024-03-01');