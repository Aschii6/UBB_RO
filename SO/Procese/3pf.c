#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <stdio.h>
#include <fcntl.h>

/*
Implement a program that creates 2 child processes A and B, which communicate using FIFO.
Process A sends to process B a number 70>n>10; process B receives this number, subtracts 4 and sends it to process A;
process A reads the number from B, decreases the number by 1 and sends it to B... and so on, until the number n has 1 digit.
*/

int main(int argc, char* argv[]){
	mkfifo ("/tmp/fifoa2b", 0666);
	mkfifo ("/tmp/fifob2a", 0666);

	int fa2b, fb2a;

	int n;
	printf("Introduceti un numar: ");
	scanf("%d",&n);

	int pa = fork();
	if (pa == 0){
		fa2b = open("/tmp/fifoa2b",O_WRONLY);
		write(fa2b,&n,sizeof(int));
		fb2a = open("/tmp/fifob2a",O_RDONLY);

		while (1){
			read(fb2a,&n,sizeof(int));
			if (n <= 9)
				break;
			n--;
			write(fa2b,&n,sizeof(int));
			printf("Proces A, am trimis spre B %d\n",n);
		}

		close(fa2b);
		close(fb2a);
		exit(0);
	}
	else{
		int pb = fork();
		if (pb == 0){
			fa2b = open("/tmp/fifoa2b",O_RDONLY);
			fb2a = open("/tmp/fifob2a",O_WRONLY);
			while (1){
				read(fa2b,&n,sizeof(int));
				if (n <= 9)
					break;
				n = n-4;
				write(fb2a,&n,sizeof(int));
				printf("Proces B, am trimis spre A %d\n",n);
			}
			close(fa2b);
			close(fb2a);
			exit(0);
		}
	}
	for(int i=0; i<2; i++)
		wait(0);
	return 0;
}
