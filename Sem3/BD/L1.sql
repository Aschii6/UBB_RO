USE GradinaZoo

CREATE TABLE Specie
(
cod_specie INT PRIMARY KEY IDENTITY,
nume_specie VARCHAR(50)
);

CREATE TABLE Zona
(
cod_zona INT PRIMARY KEY IDENTITY,
denumire_zona VARCHAR(50),
suprafata REAL
);

CREATE TABLE Cusca
(
cod_cusca INT PRIMARY KEY IDENTITY,
mediu VARCHAR(50),
capacitate INT,
cod_zona INT FOREIGN KEY REFERENCES Zona(cod_zona) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Animal
(
cod_animal INT PRIMARY KEY IDENTITY,
nume_animal VARCHAR(50),
cod_specie INT FOREIGN KEY REFERENCES Specie(cod_specie) ON UPDATE CASCADE ON DELETE CASCADE,
cod_cusca INT FOREIGN KEY REFERENCES Cusca(cod_cusca) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Ingrijitor
(
cod_ingrijitor INT PRIMARY KEY IDENTITY,
nume_ingrijitor VARCHAR(50),
salariu_ingrijitor INT
);

CREATE TABLE ResponabilitateDeIngrijire
(
cod_ingrijitor INT FOREIGN KEY REFERENCES Ingrijitor(cod_ingrijitor) ON UPDATE CASCADE ON DELETE CASCADE,
cod_cusca INT FOREIGN KEY REFERENCES Cusca(cod_cusca) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY (cod_ingrijitor, cod_cusca)
);

CREATE TABLE GhidTuristic
(
cod_ghid INT PRIMARY KEY IDENTITY,
nume_ghid VARCHAR(50),
cod_zona INT FOREIGN KEY REFERENCES Zona(cod_zona) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE CosturiLunareCusca
(
cod_cost INT PRIMARY KEY IDENTITY,
cost INT,
cod_cusca INT FOREIGN KEY REFERENCES Cusca(cod_cusca) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE CosturiLunareCusca
ADD luna INT

ALTER TABLE CosturiLunareCusca
ADD an INT

CREATE TABLE Vizitator
(
cod_vizitator INT PRIMARY KEY IDENTITY,
nume_vizitator VARCHAR(50),
data_nasterii DATE
);

CREATE TABLE RatingCusca
(
cod_cusca INT FOREIGN KEY REFERENCES Cusca(cod_cusca) ON UPDATE CASCADE ON DELETE CASCADE,
cod_vizitator INT FOREIGN KEY REFERENCES Vizitator(cod_vizitator) ON UPDATE CASCADE ON DELETE CASCADE,
PRIMARY KEY (cod_cusca, cod_vizitator),
rating INT CHECK (rating BETWEEN 1 AND 5)
);

INSERT INTO Specie (nume_specie) VALUES ('Leu'), ('Urs'), ('Crocodil'), ('Flamingo'), ('Capybara'), ('Cerb'), ('Ciute'), ('Tigru'), ('Porc Mistret');

INSERT INTO Zona (denumire_zona, suprafata) VALUES ('Ramura de Nord', 300), ('Ramura de Est', 225), ('Ramura de Sud', 500), ('Ramura de Vest', 415),
('Zona Centrala', 800);

INSERT INTO Cusca (mediu, capacitate, cod_zona) VALUES ('Padure de foioase', 40, 3), ('Savana', 25, 1), ('Pajiste', 50, 3), ('Rau', 20, 4),
('Padure de conifere', 15, 2);

INSERT INTO Cusca (mediu, capacitate, cod_zona) VALUES ('Alt rau', 20, 4);

INSERT INTO Animal (nume_animal, cod_specie, cod_cusca) VALUES ('Mirela', 8, 2), ('Bartolomeu', 8, 2), /* tigri in zona de savana */
('Balu', 2, 5), ('Winnie', 2, 5), ('Mos Martin', 2, 5), ('Fraguta', 2, 5), /* ursi in padurea de conifere */
('Ok I Pull Up', 5, 4), ('Poco', 5, 4), ('Pateu', 5, 4), ('Priscilla', 5, 4), /* capybara la rau */
('Pinky', 4, 4), ('OneLeg', 4, 4), ('Fisher', 4, 4), ('Cartofel', 4, 4) /* flamingo la rau */

INSERT INTO Animal (nume_animal, cod_specie, cod_cusca) VALUES ('Cerberus', 6, 1), ('Caprita', 7, 1), ('Olguta', 7, 1), ('Leana', 7, 1), /* cerb si ciute in padurea de foioase */
('Pumba', 9, 1), ('Petru', 9, 1), ('Mrs. Scronch', 9, 1), ('Marinela', 9, 1) /* porci mistreti in padurea de foioase */

INSERT INTO Animal (nume_animal, cod_specie, cod_cusca) VALUES ('Croco', 3, 1002), ('Broco', 3, 1002), ('Loco', 3, 1002) /* crocodili la alt rau */

INSERT INTO Animal (nume_animal, cod_specie, cod_cusca) VALUES ('Simba', 1, 2), ('Nala', 1, 2), ('Mufasa', 1, 2) /* lei la savana */

INSERT INTO Ingrijitor (nume_ingrijitor, salariu_ingrijitor) VALUES ('Andrei', 2300), ('Alex', 2200), ('Paula', 2500), ('Pavel', 2400), ('Cornel', 2250),
('Liviu', 2000), ('Marius', 2000), ('Iulia', 1600), ('Carmen', 2100), ('Bianca', 1900), ('Darius', 1700), ('Eusebiu', 1200), ('Emma', 1800)

INSERT INTO ResponabilitateDeIngrijire (cod_ingrijitor, cod_cusca) VALUES (1, 1), (2, 1), (3, 2), (4, 2), (5, 2), (6, 3), (7, 3), (8, 4), (9, 4),
(10, 5), (11, 5), (12, 1002), (13, 1002)

INSERT INTO ResponabilitateDeIngrijire (cod_ingrijitor, cod_cusca) VALUES (8, 1002)

INSERT INTO GhidTuristic (nume_ghid, cod_zona) VALUES ('Ion', 1), ('Cristi', 1), ('Costel', 2), ('Mitica', 3), ('Pablo', 3), ('Andreea', 4), 
('Adriana', 4), ('Ilinca', 5), ('George', 5), ('Laura', 5)

INSERT INTO CosturiLunareCusca (cost, cod_cusca, luna, an) VALUES (2500, 1, 10, 2023), (4700, 2, 10, 2023), (5000, 2, 11, 2023), (3800, 3, 10, 2023),
(3500, 4, 10, 2023), (3400, 5, 10, 2023), (4000, 1002, 10, 2023)

INSERT INTO Vizitator (nume_vizitator, data_nasterii) VALUES ('Adela', '2002-10-23'), ('Daniel', '2001-7-16'), ('Catalina', '1999-9-19'),
('Elisabeta', '2004-5-20'), ('Izabela', '2005-12-22'), ('Laurentiu', '2000-6-29'), ('Stefan', '1998-3-21'), ('Stefania', '2003-8-24')

INSERT INTO Vizitator (nume_vizitator, data_nasterii) VALUES ('Kristoff', '2005-10-23'), ('Xiangling', '2000-10-20')

INSERT INTO RatingCusca (cod_cusca, cod_vizitator, rating) VALUES (1, 1, 5), (3, 1, 4), (2, 2, 4), (2, 4, 5), (5, 3, 3), (4, 4, 5), (5, 4, 4),
(1, 5, 4), (1002, 6, 4), (5, 7, 5), (3, 7, 4), (3, 8, 4)

SELECT * FROM Specie
SELECT * FROM Zona
SELECT * FROM Cusca
SELECT * FROM CosturiLunareCusca
SELECT * FROM Animal
SELECT * FROM Ingrijitor
SELECT * FROM ResponabilitateDeIngrijire
SELECT * FROM GhidTuristic
SELECT * FROM Vizitator
SELECT * FROM RatingCusca