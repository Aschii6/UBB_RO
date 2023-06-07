#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
	int nr;
	char str[101];
} param;

int m7=0, nm7=0;

pthread_mutex_t mtxM7 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mtxNm7 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mtxFile = PTHREAD_MUTEX_INITIALIZER;

void* f(void* arg){
	param* p = (param*)arg;
	int nr = p->nr;
	char* str = p->str;

	printf("In thread au ajuns: %d %s\n",nr,str);
	printf("Mai departe doamne ajuta moi descurca\n");
	free(p);



	return NULL;
}

int main(int argc, char* argv[]){
	if (argc < 2){
		printf("Argumente linia comanda insuficiente\n");
		return 0;
	}
	int n = atoi(argv[1]);
	int i;
	int n1;
	char st[101];
	pthread_t* threads = (pthread_t*)malloc(n*sizeof(pthread_t));

	for(i=0;i<n;i++){
		printf("Pentru threadul %d, introduceti un numar si un string\n",i);
		scanf("%d",&n1);
		scanf("%s",st);
		printf("Am citit: %d %s\n",n1,st);
		param* p = (param*)malloc(sizeof(param));
		p->nr = n1;
		strcpy(p->str,st);
		pthread_create(&threads[i],NULL,f,(void*)p);
	}

	for(i=0;i<n;i++)
		pthread_join(threads[i],NULL);	

	free(threads);
	return 0;
}
