typedef struct {
    Lista_cheltuieli *lista;
    Lista_cheltuieli *lista_undo;
} Service;

/***
 * Creeaza o lista de cheltuieli
 * @return lista de cheltuieli de tip Lista_cheltuieli
 */
Service constructor_service();

/***
 * Destructor lista
 * @param lista Lista_cheltuieli
 */
void destructor_service(Service* s);

/***
 * Adauga o cheltuiala cu zi, suma si tip date in lista
 * @param lista Lista_cheltuieli
 * @param zi int
 * @param suma float
 * @param tip char
 * @return  -> 0 daca adaugarea a fost efectuata cu succes
 *          -> -1 daca exista deja acea cheltuiala
 */
int adauga_cheltuiala_service(Service* s, int zi, float suma, char tip[]);

/***
 * Modifica obiectul cu id dat cu atributele date
 * @param lista Lista_cheltuieli
 * @param id int
 * @param zi_noua int
 * @param suma_noua float
 * @param tip_nou  char
 * @return -> 0 daca modificarea a fost efectuata cu succes
 *         -> -1 daca nu exista acea cheltuiala cu id dat
 */
int modifica_cheltuiala_service(Service *s, int id, int zi_noua, float suma_noua, char tip_nou[]);

/***
 * Sterge o cheltuiala cu id dat
 * @param lista Lista_cheltuieli
 * @param id int
 * @return -> 0 daca stergerea a fost efectuata cu succes
 *         -> -1 daca nu exist acea cheltuiala
 */
int sterge_cheltuiala_service(Service *s, int id);
