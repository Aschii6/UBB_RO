#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main(){
	int nr[51];
	int tmp;
	int ind = 0;
	char s1[11], s2[11];

	printf("Introduceti numere sau 0 ptr a incheia: ");
	scanf("%d",&tmp);
	while (tmp != 0){
		nr[ind] = tmp;
		ind++;
		scanf("%d",&tmp);
	}
	if (ind < 2)
		return 1;

	if (fork()==0){
		sprintf(s1,"%d",nr[0]);
		sprintf(s2,"%d",nr[1]);
		execl("./cmmmc","cmmmc","xd.txt",s1,s2,NULL);
		exit(0);
	}
	int status;
	wait (&status);
	if(WEXITSTATUS(status) != 0){
		printf("Not good\n");
		return WEXITSTATUS(status);
	}
	int n;
	for (int i = 2; i < ind; i++){
		if (fork()==0){
			FILE* fd = fopen("xd.txt","r");
			fscanf(fd,"%d",&n);
			fclose(fd);
			sprintf(s1,"%d",n);
			sprintf(s2,"%d",nr[i]);
			execl("./cmmmc","cmmmc","xd.txt",s1,s2,NULL);
			exit(0);
		}
		wait (&status);
		if(WEXITSTATUS(status) != 0){
			printf("Not good\n");
			return WEXITSTATUS(status);
		}
	}
	FILE* fd = fopen("xd.txt","r");
	fscanf(fd,"%d",&n);
	printf("Cmmmc al tuturor nr este: %d\n",n);
	fclose(fd);
	return 0;
}
