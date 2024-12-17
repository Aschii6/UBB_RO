#include <mpi.h>
#include <random>
#include <iostream>
#include <fstream>
#include <string>
#include <chrono>
#include "LargeNumber.h"

using namespace std;

void writeRandomDigitsToFile(string filepath, int n) {
	ofstream f(filepath);

	random_device rd;
	mt19937 gen(rd());
	uniform_int_distribution<> dis(0, 9);

	f << n << endl;

	for (int i = 0; i < n; i++) {
		f << dis(gen) << " ";
	}

	f.close();
}

LargeNumber readDigitsFromFile(string filepath) {
	ifstream f(filepath);
	int n;
	if (!f.is_open()) {
		cout << "Error opening file " << filepath << endl;
		exit(1);
	}
	f >> n;

	LargeNumber ln;
	ln.size = n;
	ln.digits = new short[n];

	for (int i = 0; i < n; i++) {
		f >> ln.digits[n - i - 1];
	}

	f.close();
	return ln;
}

void writeResultToFile(string filepath, LargeNumber ln) {
	ofstream f(filepath);

	f << ln.size << endl;

	for (int i = 0; i < ln.size; i++) {
		f << static_cast<int>(ln.digits[ln.size - i - 1]) << " ";
	}
}

void compareResultWithSequential(int testCase, LargeNumber ln) {
	string filepath = "C:/Users/Daniel/source/repos/PPD_T3/C" + to_string(testCase) + "_Res_Seq.txt";

	ifstream f(filepath);

	int n;
	f >> n;

	if (n != ln.size) {
		throw exception("Sizes do not match");
	}

	for (int i = 0; i < n; i++) {
		int digit;
		f >> digit;
		if (digit != ln.digits[n - i - 1]) {
			throw exception("Digits do not match");
		}
	}

	f.close();
}

LargeNumber addLargeNumbersSequential(LargeNumber ln1, LargeNumber ln2) {
	LargeNumber result;
	result.size = max(ln1.size, ln2.size) + 1;
	result.digits = new short[result.size];

	int carry = 0;
	int i;
	for (i = 0; i < result.size; i++) {
		int sum = carry;
		if (i < ln1.size) {
			sum += ln1.digits[i];
		}
		if (i < ln2.size) {
			sum += ln2.digits[i];
		}

		result.digits[i] = sum % 10;
		carry = sum / 10;
	}

	if (result.digits[result.size - 1] == 0) {
		result.size--;
	}

	return result;
}

int main(int argc, char** argv) {
	LargeNumber ln1, ln2;

	int testCase = 3;

	string pathPrefix = "C:/Users/Daniel/source/repos/PPD_T3/";
	string ln1File = pathPrefix + "C" + to_string(testCase) + "_NR1.txt";
	string ln2File = pathPrefix + "C" + to_string(testCase) + "_NR2.txt";

	auto start = chrono::high_resolution_clock::now();

	ln1 = readDigitsFromFile(ln1File);
	ln2 = readDigitsFromFile(ln2File);

	LargeNumber ln3 = addLargeNumbersSequential(ln1, ln2);

	auto end = chrono::high_resolution_clock::now();
	auto duration = chrono::duration_cast<chrono::microseconds>(end - start).count();

	cout << (float)duration / 1000;

	string resultFile = pathPrefix + "C" + to_string(testCase) + "_Res.txt";
	writeResultToFile(resultFile, ln3);

	compareResultWithSequential(testCase, ln3);

	return 0;
}