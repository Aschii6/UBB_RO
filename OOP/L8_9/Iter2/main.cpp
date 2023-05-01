#include "Teste.h"
#include "Console.h"
#include "Repo.h"
#include "Validator.h"
#include "CosInchirieri.h"
#include "Service.h"

// 3. Creați o aplicație care permite:
// gestiunea unei liste de cârti. Carte: titlu, autor, gen, anul apariției
// adăugare, ștergere, modificare și afișare cârti
// căutare carte
// filtrare cârti după: titlu, anul apariției
// sortare cârti după titlu, autor, anul apariției + gen

// Introduceți posibilitatea de a crea un cos de închirieri. Utilizatorul poate crea
// interactiv o lista de cârti pe care dorește sa le închirieze.
// Adăugați următoarele acțiuni (elemente de meniu):
// Golește cos: șterge toate cărțile din cos.
// Adaugă in cos: adaugă in cos o carte după titlu
// Generează cos: utilizatorul introduce numărul total de cârti, coșul se populează aleator cu cărți
// Export. Salvează cos in fișier: CVS sau HTML. Se face salvarea in fișier doar
// când s-a selectat acest meniu, coșul nu este persistent (la repornire aplicație
// coșul este din nou gol), utilizatorul introduce numele fișierului unde se salvează la fiecare export.
// După fiecare acțiune utilizator se afișează numărul total al cărților din cos

// Iter 2
// UndoAction + salvare in fisiere

int main() {
    toateTestele();
    Repo repo("Biblioteca.csv");
    Validator validator;
    CosInchirieri cos;
    Service service(repo, validator, cos);
    Console console(service);
    console.run();
    return 0;
}