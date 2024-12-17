#include <mpi.h>
#include <random>
#include <iostream>
#include <fstream>
#include <string>
#include <chrono>

using namespace std;

void writeResultToFile(string filepath, int size, short* digits) {
	ofstream f(filepath);

	f << size << endl;

	for (int i = 0; i < size; i++) {
		f << static_cast<int>(digits[size - i - 1]) << " ";
	}
}

void compareResultWithSequential(int testCase, int size, short* digits) {
	string filepath = "C:/Users/Daniel/source/repos/PPD_T3/C" + to_string(testCase) + "_Res_Seq.txt";

	ifstream f(filepath);

	int n;
	f >> n;

	if (n != size) {
		throw exception("Sizes do not match");
	}

	for (int i = 0; i < n; i++) {
		int digit;
		f >> digit;
		if (digit != digits[n - i - 1]) {
			throw exception("Digits do not match");
		}
	}

	f.close();
}

void readDigitsFromFile(string filepath, int& n, short*& digits) {
	ifstream f(filepath);

	if (!f.is_open()) {
		cout << "Error opening file " << filepath << endl;
		exit(1);
	}

	f >> n;

	digits = new short[n];

	for (int i = 0; i < n; i++) {
		f >> digits[n - i - 1];
	}

	f.close();
}

void extendDigits(short*& digits, int size, int newSize) {
	short* aux = digits;
	digits = new short[newSize];

	for (int i = 0; i < size; i++) {
		digits[i] = aux[i];
	}

	for (int i = size; i < newSize; i++) {
		digits[i] = 0;
	}

	if (aux != nullptr)
		delete[] aux;
}

void addLargeNumbersMpi(string ln1File, string ln2File, string resFile, int numProcs, int testCase) {
	if (numProcs < 2) {
		return;
	}

	int rank;

	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	int n1, n2, n3;
	short* digits1 = nullptr;
	short* digits2 = nullptr;
	short* result = nullptr;

	int chunkSize = 0;

	if (rank == 0) {
		readDigitsFromFile(ln1File, n1, digits1);
		readDigitsFromFile(ln2File, n2, digits2);

		int size = max(n1, n2);

		// so they can be scattered evenly
		if (size % numProcs != 0) {
			size += numProcs - size % numProcs;
		}

		chunkSize = size / numProcs;

		extendDigits(digits1, n1, size);
		extendDigits(digits2, n2, size);

		n1 = size;
		n2 = size;
		n3 = size;

		result = new short[size]();
	}
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);

	short* localDigits1 = new short[chunkSize];
	short* localDigits2 = new short[chunkSize];
	short* localResult = new short[chunkSize];

	MPI_Scatter(digits1, chunkSize, MPI_SHORT, localDigits1, chunkSize, MPI_SHORT, 0, MPI_COMM_WORLD);
	MPI_Scatter(digits2, chunkSize, MPI_SHORT, localDigits2, chunkSize, MPI_SHORT, 0, MPI_COMM_WORLD);

	int carry = 0;

	if (rank > 0) {
		MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
	}

	for (int i = 0; i < chunkSize; i++) {
		int sum = localDigits1[i] + localDigits2[i] + carry;
		localResult[i] = sum % 10;
		carry = sum / 10;
	}

	if (rank < numProcs - 1) {
		MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
	}

	MPI_Gather(localResult, chunkSize, MPI_SHORT, result, chunkSize, MPI_SHORT, 0, MPI_COMM_WORLD);

	if (rank == numProcs - 1) {
		MPI_Send(&carry, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
	}

	if (rank == 0) {
		// remove any leading zeros
		while (n3 > 1 && result[n3 - 1] == 0) {
			n3--;
		}

		int lastCarry = 0;

		MPI_Recv(&lastCarry, 1, MPI_INT, numProcs - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		if (lastCarry > 0) {
			n3++;
			extendDigits(result, n3 - 1, n3);
			result[n3 - 1] = lastCarry;
		}

		writeResultToFile(resFile, n3, result);

		compareResultWithSequential(testCase, n3, result);
	}
}

int main(int argc, char** argv) {
	int testCase = 2;

	if (argc > 1) {
		testCase = atoi(argv[1]);
	}

	string pathPrefix = "C:/Users/Daniel/source/repos/PPD_T3/";
	string ln1File = pathPrefix + "C" + to_string(testCase) + "_NR1.txt";
	string ln2File = pathPrefix + "C" + to_string(testCase) + "_NR2.txt";
	string resFile = pathPrefix + "C" + to_string(testCase) + "_Res.txt";

	int rank;
	int numProcs = 1;

	auto start = chrono::high_resolution_clock::now();

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

	addLargeNumbersMpi(ln1File, ln2File, resFile, numProcs, testCase);

	MPI_Finalize();

	auto end = chrono::high_resolution_clock::now();
	auto duration = chrono::duration_cast<chrono::microseconds>(end - start).count();

	if (rank == 0) {
		cout << (float)duration / 1000;

		/*ofstream g(pathPrefix + "time.txt");
		g << (float)duration / 1000;
		g.close();*/
	}

	return 0;
}