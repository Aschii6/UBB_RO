//
// Created by Daniel on 23-Mar-23.
//

#include "Ex2.h"
#include <fstream>
#include <iostream>
using namespace std;

// 2. Sa se determine închiderea transitivă a unui graf orientat. (Închiderea tranzitivă poate
// fi reprezentată ca matricea care descrie, pentru fiecare vârf în parte, care sunt vârfurile accesibile
// din acest vârf. Matricea inchiderii tranzitive arată unde se poate ajunge din fiecare vârf.)

void Warshall(int nr_muchii, int inchiderea_tranzitiva[31][31]){
    for (int k = 1; k <= nr_muchii; k++)
        for (int i = 1; i <= nr_muchii; i++)
            for (int j = 1; j <= nr_muchii; j++)
                if (inchiderea_tranzitiva[i][j] == 0)
                    inchiderea_tranzitiva[i][j] = inchiderea_tranzitiva[i][k] & inchiderea_tranzitiva[k][j];
}

void ex2(){
    int nr_muchii, matr_adj[31][31] = {0};
    int inchiderea_tranzitiva[31][31] = {0};

    ifstream f ("graf.txt");
    f >> nr_muchii;
    int a, b;
    while (f >> a){
        f >> b;
        matr_adj[a][b] = 1;
    }
    f.close();

    for (int i = 1; i <= nr_muchii; i++) {
        inchiderea_tranzitiva[i][i] = 1;
        for (int j = 1; j <= nr_muchii; j++)
            if (matr_adj[i][j] == 1)
                inchiderea_tranzitiva[i][j] = 1;
    }
    Warshall(nr_muchii, inchiderea_tranzitiva);

    cout << "Ex2; Inchiderea tranzitiva:\n";
    for (int i = 1; i <= nr_muchii; i++){
        for (int j = 1; j <= nr_muchii; j++)
            cout << inchiderea_tranzitiva[i][j] << " ";
        cout << '\n';
    }
}
