#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

/*
Program primeste ca argumente perechi de numere si stringuri, le trimite spre un alt program cu exec, daca nr de vocale ale stringului = nr dat atunci il afiseaza
*/

int main(int argc, char* argv[]){
	if(argc < 3 || argc%2==0){
		perror("Bad arguments\n");
		exit(1);
	}
	for(int i=1;i<argc;i+=2){
		int pid = fork();
		if (pid == -1){
			perror("Fork error\n");
			exit(1);
		}
		if (pid == 0){
			execl("./str_num","str_num",argv[i],argv[i+1],NULL);
			exit(1);
		}
		wait(0);
	}
	return 0;
}
