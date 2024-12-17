#include <iostream>
#include <fstream>
#include <sstream>
#include <chrono>
#include <thread>
#include <mutex>

using namespace std;

class my_barrier {
public:
	my_barrier(int count) : thread_count(count), counter(0), waiting(0) {} 
	
	void wait() {
		std::unique_lock<std::mutex> lk(m);
		++counter;
		++waiting;
		cv.wait(lk, [&] {return counter >= thread_count; });
		cv.notify_one();
		--waiting;
		if (waiting == 0) {
			counter = 0;
		}
		lk.unlock();
	}
private:
	std::mutex m;
	std::condition_variable cv;
	int counter;
	int waiting;
	int thread_count;
};

void readFromFile(int** F, int** C, int N, int M, int n) {
	string line;

	ifstream fin(R"(C:\Users\Daniel\source\repos\PPD_T2\dataF.txt)");

	for (int i = 0; i < N; i++) {
		getline(fin, line);
		istringstream iss(line);

		for (int j = 0; j < M; j++) {
			iss >> F[i][j];
		}
	}

	fin.close();

	ifstream fin2(R"(C:\Users\Daniel\source\repos\PPD_T2\dataC.txt)");

	for (int i = 0; i < n; i++) {
		getline(fin2, line);
		istringstream iss(line);

		for (int j = 0; j < n; j++) {
			iss >> C[i][j];
		}
	}

	fin2.close();
}

void writeOutputToFile(int** res, int N, int M) {
	ofstream fout(R"(C:\Users\Daniel\source\repos\PPD_T2\output.txt)");

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			fout << res[i][j] << " ";
		}
		fout << endl;
	}

	fout.close();
}

void checkResult(int** res, int N, int M, int matrixType) {
	string fileName = R"(C:\Users\Daniel\source\repos\PPD_T2\outputSeq)" + to_string(matrixType) + ".txt";
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

void sequentialConvolution(int** F, int** C, int N, int M, int n) {
	int* previousRow = new int[M];
	int* currentRow = new int[M];
	int* nextRow = new int[M];

	int* resRow = new int[M];

	memcpy(previousRow, F[0], M * sizeof(int));
	memcpy(currentRow, F[0], M * sizeof(int));
	memcpy(nextRow, F[1], M * sizeof(int));

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			resRow[j] = C[0][0] * previousRow[max(j - 1, 0)] +
				C[0][1] * previousRow[j] +
				C[0][2] * previousRow[min(j + 1, M - 1)] +
				C[1][0] * currentRow[max(j - 1, 0)] +
				C[1][1] * currentRow[j] +
				C[1][2] * currentRow[min(j + 1, M - 1)] +
				C[2][0] * nextRow[max(j - 1, 0)] +
				C[2][1] * nextRow[j] +
				C[2][2] * nextRow[min(j + 1, M - 1)];
		}

		memcpy(previousRow, currentRow, M * sizeof(int));
		memcpy(currentRow, nextRow, M * sizeof(int));
		memcpy(nextRow, F[min(i + 2, N - 1)], M * sizeof(int));

		memcpy(F[i], resRow, M * sizeof(int));
	}

	delete[] previousRow;
	delete[] currentRow;
	delete[] nextRow;
	delete[] resRow;
}

int rowThreadFunction(int** F, int** C, int N, int M, int n, int startRow, int endRow, my_barrier& barrier) {
	int* rowBeforeStart = new int[M];
	int* rowAfterEnd = new int[M];

	memcpy(rowBeforeStart, F[max(startRow - 1, 0)], M * sizeof(int));
	memcpy(rowAfterEnd, F[min(endRow, N - 1)], M * sizeof(int));

	barrier.wait();

	int* previousRow = new int[M];
	memcpy(previousRow, rowBeforeStart, M * sizeof(int));

	int* resRow = new int[M];

	for (int i = startRow; i < endRow; i++) {
		int* currentRow = F[i];
		int* nextRow = i == endRow - 1 ? rowAfterEnd : F[i + 1];

		for (int j = 0; j < M; j++) {
			resRow[j] = C[0][0] * previousRow[max(j - 1, 0)] +
				C[0][1] * previousRow[j] +
				C[0][2] * previousRow[min(j + 1, M - 1)] +
				C[1][0] * currentRow[max(j - 1, 0)] +
				C[1][1] * currentRow[j] +
				C[1][2] * currentRow[min(j + 1, M - 1)] +
				C[2][0] * nextRow[max(j - 1, 0)] +
				C[2][1] * nextRow[j] +
				C[2][2] * nextRow[min(j + 1, M - 1)];
		}

		memcpy(previousRow, currentRow, M * sizeof(int));

		memcpy(F[i], resRow, M * sizeof(int));
	}

	delete[] rowBeforeStart;
	delete[] rowAfterEnd;
	delete[] previousRow;
	delete[] resRow;
}

void rowConvolution(int** F, int** C, int N, int M, int n, int p, my_barrier& barrier) {
	int rows = N / p;
	int rest = N % p;

	int startRow = 0;
	int endRow = rows;

	thread* threads = new thread[p];

	for (int i = 0; i < p; i++) {
		if (rest > 0) {
			endRow++;
			rest--;
		}

		threads[i] = thread(rowThreadFunction, F, C, N, M, n, startRow, endRow, ref(barrier));

		startRow = endRow;
		endRow += rows;
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}

	delete[] threads;
}

int main(int argc, char* argv[])
{
	int** F;
	int** C;

	int N, M;
	int n = 3;

	int p = 2;
	int matrixType = 1;

	my_barrier barrier(p);

	if (argc >= 2) {
		p = atoi(argv[1]);
		matrixType = atoi(argv[2]);
	}

	switch (matrixType)
	{
	case 1:
		N = 10;
		M = 10;
		break;
	case 2:
		N = 1000;
		M = 1000;
		break;
	case 3:
		N = 10000;
		M = 10000;
		break;
	default:
		N = 10;
		M = 10;
	}

	F = new int* [N];
	for (int i = 0; i < N; i++) {
		F[i] = new int[M];
	}

	C = new int* [n];
	for (int i = 0; i < n; i++) {
		C[i] = new int[n];
	}

	readFromFile(F, C, N, M, n);

	auto start = chrono::high_resolution_clock::now();

	//sequentialConvolution(F, C, N, M, n);
	rowConvolution(F, C, N, M, n, p, barrier);

	auto end = chrono::high_resolution_clock::now();
	auto duration = chrono::duration_cast<chrono::microseconds>(end - start).count();

	cout << (float)duration / 1000;

	//writeOutputToFile(F, N, M);

	//checkResult(F, N, M, matrixType);

	for (int i = 0; i < N; i++) {
		delete[] F[i];
	}
	delete[] F;

	for (int i = 0; i < n; i++) {
		delete[] C[i];
	}
	delete[] C;

	return 0;
}
