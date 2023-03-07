#include <stdio.h>

// 3.  Determina toate reprezentarile posibile a unui numar natural ca suma
//   de numere naturale consecutive.

int main() {
    int n;
    printf("n=");
    scanf("%d", &n);

    printf("%d\n", n);

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

    return 0;
}
