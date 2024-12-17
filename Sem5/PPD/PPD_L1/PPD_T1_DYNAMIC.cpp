#include <iostream>
#include <chrono>
#include <fstream>
#include <thread>
#include <sstream>

using namespace std;

int** F;
int** C;
int** res;

int N, M;
int n, m;

void readFromFile() {
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    string line;

    ifstream fin(R"(C:\Users\Daniel\source\repos\PPD_T1_FILES\dataF.txt)");
    for (int i = 0; i < N; i++) {
        getline(fin, line);
        istringstream iss(line);

        for (int j = 0; j < M; j++) {
            iss >> F[i + nPadSize][j + mPadSize];
        }

    }
    fin.close();

    ifstream fin2(R"(C:\Users\Daniel\source\repos\PPD_T1_FILES\dataC.txt)");
    for (int i = 0; i < n; i++) {
        getline(fin2, line);
        istringstream iss(line);

        for (int j = 0; j < m; j++) {
            iss >> C[i][j];
        }
    }
    fin2.close();
}

void padMatrix() {
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    for (int i = nPadSize; i < N + nPadSize; i++) {
        for (int j = mPadSize - 1; j >= 0; j--) {
            F[i][j] = F[i][j + 1];
        }
        for (int j = mPadSize; j < mPadSize * 2; j++) {
            F[i][M + j] = F[i][M + j - 1];
        }
    }

    for (int j = 0; j < M + mPadSize * 2; j++) {
        for (int i = nPadSize - 1; i >= 0; i--) {
            F[i][j] = F[i + 1][j];
        }
        for (int i = nPadSize; i < nPadSize * 2; i++) {
            F[N + i][j] = F[N + i - 1][j];
        }
    }
}

void checkResult(int matrixType) {
    string fileName = R"(C:\Users\Daniel\source\repos\PPD_T1_FILES\seqRes)" + to_string(matrixType) + ".txt";
    ifstream fin(fileName);

    int val;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            fin >> val;
            if (res[i][j] != val) {
                throw runtime_error("Incorrect result");
            }
        }
    }
    fin.close();
}

/*
* Abordare secventiala
*/
void sequentialConvolution() {
    // dimensiunea bordurii in jurul lui F
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    // Operatia de convolutie
    for (int i = nPadSize; i < N + nPadSize; i++) {
        for (int j = mPadSize; j < M + mPadSize; j++) {
            int sum = 0;
            for (int k = -nPadSize; k <= nPadSize; k++) {
                for (int l = -mPadSize; l <= mPadSize; l++) {
                    sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                }
            }
            res[i - nPadSize][j - mPadSize] = sum;
        }
    }
}

/*
* Abordare paralela cu impartirea pe linii
* Daca nr de linii nu se impart exact la nr de threaduri,
* atunci primele r threaduri vor face o linie in plus, r fiind restul impartirii
*/
void rowConvolution(int p) {
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    int rows = N / p;
    int rest = N % p;

    int startRow = nPadSize;
    int endRow = nPadSize + rows;

    thread* threads = new thread[p];

    for (int i = 0; i < p; i++) {
        if (rest > 0) {
            endRow++;
            rest--;
        }

        threads[i] = thread([=]() {
            // Operatia de convolutie
            for (int i = startRow; i < endRow; i++) {
                for (int j = mPadSize; j < M + mPadSize; j++) {
                    int sum = 0;
                    for (int k = -nPadSize; k <= nPadSize; k++) {
                        for (int l = -mPadSize; l <= mPadSize; l++) {
                            sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                        }
                    }
                    res[i - nPadSize][j - mPadSize] = sum;
                }
            }
            });

        startRow = endRow;
        endRow += rows;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }

    delete[] threads;
}

/*
* Abordare paralela cu impartire pe coloane
* Daca nr de coloane nu se imparte exact la nr de threaduri,
* atunci primele r threaduri vor face o coloana in plus, r fiind restul impartirii
*/
void columnConvolution(int p) {
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    int columns = M / p;
    int rest = M % p;

    int startColumn = mPadSize;
    int endColumn = mPadSize + columns;

    thread* threads = new thread[p];

    for (int i = 0; i < p; i++) {
        if (rest > 0) {
            endColumn++;
            rest--;
        }

        threads[i] = thread([=]() {
            // Operatia de convolutie
            for (int j = startColumn; j < endColumn; j++) {
                for (int i = nPadSize; i < N + nPadSize; i++) {
                    int sum = 0;
                    for (int k = -nPadSize; k <= nPadSize; k++) {
                        for (int l = -mPadSize; l <= mPadSize; l++) {
                            sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                        }
                    }
                    res[i - nPadSize][j - mPadSize] = sum;
                }
            }
            });

        startColumn = endColumn;
        endColumn += columns;

    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }

    delete[] threads;
}

