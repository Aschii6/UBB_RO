#include <iostream>
#include <fstream>
#include <vector>
#include <limits>
using namespace std;

void DFS(int x, int n, int vizitat[21], int matr_adj[21][21]){
    vizitat[x] = 1;
    for (int i = 1; i <= n; i++)
        if (matr_adj[x][i] == 1 && vizitat[i] == 0)
            DFS(i, n, vizitat, matr_adj);
}

void Conex(int n, int matr_adj[21][21]){
    int vizitat[21] = {0};
    int cond = 1;
    DFS(1, n, vizitat, matr_adj);
    for (int i = 1; i <= n; i++)
        if (vizitat[i] == 0)
            cond = 0;

    if (cond == 1)
        cout << "Graful este conex";
    else
        cout << "Graful nu este conex";
}

void Matricea_Distantelor(int n, int matr_adj[21][21]){
    float Inf = numeric_limits<float>::infinity();
    float matr_dist[21][21];

    for (int i = 1; i <= n; i++)
        for (int j = 1; j <= n; j++)
            if (matr_adj[i][j] == 1 && i != j)
                matr_dist[i][j] = 1;
            else
                matr_dist[i][j] = Inf;

    for (int i = 1; i <= n; i++)
        for (int j = 1; j <= n; j++)
            for (int k = 1; k <= n; k++)
                if (i != j)
                    matr_dist[i][j] = min(matr_dist[i][j], matr_dist[i][k] + matr_dist[k][j]);
                else
                    matr_dist[i][j] = 0;

    cout << "Matricea distantelor:\n";
    for (int i = 1; i <= n; i++){
        for (int j = 1; j <= n; j++)
            cout << matr_dist[i][j] << ' ';
        cout << '\n';
    }
}

void Regular(int n, int matr_adj[21][21]){
    int grad_1 = 0, cond = 1;
    for (int j=1; j<=n; j++)
        if (matr_adj[1][j] == 1)
            grad_1++;

    for (int i=2; i<=n; i++){
        int grad = 0;
        for (int j=1; j<=n; j++)
            if (matr_adj[i][j] == 1)
                grad++;
        if (grad_1 != grad){
            cond = 0;
            break;
        }
    }
    if (cond == 1)
        cout << "Graful este regular\n";
    else
        cout << "Graful nu este regular\n";
}

void Noduri_Izolate(int n, int matr_adj[21][21]){
    cout << "Nodurile izolate sunt: ";

    for (int i=1; i<=n; i++) {
        int ok = 1;
        for (int j = 1; j <=n; j++)
            if(matr_adj[i][j] == 1){
                ok = 0;
            }
        if (ok == 1)
            cout << i << ' ';
    }
    cout << '\n';
}

void ex1(){
    int n, matr_adj[21][21] = {0};
    ifstream f ("fisier.txt");
    int a, b;
    f >> n;
    while (f >> a){
        f >> b;
        matr_adj[a][b] = 1;
        matr_adj[b][a] = 1;
    }
    f.close();

    cout << "Matrice de adiacenta:\n";
    for(int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++)
            cout << matr_adj[i][j] << ' ';
        cout << '\n';
    }

    // matr de adiacenta -> lista de adiacenta
    vector<vector<int>> lista_adj (n);

    for(int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++)
            if (matr_adj[i+1][j+1] == 1)
                lista_adj[i].push_back(j+1);
    }

    cout << "Lista de adiacenta: [ ";
    for (int i = 0; i < lista_adj.size(); i++){
            cout << '[';
            if (lista_adj[i].size() > 0){
                for (int j = 0; j < lista_adj[i].size() - 1; j++)
                    cout << lista_adj[i][j] << ", ";
                if (lista_adj[i].size() >= 1)
                    cout << lista_adj[i][lista_adj[i].size() - 1];
            }
            cout << "] ";
    }
    cout << "]\n";

    // lista de adiacenta -> matrice de incidenta
    int matr_inc[21][51] = {0};
    int nr_muchii = 0;

    for(int i = 0; i < lista_adj.size(); i++)
        for (int j = 0; j < lista_adj[i].size(); j++)
            if (i < lista_adj[i][j]){
                nr_muchii++;
                matr_inc[i][nr_muchii] = 1;
                matr_inc[lista_adj[i][j] - 1][nr_muchii] = 1;
            }

    cout << "Matricea de incidenta este:\n";
    for (int i = 0; i < n; i++){
        for (int j = 1; j <= nr_muchii; j++)
            cout << matr_inc[i][j] << ' ';
        cout << '\n';
    }

    // matrice de incidenta -> lista de adiacenta
    vector<vector<int>> lista_adj_c (n);

    for (int j = 1; j <= nr_muchii; j++){
        int m1 = -1, m2 = -1;
        for (int i = 0; i < n; i++)
            if (matr_inc[i][j] == 1){
                if (m1 == -1)
                    m1 = i;
                else
                    m2 = i;
            }
        lista_adj_c[m1].push_back(m2+1);
        lista_adj_c[m2].push_back(m1+1);
    }

    cout << "Lista de adiacenta: [ ";
    for (int i = 0; i < lista_adj_c.size(); i++){
        cout << '[';
        if (lista_adj_c[i].size() > 0){
            for (int j = 0; j < lista_adj_c[i].size() - 1; j++)
                cout << lista_adj_c[i][j] << ", ";
            if (lista_adj_c[i].size() >= 1)
                cout << lista_adj_c[i][lista_adj_c[i].size() - 1];
        }
        cout << "] ";
    }
    cout << "]\n";

    // lista de adiacenta -> matrice de adiacenta
    int matr_adj_c[21][21] = {0};

    for(int i = 0; i < lista_adj_c.size(); i++)
        for (int j = 0; j < lista_adj_c[i].size(); j++)
            matr_adj_c[i+1][lista_adj_c[i][j]] = 1;

    cout << "Matrice de adiacenta:\n";
    for(int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++)
            cout << matr_adj_c[i][j] << ' ';
        cout << '\n';
    }

    // matrice de adiacenta -> lista
    vector<int> lista;

    for(int i = 1; i <= n; i++) {
        for (int j = i + 1; j <= n; j++)
            if (matr_adj_c[i][j] == 1){
                lista.push_back(i);
                lista.push_back(j);
            }
    }

    cout << "Lista de muchii: ";
    for (int i = 0; i < lista.size(); i += 2)
        cout << '(' << lista[i] << ", " << lista[i+1] << ')';
    cout << '\n';
}

void ex2(){
    // citire si memorare in matr. de adiacenta
    ifstream f ("fisier.txt");
    int n, matr_adj[21][21] = {0};
    int a, b;
    f >> n;
    while (f >> a){
        f >> b;
        matr_adj[a][b] = 1;
        matr_adj[b][a] = 1;
    }

    Noduri_Izolate(n, matr_adj);
    Regular(n, matr_adj);
    Matricea_Distantelor(n, matr_adj);
    Conex(n, matr_adj);
}

int main() {
    ex1();
    ex2();
    return 0;
}
