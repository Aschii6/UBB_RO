import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        int N, M;
        int n = 3;

        int p = 4;
        int matrixType = 1;

        if (args.length >= 2) {
            p = Integer.parseInt(args[0]);
            matrixType = Integer.parseInt(args[1]);
        }

        switch (matrixType) {
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

        int[][] F = new int[N + n - 1][M];
        int[][] C = new int[n][n];

        readDataFromFile(F, C, N, M, n);

        long startTime = System.currentTimeMillis();

//        sequentialConvolution(F, C, N, M, n);
        rowConvolution(F, C, N, M, n, p);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);

        writeOutputToFile(F, N, M);
//        checkResult(F, N, M, matrixType);
    }

    private static void sequentialConvolution(int[][] F, int[][] C, int N, int M, int n) {
        int[] previousRow = new int[M];
        int[] currentRow = new int[M];
        int[] nextRow = new int[M];

        int[] resRow = new int[M];

        System.arraycopy(F[0], 0, previousRow, 0, M);
        System.arraycopy(F[0], 0, currentRow, 0, M);
        System.arraycopy(F[1], 0, nextRow, 0, M);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                resRow[j] = C[0][0] * previousRow[Math.max(j - 1, 0)] +
                        C[0][1] * previousRow[j] +
                        C[0][2] * previousRow[Math.min(j + 1, M - 1)] +
                        C[1][0] * currentRow[Math.max(j - 1, 0)] +
                        C[1][1] * currentRow[j] +
                        C[1][2] * currentRow[Math.min(j + 1, M - 1)] +
                        C[2][0] * nextRow[Math.max(j - 1, 0)] +
                        C[2][1] * nextRow[j] +
                        C[2][2] * nextRow[Math.min(j + 1, M - 1)];
            }

            System.arraycopy(currentRow, 0, previousRow, 0, M);
            System.arraycopy(nextRow, 0, currentRow, 0, M);
            System.arraycopy(F[Math.min(i + 2, N - 1)], 0, nextRow, 0, M);

            System.arraycopy(resRow, 0, F[i], 0, M);
        }
    }

    private static void rowConvolution(int[][] F, int[][] C, int N, int M, int n, int p) {
        CyclicBarrier barrier = new CyclicBarrier(p);

        int rows = N / p;
        int rest = N % p;

        RowThread[] threads = new RowThread[p];
        int startRow = 0;
        int endRow = rows;

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                endRow++;
                rest--;
            }

            threads[i] = new RowThread(F, C, N, M, n, startRow, endRow, barrier);
            threads[i].start();

            startRow = endRow;
            endRow += rows;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private static class RowThread extends Thread {
        private int[][] F, C;
        private int N, M, n;
        private int startRow, endRow;
        private CyclicBarrier barrier;

        public RowThread(int[][] F, int[][] C, int N, int M, int n, int startRow, int endRow, CyclicBarrier barrier) {
            this.F = F;
            this.C = C;
            this.N = N;
            this.M = M;
            this.n = n;
            this.startRow = startRow;
            this.endRow = endRow;
            this.barrier = barrier;
        }

        public void run() {
            int[] rowBeforeStart = new int[M];
            int[] rowAfterEnd = new int[M];

            System.arraycopy(F[Math.max(startRow - 1, 0)], 0, rowBeforeStart, 0, M);
            System.arraycopy(F[Math.min(endRow, N - 1)], 0, rowAfterEnd, 0, M);

            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("Error in barrier: " + e.getMessage());
                return;
            }

            int[] previousRow = new int[M];
            System.arraycopy(rowBeforeStart, 0, previousRow, 0, M);

            int[] resRow = new int[M];

            for (int i = startRow; i < endRow; i++) {
                if (i == endRow - 1) {
                    for (int j = 0; j < M; j++) {
                        resRow[j] = C[0][0] * previousRow[Math.max(j - 1, 0)] +
                                C[0][1] * previousRow[j] +
                                C[0][2] * previousRow[Math.min(j + 1, M - 1)] +
                                C[1][0] * F[i][Math.max(j - 1, 0)] +
                                C[1][1] * F[i][j] +
                                C[1][2] * F[i][Math.min(j + 1, M - 1)] +
                                C[2][0] * rowAfterEnd[Math.max(j - 1, 0)] +
                                C[2][1] * rowAfterEnd[j] +
                                C[2][2] * rowAfterEnd[Math.min(j + 1, M - 1)];
                    }
                } else {
                    for (int j = 0; j < M; j++) {
                        resRow[j] = C[0][0] * previousRow[Math.max(j - 1, 0)] +
                                C[0][1] * previousRow[j] +
                                C[0][2] * previousRow[Math.min(j + 1, M - 1)] +
                                C[1][0] * F[i][Math.max(j - 1, 0)] +
                                C[1][1] * F[i][j] +
                                C[1][2] * F[i][Math.min(j + 1, M - 1)] +
                                C[2][0] * F[i + 1][Math.max(j - 1, 0)] +
                                C[2][1] * F[i + 1][j] +
                                C[2][2] * F[i + 1][Math.min(j + 1, M - 1)];
                    }
                }

                System.arraycopy(F[i], 0, previousRow, 0, M);
                System.arraycopy(resRow, 0, F[i], 0, M);
            }
        }
    }

    private static void readDataFromFile(int[][] F, int[][] C, int N, int M, int n) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Daniel\\IdeaProjects\\PPD_T2\\src\\dataF.txt"));

            for (int i = 0; i < N; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < M; j++) {
                    F[i][j] = Integer.parseInt(line[j]);
                }
            }

            reader = new BufferedReader(new FileReader("C:\\Users\\Daniel\\IdeaProjects\\PPD_T2\\src\\dataC.txt"));

            for (int i = 0; i < n; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < n; j++) {
                    C[i][j] = Integer.parseInt(line[j]);
                }
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void writeOutputToFile(int[][] res, int N, int M) {
        try {
            FileWriter writer = new FileWriter("C:\\Users\\Daniel\\IdeaProjects\\PPD_T2\\src\\output.txt");

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    writer.write(res[i][j] + " ");
                }
                writer.write("\n");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void checkResult(int[][] res, int N, int M, int matrixType) {
        String filePath = "C:\\Users\\Daniel\\IdeaProjects\\PPD_T2\\src\\outputSeq" + matrixType + ".txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            for (int i = 0; i < N; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < M; j++) {
                    if (res[i][j] != Integer.parseInt(line[j])) {
                        throw new Exception("Wrong result");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Wrong result: " + e.getMessage());
        }
    }
}