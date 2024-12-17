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

void addLargeNumbersMpi(string ln1File, string ln2File, string resFile, int numProcs, int testCase) {
	if (numProcs < 2) {
		return;
	}

	int rank;

	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		//
		int n1, n2;
		short* digits1;
		short* digits2;

		readDigitsFromFile(ln1File, n1, digits1);
		readDigitsFromFile(ln2File, n2, digits2);

		int size = max(n1, n2);

		int chunkSize = size / (numProcs - 1);
		int rest = size % (numProcs - 1);

		int start = 0;
		int end = chunkSize;

		for (int i = 1; i < numProcs; i++) {
			if (rest > 0) {
				end++;
				rest--;
			}

			int sendSize = end - start;

			short* sendDigits1 = new short[sendSize];
			short* sendDigits2 = new short[sendSize];

			for (int j = 0; j < sendSize; j++) {
				if (start + j < n1) {
					sendDigits1[j] = digits1[start + j];
				}
				else {
					sendDigits1[j] = 0;
				}

				if (start + j < n2) {
					sendDigits2[j] = digits2[start + j];
				}
				else {
					sendDigits2[j] = 0;
				}
			}

			MPI_Send(&sendSize, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(sendDigits1, sendSize, MPI_SHORT, i, 0, MPI_COMM_WORLD);

			MPI_Send(&sendSize, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(sendDigits2, sendSize, MPI_SHORT, i, 0, MPI_COMM_WORLD);

			start = end;
			end += chunkSize;

			if (sendDigits1 != nullptr)
				delete[] sendDigits1;
			if (sendDigits2 != nullptr)
				delete[] sendDigits2;
		}
		//

		int readSize;
		int totalSize = 0;

		int proccesId = 1;

		short** digits = new short* [numProcs];
		int* sizes = new int[numProcs];

		while (proccesId < numProcs) {
			MPI_Recv(&readSize, 1, MPI_INT, proccesId, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			sizes[proccesId - 1] = readSize;
			totalSize += readSize;

			digits[proccesId - 1] = new short[readSize];

			MPI_Recv(digits[proccesId - 1], readSize, MPI_SHORT, proccesId, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			proccesId++;
		}

		short* resultDigits = new short[totalSize];

		int index = 0;
		for (int i = 0; i < numProcs - 1; i++) {
			for (int j = 0; j < sizes[i]; j++) {
				resultDigits[index] = digits[i][j];
				index++;
			}
		}

		string filepath = "C:/Users/Daniel/source/repos/PPD_T3/C" + to_string(testCase) + "_Res.txt";
		writeResultToFile(filepath, totalSize, resultDigits);

		compareResultWithSequential(testCase, totalSize, resultDigits);

		for (int i = 0; i < numProcs - 1; i++) {
			if (digits[i] != nullptr)
				delete[] digits[i];
		}
		if (digits != nullptr)
			delete[] digits;

		if (sizes != nullptr)
			delete[] sizes;

		if (resultDigits != nullptr)
			delete[] resultDigits;
	}
	else {
		int chunkSize;
		int carry = 0;

		MPI_Recv(&chunkSize, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		short* digits1 = new short[chunkSize];

		MPI_Recv(digits1, chunkSize, MPI_SHORT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		MPI_Recv(&chunkSize, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		short* digits2 = new short[chunkSize];

		MPI_Recv(digits2, chunkSize, MPI_SHORT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		int resultSize = chunkSize;
		short* resultDigits = new short[resultSize];

		if (rank > 1) {
			MPI_Recv(&carry, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}

		for (int i = 0; i < resultSize; i++) {
			int sum = carry;
			if (i < chunkSize) {
				sum += digits1[i];
			}
			if (i < chunkSize) {
				sum += digits2[i];
			}

			resultDigits[i] = sum % 10;
			carry = sum / 10;
		}

		if (rank < numProcs - 1) {
			MPI_Send(&carry, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
		}
		else {
			if (carry > 0) {
				resultSize++;

				short* aux = resultDigits;
				resultDigits = new short[resultSize];

				for (int i = 0; i < resultSize - 1; i++) {
					resultDigits[i] = aux[i];
				}
				resultDigits[resultSize - 1] = carry;

				if (aux != nullptr)
					delete[] aux;
			}
		}

		MPI_Send(&resultSize, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);

		MPI_Send(resultDigits, resultSize, MPI_SHORT, 0, 0, MPI_COMM_WORLD);

		if (digits1 != nullptr)
			delete[] digits1;

		if (digits2 != nullptr)
			delete[] digits2;
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

		ofstream g(pathPrefix + "time.txt");
		g << (float)duration / 1000;
	}

	return 0;
}