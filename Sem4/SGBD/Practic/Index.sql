USE PracticS1

SELECT cod_carte, titlu FROM Carti

CREATE NONCLUSTERED INDEX ix_carti ON Carti(titlu)