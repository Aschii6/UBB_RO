#include <iostream>
#include <fstream>
#include <string>

using namespace std;

void generateMatrix(int N, string filename) {
	ofstream fout(filename);

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			fout << rand() % 10 << " ";
		}
		fout << endl;
	}
}

//int main() {
//	int N = 1000;
//
//	string prefix = R"(C:\Users\Daniel\source\repos\PPD_CUDA\)";
//
//	generateMatrix(N, prefix + "A.txt");
//	generateMatrix(N, prefix + "B.txt");
//
//	return 0;
//}