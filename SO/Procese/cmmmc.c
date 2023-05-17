#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[]){
	FILE* fd;
	char* numeFis = argv[1];
	int a = atoi(argv[2]), b = atoi(argv[3]);
	int ca = a, cb = b;
	while (ca != cb)
		if (ca > cb)
			ca -= cb;
		else
			cb -= ca;
	int cmmmc = a* (b/cb);
	
	fd = fopen(numeFis, "w");
	fprintf(fd,"%d",cmmmc);
	fclose(fd);

	return 0;
}
