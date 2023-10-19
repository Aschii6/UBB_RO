#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <unistd.h>

sem_t sem;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

int money[15];

void* f(void* arg){
	sem_wait(&sem);

	int nr = (int)arg;
	int j;
	printf("Thread %d: am inceput executia\n",nr);

	for (j=0;j<3;j++){
		int pers=nr;
		while (pers == nr)
			pers = rand()%15;
		pthread_mutex_lock(&mtx);

		int suma = money[nr]/2;
		printf("Thread %d trimit spre %d: %d bani\n",nr,pers,suma);
		money[nr]-=suma;
		money[pers]+=suma;

		pthread_mutex_unlock(&mtx);

		sleep(rand()%3);
	}

	sem_post(&sem);
	return NULL;
}

int main(){
	pthread_t th[15];	
	sem_init(&sem,0,10);
	srand(time(NULL));
	int i;
	for (i=0;i<15;i++){
		money[i]=100;
		pthread_create(&th[i],NULL,f,(void*)i);
	}

	for (i=0;i<15;i++)
		pthread_join(th[i],NULL);

	printf("Am ajuns la final. Acestea sunt rezultatele in urma jocului:\n");
	
	for (i=0;i<15;i++)
		printf("%d ",money[i]);

	sem_destroy(&sem);
	return 0;
}
