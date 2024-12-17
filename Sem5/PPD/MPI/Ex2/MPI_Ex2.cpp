#include <iostream>
#include <fstream>
#include <mpi.h>
#include <string>

using namespace std;

void readNumbersFromFile(string path, int& n, int*& numbers) {
	ifstream fin(path);
	if (!fin.is_open()) {
		cout << "Error opening file" << endl;
		MPI_Abort(MPI_COMM_WORLD, 1);
		return;
	}

	fin >> n;
	numbers = new int[n];

	for (int i = 0; i < n; i++)
	{
		fin >> numbers[i];
	}

	fin.close();
}

int main(int argc, char* argv[])
{
	string pathPrefix = "C:/Users/Daniel/source/repos/MPI_Ex2/";
	string numbersPath = pathPrefix + "numbers.txt";
	string outputPath = pathPrefix + "output.txt";

	MPI_Init(&argc, &argv);

	int rank, size;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int n;
	int* numbers = nullptr;
	int* res = nullptr;
	int X;

	int chunkSize;

	if (rank == 0) {
		cout << "X = ";
		cin >> X;

		readNumbersFromFile(numbersPath, n, numbers);

		res = new int[n];
		chunkSize = n / (size - 1);
	}

	MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);

	int A = 0, B = 0;

	if (rank == 0) {
		int evenPos = 0;
		int oddPos = 1;

		int* sendNumbers = new int[chunkSize];

		for (int i = 1; i < size; i++)
		{
			if (i % 2 == 0) {
				for (int j = 0; j < chunkSize; j++) {
					sendNumbers[j] = numbers[evenPos];
					evenPos += 2;
				}
			}
			else {
				for (int j = 0; j < chunkSize; j++) {
					sendNumbers[j] = numbers[oddPos];
					oddPos += 2;
				}
			}
			MPI_Send(sendNumbers, chunkSize, MPI_INT, i, 0, MPI_COMM_WORLD);
		}

		int* receiveRes = new int[chunkSize];
		int auxA, auxB;

		evenPos = 0; oddPos = 1;

		for (int i = 1; i < size; i++) {
			MPI_Recv(receiveRes, chunkSize, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			MPI_Recv(&auxA, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			MPI_Recv(&auxB, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			A += auxA;
			B += auxB;

			if (i % 2 == 0) {
				for (int j = 0; j < chunkSize; j++) {
					res[evenPos] = receiveRes[j];
					evenPos += 2;
				}
			}
			else {
				for (int j = 0; j < chunkSize; j++) {
					res[oddPos] = receiveRes[j];
					oddPos += 2;
				}
			}
		}

		ofstream fout(outputPath);

		for (int i = 0; i < n; i++)
		{
			fout << res[i] << "\n";
		}

		fout.close();

		delete[] sendNumbers;
		delete[] receiveRes;
		delete[] res;
		delete[] numbers;

		cout << "A = " << A << endl;
		cout << "B = " << B << endl;
	}
	else {
		int* localNumbers = new int[chunkSize];
		int* localRes = new int[chunkSize];

		MPI_Recv(localNumbers, chunkSize, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		cout << "Rank " << rank << " received: ";
		for (int i = 0; i < chunkSize; i++)
		{
			cout << localNumbers[i] << " ";
		}

		// PROBABIL TREBUIA SUMA CIFRELOR

		int sum = 0;
		for (int i = 0; i < chunkSize; i++)
		{
			sum += localNumbers[i];
		}

		if (sum < X) {
			for (int i = 0; i < chunkSize; i++)
			{
				localRes[i] = localNumbers[i] * 2;
			}
			A++;
		}
		else {
			for (int i = 0; i < chunkSize; i++)
			{
				localRes[i] = localNumbers[i] / 2;
			}
			B++;
		}

		MPI_Send(localRes, chunkSize, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(&A, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(&B, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);

		delete[] localNumbers;
		delete[] localRes;
	}

	MPI_Finalize();
}