#include <iostream>
#include <string>
#include <fstream>
#include <random>
#include <mpi.h>

using namespace std;

void generateRandomWords(string outputPath, int nr, int minLength, int maxLength) {
	mt19937 mt_rand(time(0));

	ofstream fout(outputPath);

	fout << nr << "\n\n";

	for (int i = 0; i < nr; i++)
	{
		int length = mt_rand() % (maxLength - minLength + 1) + minLength;
		for (int j = 0; j < length; j++)
		{
			fout << (char)(mt_rand() % 26 + 'a');
		}
		fout << "\n";
	}
}

int main(int argc, char* argv[])
{
	string prefix = "C:/Users/Daniel/source/repos/MPI_Practic/";
	string inputPath1 = prefix + "words1.txt";
	string inputPath2 = prefix + "words2.txt";
	string outputPath = prefix + "output.txt";

	//generateRandomWords(inputPath2, 10000, 3, 15);

	MPI_Init(&argc, &argv);

	int rank, size;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	if (size < 2) {
		MPI_Abort(MPI_COMM_WORLD, 1);
		return 1;
	}

	int Y;

	if (rank == 0) {
		cout << "Y = ";
		cin >> Y;
	}

	MPI_Bcast(&Y, 1, MPI_INT, 0, MPI_COMM_WORLD);

	if (rank == 0) {
		ifstream fin(inputPath1);
		int n;
		fin >> n;

		int chunkSize = n / (size - 1);
		int rest = n % (size - 1);

		int start = 0;
		int end = chunkSize;

		for (int i = 1; i < size; i++)
		{
			if (rest > 0) {
				end++;
				rest--;
			}

			int wordsSent = end - start;

			MPI_Send(&wordsSent, 1, MPI_INT, i, 0, MPI_COMM_WORLD);

			for (int j = start; j < end; j++)
			{
				string word;
				fin >> word;

				int length = word.length();
				MPI_Send(&length, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
				MPI_Send(word.c_str(), length, MPI_CHAR, i, 0, MPI_COMM_WORLD);
			}

			start = end;
			end += chunkSize;
		}
		fin.close();

		int sum = 0;

		ofstream fout(outputPath);

		for (int i = 1; i < size; i++)
		{
			int wordsReceived;
			MPI_Recv(&wordsReceived, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			for (int j = 0; j < wordsReceived; j++)
			{
				int length;
				MPI_Recv(&length, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				char* buffer = new char[length + 1];
				MPI_Recv(buffer, length, MPI_CHAR, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				buffer[length] = '\0';

				string word = string(buffer);

				fout << word << "\n";

				delete[] buffer;
			}

			int partialSum;
			MPI_Recv(&partialSum, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			sum += partialSum;
		}
		fout.close();

		cout << "Sum = " << sum << "\n";
	}
	else {
		int wordsReceived;

		MPI_Recv(&wordsReceived, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		string* words = new string[wordsReceived];

		for (int i = 0; i < wordsReceived; i++)
		{
			int length;
			MPI_Recv(&length, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

			char* buffer = new char[length + 1];
			MPI_Recv(buffer, length, MPI_CHAR, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			buffer[length] = '\0';

			words[i] = string(buffer);

			delete[] buffer;
		}

		int sum = 0;
		for (int i = 0; i < wordsReceived; i++)
		{
			if (words[i].length() <= Y) {
				words[i].append("@");
			}
			else {
				words[i] = "*" + words[i];
			}

			sum += words[i].length();
		}

		MPI_Send(&wordsReceived, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);

		for (int i = 0; i < wordsReceived; i++)
		{
			int length = words[i].length();
			MPI_Send(&length, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
			MPI_Send(words[i].c_str(), length, MPI_CHAR, 0, 0, MPI_COMM_WORLD);
		}

		MPI_Send(&sum, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);

		delete[] words;
	}


	MPI_Finalize();

	return 0;
}
