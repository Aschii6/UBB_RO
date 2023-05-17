#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(){
	int A2B, B2A;
	char str[51];
	int len;

	A2B = open("/tmp/A2B",O_RDONLY);
	B2A = open("/tmp/B2A",O_WRONLY);

	while(1){
		read(A2B,&len,sizeof(int));
		read(A2B,str,len+1);
		if(strcmp(str,"0")==0)
			break;
		for(int i = 0; i < strlen(str); i++)
			if (str[i] >= 'a' && str[i] <= 'z')
				str[i] -= 32;
		//write(B2A,&len,sizeof(int));
		write(B2A,str,len+1);
	}
	close(A2B);
	close(B2A);
	return 0;
}
