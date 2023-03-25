//
// Created by Daniel on 23-Mar-23.
//

#include "Ex3.h"
#include <fstream>
#include <vector>
#include <iostream>
using namespace std;

#define Inf 999999

// 3. Să se scrie un program care găsește o soluție pentru unul din următoarele labirinturi: labirint_1.txt.
//  Pentru labirintul 1 si 2 punctul de pornire este indicat de litera S (am inlocuit cu cifra 2)
//  și punctul de oprire este indicat de litera F (am inlocuit cu cifra 3).

void citesteLabirint(int Labirint[22][42], int &start, int &finish){
    int citit;
    ifstream f("labirint1.txt");
    for (int i = 1; i <= 21; i++)
        for (int j = 1; j <= 41; j++){
            f >> citit;
            if (citit == 2){
                start = 41 * (i - 1) + j;
                Labirint[i][j] = 1;
            }
            else if (citit == 3){
                finish = 41 * (i - 1) + j;
                Labirint[i][j] = 1;
            }
            else
                Labirint[i][j] = !citit;
        }
    f.close();
}

void ex3(){
    int Labirint1[22][42] = {0}; // are dimensiune 21x41
    int start, finish;
    citesteLabirint(Labirint1, start, finish);

    // BFS pemntru a gasi un drum de la start la finish

    int culoare[882] = {0}, distanta[882] = {0}, parinte[882] = {0};

    for (int i = 1; i <= 881; i++){
        if (i == start)
            continue;
        distanta[i] = Inf;
        parinte[i] = 0;
        culoare[i] = 0; // alb
    }

    vector<int> coada;
    coada.push_back(start);
    culoare[start] = 1; // gri
    distanta[start] = 0;
    parinte[start] = 0;

while (!coada.empty()){
        int u = coada[0];
        coada.erase(coada.begin());
        for (int i = 0; i < 4; i++){
            int v;
            if (i == 0)
                v = u - 41;
            else if (i == 1)
                v = u + 41;
            else if (i == 2)
                v = u - 1;
            else
                v = u + 1;
            if (Labirint1[(v - 1) / 41 + 1][(v - 1) % 41 + 1] == 1 && culoare[v] == 0){
                culoare[v] = 1;
                distanta[v] = distanta[u] + 1;
                parinte[v] = u;
                coada.push_back(v);
            }
        }
        culoare[u] = 2; // negru
    }

    if (distanta[finish] == Inf){
        cout << "Nu exista drum de la start la finish";
        return;
    }

    // afisare drum
    int drum[882] = {0}, k = 0;
    int u = finish;
    while (u != start){
        drum[k++] = u;
        u = parinte[u];
    }

    cout << "Drumul de la start la finish este:\n";
    for (int i = k - 1; i >= 0; i--)
        cout << drum[i] << ' ';
}