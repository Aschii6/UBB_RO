#include <stdio.h>

// 3.  Determina toate reprezentarile posibile a unui numar natural ca suma
//   de numere naturale consecutive.

void suma_de_numere(int n){
    for (int i=1; i <= n; i++){
        int sum = i;
        for (int j=i+1; j<=n; j++){
            sum += j;
            if (sum == n){
                for (int k=i; k<j; k++)
                    printf("%d + ", k);
                printf("%d\n", j);
            }
            if (sum > n)
                break;
        }
    }
}

void run(){
    printf("Introduceti un numar sau 0 pentru a finaliza\n");
    int n;
    while (1){
        printf("n=");
        scanf("%d", &n);
        if (n == 0)
            break;
        else{
            suma_de_numere(n);
        }
    }
}

int main() {
    run();
    return 0;
}
