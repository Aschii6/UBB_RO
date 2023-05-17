#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main(){
	if(fork()==0){
		execl("cmp2num.sh","cmp2num.sh",8,8,NULL);
		exit(2); // error
	}
	int status;
	WIFEXITED(status);
	printf("%d",status);
	if (status == 0)
		printf("Toate egale");
	return 0;
}
