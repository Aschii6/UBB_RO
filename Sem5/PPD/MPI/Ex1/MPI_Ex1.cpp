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
    string pathPrefix = "C:/Users/Daniel/source/repos/MPI_Ex1/";
    string numbersFile = pathPrefix + "numbers.txt";
	string outputFile = pathPrefix + "output.txt";

	int rank, size;

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int n;
	int* numbers = nullptr;
	int* res = nullptr;
	int X;

	int chunkSize;

	if (rank == 0) {
		cout << "X= ";
		cin >> X;

		// read from file
		readNumbersFromFile(numbersFile, n, numbers);

		res = new int[n];

		chunkSize = n / size;
	}

	// common operations
	MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);

	// local vars for each process
	int* localNumbers = new int[chunkSize];
	int* localRes = new int[chunkSize];
	int A = 0, B = 0;

	MPI_Scatter(numbers, chunkSize, MPI_INT, localNumbers, chunkSize, MPI_INT, 0, MPI_COMM_WORLD);

	for (int i = 0; i < chunkSize; i++)
	{
		int aux = localNumbers[i];
		int sum = 0;

		while (aux > 0) {
			sum += aux % 10;
			aux /= 10;
		}

		if (sum < X) {
			localRes[i] = localNumbers[i] * 2;
			A++;
		}
		else {
			localRes[i] = localNumbers[i] / 2;
			B++;
		}
	}

	MPI_Gather(localRes, chunkSize, MPI_INT, res, chunkSize, MPI_INT, 0, MPI_COMM_WORLD);

	if (rank == 0) {
		// print output
		ofstream fout(outputFile);

		for (int i = 0; i < n; i++)
		{
			fout << res[i] << "\n";
		}

		fout.close();
	}

	if (rank == 1) {
		for (int i = 0; i < size; i++) {
			if (i == 1)
				continue;

			int auxA, auxB;

			MPI_Recv(&auxA, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			MPI_Recv(&auxB, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			A += auxA;
			B += auxB;
		}

		cout << "A= " << A << ", B= " << B << '\n';
	}
	else {
		MPI_Send(&A, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
		MPI_Send(&B, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
	}

	MPI_Finalize();

	return 0;
}
