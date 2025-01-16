#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <chrono>
#include <stdexcept>

__global__ void matrixMultiplicationKernel(int* A, int* B, int* C, int N)
{
	int row = blockIdx.y * blockDim.y + threadIdx.y;
	int col = blockIdx.x * blockDim.x + threadIdx.x;

	if (row < N && col < N)
	{
		int sum = 0;
		for (int i = 0; i < N; i++)
		{
			sum += A[row * N + i] * B[i * N + col];
		}
		C[row * N + col] = sum;
	}
}

void matrixMultiplication(int* A, int* B, int* C, int N)
{
	int* d_A, * d_B, * d_C;
	int size = N * N * sizeof(int);

	cudaMalloc(&d_A, size);
	cudaMemcpy(d_A, A, size, cudaMemcpyHostToDevice);

	cudaMalloc(&d_B, size);
	cudaMemcpy(d_B, B, size, cudaMemcpyHostToDevice);

	cudaMalloc(&d_C, size);

	dim3 threadsPerBlock(16, 16);
	dim3 blocksPerGrid((N + 15) / 16, (N + 15) / 16);

	matrixMultiplicationKernel << <blocksPerGrid, threadsPerBlock >> > (d_A, d_B, d_C, N);

	cudaMemcpy(C, d_C, size, cudaMemcpyDeviceToHost);

	cudaFree(d_A);
	cudaFree(d_B);
	cudaFree(d_C);
}

void readFromFile(int* A, int* B, int N) {
	std::string line;

	std::ifstream fin(R"(C:\Users\Daniel\source\repos\PPD_CUDA\A.txt)");
	for (int i = 0; i < N; i++) {
		getline(fin, line);
		std::istringstream iss(line);

		for (int j = 0; j < N; j++) {
			iss >> A[i * N + j];
		}
	}
	fin.close();

	std::ifstream fin2(R"(C:\Users\Daniel\source\repos\PPD_CUDA\B.txt)");
	for (int i = 0; i < N; i++) {
		getline(fin2, line);
		std::istringstream iss(line);

		for (int j = 0; j < N; j++) {
			iss >> B[i * N + j];
		}
	}
	fin2.close();
}

void writeResToFile(int* C, int N) {
	std::ofstream fout(R"(C:\Users\Daniel\source\repos\PPD_CUDA\Res.txt)");
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			fout << C[i * N + j] << " ";
		}
		fout << std::endl;
	}
	fout.close();
}

int main()
{
	int N = 1000;

	int* A = new int[N * N];
	int* B = new int[N * N];
	int* C = new int[N * N];

	readFromFile(A, B, N);

	auto start = std::chrono::high_resolution_clock::now();

	matrixMultiplication(A, B, C, N);

	auto end = std::chrono::high_resolution_clock::now();
	auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();

	std::cout << (float)duration / 1000;

	writeResToFile(C, N);

	delete[] A;
	delete[] B;
	delete[] C;

	cudaError_t cudaStatus = cudaDeviceReset();
	if (cudaStatus != cudaSuccess) {
		fprintf(stderr, "cudaDeviceReset failed!");
		return 1;
	}

	return 0;
}