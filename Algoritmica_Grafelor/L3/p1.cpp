#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
using namespace std;

// Având dat un graf orientat ponderat și un vârf sursă,
// calculați costul minim de la vârful sursă până la fiecare vârf accesibil din graf.
// Implementarea trebuie să fie eficientă din punctul de vedere al memoriei folosite și al timpului de execuție.
// (Implementați un algoritm de drum minim în graf: Bellman-Ford, Dijkstra)

const int Inf = 1000001;

struct myComp {
    constexpr bool operator()(
            pair<int, int> const& a,
            pair<int, int> const& b)
    const noexcept
    {
        return a.second > b.second;
    }
};

void Dijkstra(vector<int>& drumuriCostMinim, vector<vector<pair<int,int>>>& listaAdj, int nrVarfuri, int nodSursa){

    drumuriCostMinim[nodSursa] = 0;
    priority_queue<pair<int,int>, vector<pair<int,int>>, myComp> pq;
    vector<bool> vizitat(nrVarfuri, false);

    pq.push({nodSursa, 0});

    while (!pq.empty()){
        int varf = pq.top().first;
        pq.pop();
        if (vizitat[varf])
            continue;
        vizitat[varf] = true;
        for (auto& vecin : listaAdj[varf]){
            if (drumuriCostMinim[vecin.first] > drumuriCostMinim[varf] + vecin.second){
                drumuriCostMinim[vecin.first] = drumuriCostMinim[varf] + vecin.second;
                pq.push({vecin.first, drumuriCostMinim[vecin.first]});
            }
        }
    }
}

int main(int argc, char* argv[]){
    if (argc < 3){
        cout << "Parametrii in linia de comanda insuficienti\n";
        return 0;
    }
    ifstream f (argv[1]);
    if (!f){
        cout << "Fisierele nu au fost gasite\n";
        return 0;
    }
    ofstream g (argv[2]);

    int nrVarfuri, nrArce, nodSursa;
    f >> nrVarfuri;
    f >> nrArce;
    f >> nodSursa;

    vector<vector<pair<int,int>>> listaAdj(nrVarfuri);
    vector<int> drumuriCostMinim(nrVarfuri, Inf);

    for (int i = 0; i < nrArce; ++i) {
        int varf1, varf2, pondere;
        f >> varf1 >> varf2 >> pondere;
        listaAdj[varf1].emplace_back(varf2, pondere);
    }

    Dijkstra(drumuriCostMinim, listaAdj, nrVarfuri, nodSursa);

    for (int i = 0; i < nrVarfuri; ++i) {
        if (drumuriCostMinim[i] == Inf)
            g << "INF" << ' ';
        else
            g << drumuriCostMinim[i] << ' ';
    }
    g << '\n';

    f.close();
    g.close();
    return 0;
}