/*
* Abordare paralela cu impartire pe blocuri
* Se imparte matricea in p blocuri de dimensiuni egale, folosind cei mai apropiati divizori ai lui p
* care inmultiti dau p
* Astfel blocurile impartite vor avea dimensiuni echilibrate
*/
void blockConvolution(int p) {
    int nPadSize = (n - 1) / 2;
    int mPadSize = (m - 1) / 2;

    // cei mai apropiati divizori ai lui p (d1 - d2 este minim) care inmultiti dau p
    int p1 = 1;
    int p2 = p;

    for (int i = sqrt(p); i > 0; i--) {
        if (p % i == 0) {
            p1 = i;
            p2 = p / i;
            break;
        }
    }

    int rows = N / p1;
    int restRows = N % p1;

    int columns = M / p2;
    int restColumns = M % p2;

    int startRow = nPadSize;
    int endRow = nPadSize + rows;

    int startColumn = mPadSize;
    int endColumn = mPadSize + columns;

    thread* threads = new thread[p];

    for (int i = 0; i < p1; i++) {
        if (restRows > 0) {
            endRow++;
            restRows--;
        }

        for (int j = 0; j < p2; j++) {
            if (restColumns > 0) {
                endColumn++;
                restColumns--;
            }

            threads[i * p2 + j] = thread([=]() {
                // Operatia de convolutie
                for (int i = startRow; i < endRow; i++) {
                    for (int j = startColumn; j < endColumn; j++) {
                        int sum = 0;
                        for (int k = -nPadSize; k <= nPadSize; k++) {
                            for (int l = -mPadSize; l <= mPadSize; l++) {
                                sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                            }
                        }
                        res[i - nPadSize][j - mPadSize] = sum;
                    }
                }
                });
            startColumn = endColumn;
            endColumn += columns;
        }

        startRow = endRow;
        endRow += rows;

        startColumn = mPadSize;
        endColumn = mPadSize + columns;
        restColumns = M % p2;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }

    delete[] threads;
}

int main(int argc, char* argv[])
{
    int p = 4;
    int matrixType = 1;

    if (argc >= 2) {
        p = atoi(argv[1]);
        matrixType = atoi(argv[2]);
    }

    switch (matrixType) {
    case 1: {
        N = 10;
        M = 10;
        n = 3;
        m = 3;
        break;
    }
    case 2: {
        N = 1000;
        M = 1000;
        n = 5;
        m = 5;
        break;
    }
    case 3: {
        N = 10;
        M = 10000;
        n = 5;
        m = 5;
        break;
    }
    case 4: {
        N = 10000;
        M = 10;
        n = 5;
        m = 5;
        break;
    }
    case 5: {
        N = 10000;
        M = 10000;
        n = 5;
        m = 5;
        break;
    }
    default:
        N = 10;
        M = 10;
        n = 3;
        m = 3;
    }

	F = new int* [N + n - 1];
	for (int i = 0; i < N + n - 1; i++) {
		F[i] = new int[M + m - 1];
	}

	C = new int* [n];
	for (int i = 0; i < n; i++) {
		C[i] = new int[m];
	}

	res = new int* [N];
	for (int i = 0; i < N; i++) {
		res[i] = new int[M];
	}

	readFromFile();
	padMatrix();

	auto start = chrono::high_resolution_clock::now();
	
    //sequentialConvolution();
	//rowConvolution(p);
	//columnConvolution(p);
    blockConvolution(p);

	auto end = chrono::high_resolution_clock::now();
	auto duration = chrono::duration_cast<chrono::microseconds>(end - start).count();

    cout << (float) duration / 1000;

	//checkResult(matrixType);

	for (int i = 0; i < N + n - 1; i++) {
		delete[] F[i];
	}
	delete[] F;

	for (int i = 0; i < n; i++) {
		delete[] C[i];
	}
	delete[] C;

	for (int i = 0; i < N; i++) {
		delete[] res[i];
	}
	delete[] res;

	return 0;
}
