#include <iostream>
#include <fstream>
#include <mpi.h>
#include <string>

using namespace std;

void readFromFile(string inputPath, int& nrCartiere, int& nrTemperaturi, int*& temperaturi) {
	ifstream fin(inputPath);

	if (!fin.is_open()) {
		cerr << "Could not open file " << inputPath << endl;
		MPI_Abort(MPI_COMM_WORLD, 1);
		return;
	}

	fin >> nrCartiere >> nrTemperaturi;

	temperaturi = new int[nrCartiere * nrTemperaturi];

	int id;

	for (int i = 0; i < nrCartiere; i++) {
		for (int j = 0; j < nrTemperaturi; j++) {
			fin >> id;
			fin >> temperaturi[i * nrTemperaturi + j];
		}
	}

	fin.close();
}

int main(int argc, char* argv[])
{
	string pathPrefix = "C:/Users/Daniel/source/repos/MPI_Ex3/";
	string tempsPath = pathPrefix + "temps.txt";
	string outputPath = pathPrefix + "output.txt";

	int rank, size;

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int nrCartiere, nrTemperaturi;
	int* temperaturi = nullptr;

	int chunkSize, rest;

	if (rank == 0) {
		readFromFile(tempsPath, nrCartiere, nrTemperaturi, temperaturi);
	}

	MPI_Bcast(&nrCartiere, 1, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Bcast(&nrTemperaturi, 1, MPI_INT, 0, MPI_COMM_WORLD);

	chunkSize = nrCartiere / size;
	rest = nrCartiere % size;

	int* sendCounts = new int[size];
	int* displs = new int[size];
	int offset = 0;

	for (int i = 0; i < size; i++) {
		if (i < rest) {
			sendCounts[i] = (chunkSize + 1) * nrTemperaturi;
		}
		else {
			sendCounts[i] = chunkSize * nrTemperaturi;
		}

		displs[i] = offset;
		offset += sendCounts[i];
	}
	
	int recvSize = sendCounts[rank];
	int* recvBuffer = new int[recvSize];

	MPI_Scatterv(temperaturi, sendCounts, displs, MPI_INT, recvBuffer, recvSize, MPI_INT, 0, MPI_COMM_WORLD);

	int nrCartiereLocal = recvSize / nrTemperaturi;

	float* mediiLocal = new float[nrCartiereLocal];

	for (int i = 0; i < nrCartiereLocal; i++) {
		mediiLocal[i] = 0;

		for (int j = 0; j < nrTemperaturi; j++) {
			mediiLocal[i] += recvBuffer[i * nrTemperaturi + j];
		}

		mediiLocal[i] /= nrTemperaturi;
	}

	float* medii = nullptr;

	if (rank == 0) {
		medii = new float[nrCartiere];
	}

	int* recvCounts = new int[size];
	int* recvDispls = new int[size];
	int recvOffset = 0;

	for (int i = 0; i < size; i++) {
		if (i < rest) {
			recvCounts[i] = (chunkSize + 1);
		}
		else {
			recvCounts[i] = chunkSize;
		}

		recvDispls[i] = recvOffset;
		recvOffset += recvCounts[i];
	}

	MPI_Gatherv(mediiLocal, nrCartiereLocal, MPI_FLOAT, medii, recvCounts, recvDispls, MPI_FLOAT, 0, MPI_COMM_WORLD);

	if (rank == 0) {
		ofstream fout(outputPath);

		for (int i = 0; i < nrCartiere; i++) {
			fout << i + 1 << " " << medii[i] << '\n';
		}

		fout.close();

		delete[] medii;
		delete[] temperaturi;
	}

	delete[] sendCounts;
	delete[] displs;
	delete[] recvBuffer;
	delete[] mediiLocal;

	MPI_Finalize();
}
