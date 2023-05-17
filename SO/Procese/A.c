#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

/*
Write two independent programs A and B that communicate using fifos. Program A reads words from keyboard and send them to process B, 
receiving back the word in uppercase letters and a number representing the number of letters of the word. 
Program B received from A a word, computes the corresponsing word with uppercase letters and number of letters and sends these to to program A.
This continues in a loop, untill program A sends word "000" and receives back the same word and number 0 and terminates.
So does program B, when received "000", sends to A "000" and number 0 and terminates.
*/

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
