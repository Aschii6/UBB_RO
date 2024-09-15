USE PracticS1

BEGIN TRAN

UPDATE Tipografii SET adresa = 'Los Angeles' WHERE cod_tipografie = 1

WAITFOR DELAY '00:00:06'

ROLLBACK TRAN