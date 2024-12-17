import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        int N, M;
        int n, m;

        int p = 4;
        int matrixType = 1; // pentru a alege dimensiunea matricilor

        if (args.length >= 2) {
            p = Integer.parseInt(args[0]);
            matrixType = Integer.parseInt(args[1]);
        }

        switch (matrixType) {
            case 1:
                N = 10;
                M = 10;
                n = 3;
                m = 3;
                break;
            case 2:
                N = 1000;
                M = 1000;
                n = 5;
                m = 5;
                break;
            case 3:
                N = 10;
                M = 10000;
                n = 5;
                m = 5;
                break;
            case 4:
                N = 10000;
                M = 10;
                n = 5;
                m = 5;
                break;
            case 5:
                N = 10000;
                M = 10000;
                n = 5;
                m = 5;
                break;
            default:
                N = 10;
                M = 10;
                n = 3;
                m = 3;
                break;
        }

        int[][] F = new int[N + n - 1][M + m - 1];
        int[][] C = new int[n][m];
        int[][] res = new int[N][M];

        readDataFromFile(F, C, N, M, n, m);
        padMatrix(F, N, M, n, m);

        long startTime = System.currentTimeMillis();

//        sequentialConvolution(F, C, res, N, M, n, m);
//        rowConvolution(F, C, res, N, M, n, m, p);
//        columnConvolution(F, C, res, N, M, n, m, p);
        blockConvolution(F, C, res, N, M, n, m, p);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);

        checkResult(res, N, M, matrixType);
    }

    // Abordare secventiala
    private static void sequentialConvolution(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m) {
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        for (int i = nPadSize; i < N + nPadSize; i++) {
            for (int j = mPadSize; j < M + mPadSize; j++) {
                int sum = 0;
                for (int k = -nPadSize; k <= nPadSize; k++) {
                    for (int l = -mPadSize; l <= mPadSize; l++) {
                        sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                    }
                }
                res[i - nPadSize][j - mPadSize] = sum;
            }
        }
    }

    /*
     * Abordare paralela cu impartire pe linii
     * Daca nr de linii nu se impart exact la nr de threaduri,
     * atunci primele r threaduri vor face o linie in plus, r fiind restul impartirii
     */
    private static void rowConvolution(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int p) {
        // Dimensiunea bordata a matricei F
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        int rows = N / p;
        int rest = N % p;

        RowThread[] threads = new RowThread[p];
        int startRow = nPadSize;
        int endRow = nPadSize + rows;

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                endRow++;
                rest--;
            }

            RowThread thread = new RowThread(F, C, res, N, M, n, m, startRow, endRow);
            threads[i] = thread;
            thread.start();
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
        private int[][] F, C, res;
        private int N, M, n, m;
        private int startRow, endRow;

        // Primeste linia de la care incepe si randul la care se opreste
        public RowThread(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int startRow, int endRow) {
            this.F = F;
            this.C = C;
            this.res = res;
            this.N = N;
            this.M = M;
            this.n = n;
            this.m = m;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        public void run() {
            int nPadSize = (n - 1) / 2;
            int mPadSize = (m - 1) / 2;

            for (int i = startRow; i < endRow; i++) {
                for (int j = mPadSize; j < M + mPadSize; j++) {
                    int sum = 0;
                    for (int k = -nPadSize; k <= nPadSize; k++) {
                        for (int l = -mPadSize; l <= mPadSize; l++) {
                            sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                        }
                    }
                    res[i - nPadSize][j - mPadSize] = sum;
                }
            }
        }
    }

    /*
     * Abordare paralela cu impartire pe coloane
     * Daca nr de coloane nu se imparte exact la nr de threaduri,
     * atunci primele r threaduri vor face o coloana in plus, r fiind restul impartirii
     */
    private static void columnConvolution(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int p) {
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        int columns = M / p;
        int rest = M % p;

        ColumnThread[] threads = new ColumnThread[p];
        int startColumn = mPadSize;
        int endColumn = mPadSize + columns;

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                endColumn++;
                rest--;
            }

            ColumnThread thread = new ColumnThread(F, C, res, N, M, n, m, startColumn, endColumn);
            threads[i] = thread;
            thread.start();
            startColumn = endColumn;
            endColumn += columns;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    // Primeste coloana de la care incepe si coloana la care se opreste
    private static class ColumnThread extends Thread {
        private int[][] F, C, res;
        private int N, M, n, m;
        private int startColumn, endColumn;

        public ColumnThread(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int startColumn, int endColumn) {
            this.F = F;
            this.C = C;
            this.res = res;
            this.N = N;
            this.M = M;
            this.n = n;
            this.m = m;
            this.startColumn = startColumn;
            this.endColumn = endColumn;
        }

        public void run() {
            int nPadSize = (n - 1) / 2;
            int mPadSize = (m - 1) / 2;

            for (int j = startColumn; j < endColumn; j++) {
                for (int i = nPadSize; i < N + nPadSize; i++) {
                    int sum = 0;
                    for (int k = -nPadSize; k <= nPadSize; k++) {
                        for (int l = -mPadSize; l <= mPadSize; l++) {
                            sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                        }
                    }
                    res[i - nPadSize][j - mPadSize] = sum;
                }
            }
        }
    }

    /*
     * Abordare paralela cu impartire pe blocuri
     * Se imparte matricea in p blocuri de dimensiuni egale, folosind cei mai apropiati divizori ai
     * lui p care inmultiti dau p
     * Astfel blocurile impartite vor avea dimensiuni echilibrate
     */
    private static void blockConvolution(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int p) {
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        int p1 = 1, p2 = p;

        for (int i = (int) Math.sqrt(p); i >= 1; i--) {
            if (p % i == 0) {
                p1 = i;
                p2 = p / i;
                break;
            }
        }

        int rows = N / p1;
        int restRows = N % p1;
        int columns = M / p2;
        int restColumns = M % p2;

        BlockThread[] threads = new BlockThread[p];

        int startRow = nPadSize;
        int endRow = nPadSize + rows;
        int startColumn = mPadSize;
        int endColumn = mPadSize + columns;

        for (int i = 0; i < p1; i++) {
            if (restRows > 0) {
                endRow++;
                restRows--;
            }
            for (int j = 0; j < p2; j++) {
                if (restColumns > 0) {
                    endColumn++;
                    restColumns--;
                }

                BlockThread thread = new BlockThread(F, C, res, N, M, n, m, startRow, endRow, startColumn, endColumn);
                threads[i * p2 + j] = thread;
                thread.start();
                startColumn = endColumn;
                endColumn += columns;
            }
            startRow = endRow;
            endRow += rows;

            startColumn = mPadSize;
            endColumn = mPadSize + columns;
            restColumns = M % p2;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    // Primeste un colt de la care incepe si un colt la care se opreste
    private static class BlockThread extends Thread {
        private int[][] F, C, res;
        private int N, M, n, m;
        private int startRow, endRow, startColumn, endColumn;

        public BlockThread(int[][] F, int[][] C, int[][] res, int N, int M, int n, int m, int startRow, int endRow, int startColumn, int endColumn) {
            this.F = F;
            this.C = C;
            this.res = res;
            this.N = N;
            this.M = M;
            this.n = n;
            this.m = m;
            this.startRow = startRow;
            this.endRow = endRow;
            this.startColumn = startColumn;
            this.endColumn = endColumn;
        }

        public void run() {
            int nPadSize = (n - 1) / 2;
            int mPadSize = (m - 1) / 2;

            for (int i = startRow; i < endRow; i++) {
                for (int j = startColumn; j < endColumn; j++) {
                    int sum = 0;
                    for (int k = -nPadSize; k <= nPadSize; k++) {
                        for (int l = -mPadSize; l <= mPadSize; l++) {
                            sum += F[i + k][j + l] * C[k + nPadSize][l + mPadSize];
                        }
                    }
                    res[i - nPadSize][j - mPadSize] = sum;
                }
            }
        }
    }

    // Citeste matricile F si C din fisiere, pregatind spatiu in matricea F pentru bordare
    private static void readDataFromFile(int[][] F, int[][] C, int N, int M, int n, int m) {
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Daniel\\IdeaProjects\\PPD_T1\\src\\dataF.txt"));
            for (int i = 0; i < N; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < M; j++) {
                    F[i + nPadSize][j + mPadSize] = Integer.parseInt(line[j]);
                }
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Daniel\\IdeaProjects\\PPD_T1\\src\\dataC.txt"));
            for (int i = 0; i < n; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < m; j++) {
                    C[i][j] = Integer.parseInt(line[j]);
                }
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Bordeaza matricea F
    private static void padMatrix(int[][] F, int N, int M, int n, int m) {
        int nPadSize = (n - 1) / 2;
        int mPadSize = (m - 1) / 2;

        for (int i = nPadSize; i < N + nPadSize; i++) {
            for (int j = mPadSize - 1; j >= 0; j--) {
                F[i][j] = F[i][j + 1];
            }
            for (int j = mPadSize; j < mPadSize * 2; j++) {
                F[i][M + j] = F[i][M + j - 1];
            }
        }

        for (int j = 0; j < M + mPadSize * 2; j++) {
            for (int i = nPadSize - 1; i >= 0; i--) {
                F[i][j] = F[i + 1][j];
            }
            for (int i = nPadSize; i < nPadSize * 2; i++) {
                F[N + i][j] = F[N + i - 1][j];
            }
        }
    }

    // Verifica daca rezultatul este identic cu un rezultat secvential salvat in fisier
    private static void checkResult(int[][] res, int N, int M, int matrixType) {
        String fileName = "C:\\Users\\Daniel\\IdeaProjects\\PPD_T1\\src\\seqRes" + matrixType + ".txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            for (int i = 0; i < N; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < M; j++) {
                    if (res[i][j] != Integer.parseInt(line[j])) {
                        throw new RuntimeException("The result is incorrect");
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException("The result is incorrect");
        }
    }
}
