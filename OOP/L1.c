#include <stdio.h>

// 3.  Determina toate reprezentarile posibile a unui numar natural ca suma
//   de numere naturale consecutive.

// primeste un natural n si afiseaza posibilitatile de sume de numere consecutive care sunt egale cu n
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

int prim(int n){
    for (int i=2; i<=n/2; i++)
        if (n%i == 0)
            return 0;
    return 1;
}

void termeni(int n){
    // afiseaza primi n termeni din sirul cerut
    int i = 1;
    int elem = 1;
    int parti;
    while(i<=n){
        if (prim(elem) == 1)
            for (int j=elem; j >= 1; j--){
                if (i>n)
                    break;
                printf("%d, ", j);
                i++;
            }
        else{
            printf("%d, ", elem);
            i++;
            for (parti = 2; parti <= n/2; parti++)
                if (elem % parti == 0 && prim(parti) == 1)
                    for (int j=1; j<=parti; j++){
                        if (i>n)
                            break;
                        printf("%d, ", parti);
                        i++;
                    }
        }
        elem++;
    }
    printf("\n");
}

void run(){
    // meniul care permite instructiuni repetate
    int c;
    printf("3 ptr ex.3 sau 5 ptr ex.5\n");
    scanf("%d", &c);

    if (c ==3 || c == 5){
        printf("Introduceti un numar sau 0 pentru a finaliza\n");
        int n;
        while (1){
            printf("n=");
            scanf("%d", &n);
            if (n == 0)
                break;
            else{
                if (c == 3)
                    suma_de_numere(n);
                else if (c == 5)
                    termeni(n);
            }
        }
    }
}

int main() {
    run();
    return 0;
}
