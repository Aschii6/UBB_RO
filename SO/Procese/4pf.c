#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>

/*
Write a program that creates a child process. The two communicate through a pipe. The parent reads a string 
with >25 characters and sends it to the child, which removes 1 vowel and sends it to the parent, 
which removes the first and the last character and sends it to the child back which removes again a vowel 
and sends it back .... and so on untill the string contains 3 or less characters.
*/

int main(){
	char str[51];
	printf("Dati string\n");
	scanf("%s",str);
	int len = strlen(str);

	int p2c[2],c2p[2];
	if (pipe(p2c)==-1){perror("Err pipe\n"); exit(1);}
	if (pipe(c2p)==-1){perror("Err pipe\n"); exit(1);}

	int pid = fork();
	if (pid == -1){perror("Err fork\n"); exit(1);}

	if (pid == 0){
		// In child
		close(p2c[1]);close(c2p[0]);
		while (1){
			read(p2c[0],&len,sizeof(int));
			read(p2c[0],str,len+1);
		        if (strlen(str)<3)
				break;	
			for (int i = 0; i < strlen(str); i++){
				if(strchr("aeiouAEIOU",str[i])!=NULL){
					memmove(&str[i],&str[i+1],strlen(str)-i);
					break;
				}
			}
			printf("Copil, sirul a devenit %s\n",str);
			len = strlen(str);
			write(c2p[1],&len,sizeof(int));
			write(c2p[1],str,strlen(str)+1);
			sleep(1);
		}
		close(p2c[0]);close(c2p[1]);
		exit(0);
	}
	// In parent
	close (p2c[0]);close(c2p[1]);

	write(p2c[1],&len,sizeof(int));
	write(p2c[1],str,strlen(str)+1);

	while(1){
		read(c2p[0],&len,sizeof(int));
		read(c2p[0],str,len+1);
		if (strlen(str)<3)
			break;
		memmove(&str[0],&str[1],strlen(str));
		str[strlen(str)-1]='\0';

		printf("Parinte, sirul a devenit %s\n",str);
		len = strlen(str);
		write(p2c[1],&len,sizeof(int));
		write(p2c[1],str,strlen(str)+1);
		sleep(1);
	}

	wait(0);
	close(p2c[1]);close(c2p[0]);
	return 0;
}
