#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[]){
	int nr = atoi(argv[1]);
	char* str = argv[2];
	int tmp = 0;

	for (int i=0; i<strlen(str);i++)
		if(strchr("aeiouAEIOU",str[i])!=NULL)
			tmp++;
	if (tmp == nr)
		printf("%d %s\n",nr, str);
	return 0;
}
