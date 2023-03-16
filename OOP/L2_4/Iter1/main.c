#include <stdio.h>
#include "Masina.h"
#include "Lista.h"
#include "InchirieriAutoService.h"

// 4. Inchiriere masini
//
// Creati o aplicatie pentru o firma de inchirieri auto. Fiecare masina are un numar de inmatriculare,
// model si categorie (mini, sport, suv, etc).
// Aplicatia permite:
// a) adaugare de masini
// b) actualizare masina existenta
// c) inchiriere masina/returnare masina
// d) Vizualizare masini dupa un criteru dat (categorie, model)
// e) Permite sortarea masinilor dupa: model sau categorie (crescator/descrescator)

void toateTestele(){
    testeCreeazaDistruge();
    testCreeazaLista();
    testIteratorLista();
    testCopieLista();
    testeAdaugaMasina();
    testeModificaMainsa();
    testeInchiriazaReturneaza();
}

void UIadaugaMasina(Lista* l){
    int nr;
    char model[30], categorie[30];
    scanf_s("%d", &nr);
    scanf_s("%s", model);
    scanf_s("%s", categorie);

    int eroare = adaugaMasina(l, nr, model, categorie);
    if (eroare != 0){
        printf("Masina invalida\n");
    } else{
        printf("Gata!\n");
    }
}

void UImodificaMasina(Lista* l){
    int nr;
    char model[30], categorie[30];
    scanf_s("%d", &nr);
    scanf_s("%s", model);
    scanf_s("%s", categorie);

    int eroare = modificaMasina(l, nr, model, categorie);
    if (eroare != 0){
        printf("Masina invalida\n");
    } else{
        printf("Gata!\n");
    }
}

void UIafiseazaMasini(Lista* l){
    for (int i = 0; i < dimensiune(l); ++i) {
        Masina m = elem(l, i);
        printf("[%d] %s - %s ", m.nr, m.model, m.categorie);
        if (m.inchiriata == 0)
            printf("Disponibila\n");
        else
            printf("Inchiriata\n");
    }
}

void UIinchiriaza(Lista* l){
    int nr;
    scanf_s("%d", &nr);

    int eroare = inchiriazaMasina(l, nr);
    if (eroare == 1)
        printf("Masina nu exista\n");
    else if (eroare == 2)
        printf("Masina e deja inchiriata\n");
    else if (eroare == 0)
        printf("Gata!\n");
}

void UIreturneaza(Lista* l){
    int nr;
    scanf_s("%d", &nr);

    int eroare = returneazaMasina(l, nr);
    if (eroare == 1)
        printf("Masina nu exista\n");
    else if (eroare == 2)
        printf("Masina nu e inchiriata\n");
    else if (eroare == 0)
        printf("Gata!\n");
}

void UIfiltrare(Lista* l){
    printf("1.Model 2.Categorie\n");
    int opt;
    scanf_s("%d", &opt);
    if (opt == 1){
        printf("Introdu substring:\n");
        char substring[30];
        scanf_s("%s", substring, 30);

        Lista filtrat = filtreazaModel(l, substring);
        UIafiseazaMasini(&filtrat);
    } else if (opt == 2){
        printf("Introdu substring:\n");
        char substring[30];
        scanf_s("%s", substring, 30);

        Lista filtrat = filtreazaCategorie(l, substring);
        UIafiseazaMasini(&filtrat);
    } else
        printf("Comanda invalida");
}

void UIsortare(){

}

void run(){
    printf("Meniu:\n0.Inchide\n1.Adauga\n2.Actualizeaza\n3.Inchiriere\n4.Returnare\n5.Filtrare\n6.Sortare\n7.Afiseaza Toate\n");
    int n;
    Lista l = creeazaGoala();
    int ruleaza = 1;
    while (ruleaza){
        // afiseaza toate masinile
        scanf_s("%d", &n);
        switch (n) {
            case 0:
                ruleaza = 0;
                break;
            case 1:
                printf("Introdu pe rand: nr. inmatriculare, model, categorie\n");
                UIadaugaMasina(&l);
                break;
            case 2:
                printf("Introdu pe rand: nr. inmatriculare existent, modelul noul, categoria noua\n");
                UImodificaMasina(&l);
                break;
            case 3:
                printf("Introdu nr masinii de inchiriat\n");
                UIinchiriaza(&l);
                break;
            case 4:
                printf("Introdu nr masinii de returnat\n");
                UIreturneaza(&l);
                break;
            case 5:
                UIfiltrare(&l);
                break;
            case 6:
                UIsortare();
                break;
            case 7:
                UIafiseazaMasini(&l);
                break;
            default:
                printf("Comanda invalida!\n");
        }
    }
}

int main() {
    toateTestele();
    run();
    return 0;
}
