//
// Created by Daniel on 23-Mar-23.
//

#include "Ex5.h"
#include <vector>
#include <fstream>
#include <iostream>
using namespace std;

#define Inf 999999

// 5. Pentru un graf dat să se afișeze pe ecran vârfurile descoperite de apelul recursiv al procedurii
// DFS_VISIT(G, u) (padurea descoperită de DFS).

void DFS_Visit(int u, vector<vector<int>> listaAdj, int culoare[], int descoperire[], int timp[], int &timp_curent){
    culoare[u] = 1; // gri
    timp_curent++;
    descoperire[u] = timp_curent;
    for (int i = 0; i < listaAdj[u].size(); i++){
        int v = listaAdj[u][i];
        if (culoare[v] == 0){
            DFS_Visit(v, listaAdj, culoare, descoperire, timp, timp_curent);
        }
    }
    culoare[u] = 2; // negru
    timp_curent++;
    timp[u] = timp_curent;
}

void ex5(){
    ifstream f("graf.txt");
    int nr;
    f >> nr;
    vector<vector<int>> listaAdj (nr + 1);
    int a, b;
    while (f >> a){
        f >> b;
        listaAdj[a].push_back(b);
    }
    f.close();

    cout << "Ex5; Varful sursa:";
    int vs;
    cin >> vs;

    // DFS
    int culoare[31] = {0}, descoperire[31], timp[31] = {0};
    int timp_curent = 0;

    for (int i = 1; i <= nr; i++)
        descoperire[i] = Inf;

    DFS_Visit(vs, listaAdj, culoare, descoperire, timp, timp_curent);

    for (int i = 1; i <= nr; i++)
        cout << "Varf: " << i << " Descoperire: " << descoperire[i] << " Timp: " << timp[i] <<'\n';
}