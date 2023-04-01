#include "Teste.h"
#include "Console.h"
#include "Repo.h"
#include "Validator.h"
#include "Service.h"

// 3. Creați o aplicație care permite:
// gestiunea unei liste de cârti. Carte: titlu, autor, gen, anul apariției
// adăugare, ștergere, modificare și afișare cârti
// căutare carte
// filtrare cârti după: titlu, anul apariției
// sortare cârti după titlu, autor, anul apariției + gen

int main() {
    toateTestele();
    Repo repo;
    Validator validator;
    Service service(repo, validator);
    Console console(service);
    console.adaugaCateva();
    console.run();
    return 0;
}