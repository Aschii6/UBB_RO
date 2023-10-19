#include <stdlib.h>
#include <stdio.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

/*
Process creates child and grandchild. Isi trimit back and forth un numar modificand din el si se opresc cand e mai mic decat 5
*/

int main(int argc, char* argv[]){
	int c2g[2],g2c[2];
	pipe(c2g);
	pipe(g2c);

	// citesc nr
	int n = 56;
	srand(time(NULL));

	if (fork()==0){
		// In child
		write(c2g[1],&n,sizeof(int));
		if (fork()==0){
			// In grandchild
			while (n >= 5){
				read(c2g[0],&n,sizeof(int));
				if (n%3 == 0)
					n = n/3;
				printf("G:n=%d\n",n);
				write(g2c[1],&n,sizeof(int));
			}
			close(c2g[0]);close(c2g[1]);
			close(g2c[0]);close(g2c[1]);
			exit(0);
		}
		// In child
		while (n >= 5){
			read(g2c[0],&n,sizeof(int));
			if (n < 5)
				break;
			int c = rand()%3;
			n += c;
			printf("C:n=%d\n",n);
			write(c2g[1],&n,sizeof(int));
		}
		close(c2g[0]);close(c2g[1]);
		close(g2c[0]);close(g2c[1]);
		wait(0);
		exit(0);
	}
	// In parent
	close(c2g[0]);close(c2g[1]);
	close(g2c[0]);close(g2c[1]);
	wait(0);

	return 0;
}
