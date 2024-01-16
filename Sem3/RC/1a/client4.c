#include<sys/types.h>
#include<sys/socket.h>
#include<stdio.h>
#include<netinet/in.h>
#include<netinet/ip.h>
#include<string.h>

int main(){
	int c;
	struct sockaddr_in server;
	uint16_t n1, n2, nRez;
        char v1[100], v2[100], vRez[200];

	c = socket(AF_INET, SOCK_STREAM, 0);

	if (c < 0) {
		printf("Eroare la crearea socketului client\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");

	if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
		printf("Eroare la conectarea la server\n");
		return 1;
	}

	printf("n1 = ");
	scanf("%hu", &n1);
	
	while (getchar() != '\n');

	printf("n2 = ");
	scanf("%hu", &n2);

	while (getchar() != '\n');

	nRez = n1 + n2;

	printf("Dati primul sir de caractere: ");
	for (uint16_t i = 0; i < n1; i++) {
		v1[i] = getchar();
		while (getchar() != '\n');
	}

	printf("Dati al doilea sir de caracter: ");
	for (uint16_t i = 0; i < n2; i++) {
		v2[i] = getchar();
		while (getchar() != '\n');
	}

	printf("Date trimise: n1 = %d n2 = %d\n", n1, n2);
	
	for (uint16_t i = 0; i < n1; i++)
		printf("%c ", v1[i]);
	printf("\n");

	for (uint16_t i = 0; i < n2; i++)
		printf("%c ", v2[i]);
	printf("\n");

	n1 = htons(n1);
	n2 = htons(n2);

	send(c, &n1, sizeof(n1), 0);

	n1 = ntohs(n1);
	send(c, v1, sizeof(char)*n1, 0);

	send(c, &n2, sizeof(n2), 0);

	n2 = ntohs(n2);	
	send(c, v2, sizeof(char)*n2, 0);

	recv(c, vRez, sizeof(char)*nRez, 0);

	printf("Sirul interclasat este: \n");
	for (uint16_t i = 0; i < nRez; i++){
		printf("%c", vRez[i]);
	}
	printf("\n");

	close(c);
	return 0;
}
