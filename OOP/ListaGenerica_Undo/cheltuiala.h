#pragma once

typedef struct{
    int id;
    int zi;
    float suma;
    char* tip;
}Cheltuiala;

/***
 * Creeaza o cheltuiala.
 * @param id int
 * @param zi int
 * @param suma float
 * @param tip char
 * @return cheltuiala
 */
Cheltuiala* constructor_cheltuiala(int id, int zi, float suma, char* tip);

/***
 * Dealoca memoria alocata pt cheltuiala
 * @param c Cheltuiala
 */
void destructor_cheltuiala(Cheltuiala* c);

/***
 * Returneaza o copie a cheltuileii
 * @param c Cheltuiala
 * @returncopia cheltuiala
 */
Cheltuiala* copy_cheltuiala(Cheltuiala* c);
