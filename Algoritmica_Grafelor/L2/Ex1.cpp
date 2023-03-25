//
// Created by Daniel on 23-Mar-23.
//

#include "Ex1.h"
#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

#define Inf 999999

// 1. Implementați algoritmul lui Moore pentru un graf orientat neponderat (algoritm bazat pe
// Breath-first search, vezi cursul 2). Datele sunt citete din fisierul graf.txt. Primul rând din graf.txt conține
// numărul vârfurilor, iar următoarele rânduri conțin muchiile grafului. Programul trebuie să afiseze lanțul
// cel mai scurt dintr-un vârf (vârful sursa poate fi citit de la tastatura).

void Moore(int lungime_drum[31], int parinte[31], int s, int nr, int matr_adj[31][31]){
    vector<int> coada;
    lungime_drum[s] = 0;
    for (int i = 1; i <= nr; i++)
        if (i != s)
            lungime_drum[i] = Inf;
    coada.push_back(s);
    while (!coada.empty()){
        int u = coada[0];
        coada.erase(coada.begin());
        for (int i = 1; i <= nr; i++)
            if (matr_adj[u][i] == 1 && lungime_drum[i] == Inf){
                lungime_drum[i] = lungime_drum[u] + 1;
                parinte[i] = u;
                coada.push_back(i);
            }
    }
}

void DrumuriMoore(int lungime_drum[31], int parinte[31], int nr){
    vector<int> drumuri(nr+1);

    for (int i = 1; i <= nr; i++){
        if (lungime_drum[i] == Inf || lungime_drum[i] == 0)
            continue;
        int k = lungime_drum[i];
        drumuri[k] = i;
        while (k!=0){
            drumuri[k-1] = parinte[drumuri[k]];
            k--;
        }
        cout << "Drumul dintre " << drumuri[0] << " si " << drumuri[lungime_drum[i]] << " este: ";
        for (int j = 0; j <= lungime_drum[i]; j++)
            cout << drumuri[j] << " ";
        cout << '\n';
        drumuri.clear();
    }
}

void ex1(){
    int nr_muchii, matr_adj[31][31] = {0};

    ifstream f ("graf.txt");
    f >> nr_muchii;
    int a, b;
    while (f >> a){
        f >> b;
        matr_adj[a][b] = 1;
    }
    f.close();

    cout << "Ex1; Varf sursa:";
    int varf_sursa;
    cin >> varf_sursa;
    int lungime_drum[31] = {0};
    int parinte[31] = {0};
    Moore(lungime_drum, parinte, varf_sursa, nr_muchii, matr_adj);
    DrumuriMoore(lungime_drum, parinte, nr_muchii);
}