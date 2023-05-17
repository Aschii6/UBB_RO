#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(){
	mkfifo("/tmp/A2B",0666);
	mkfifo("/tmp/B2A",0666);

	int A2B, B2A;
	char str[51];
	int len;

	A2B = open("/tmp/A2B",O_WRONLY);
	B2A = open("/tmp/B2A",O_RDONLY);

	while(1){
		printf("Dati str\n");
		scanf("%s",str);
		len=strlen(str);
		write(A2B,&len,sizeof(int));
		write(A2B,str,len+1);
		if(strcmp(str,"0")==0)
			break;
		//read(B2A,&len,sizeof(int));
		read(B2A,str,len+1);
		printf("A, primit de la B: %s\n",str);
	}

	close(A2B);
	close(B2A);
	return 0;
}
