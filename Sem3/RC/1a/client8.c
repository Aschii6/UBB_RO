#include<sys/types.h>
#include<sys/socket.h>
#include<stdio.h>
#include<netinet/in.h>
#include<netinet/ip.h>
#include<string.h>

int main(){
	int c;
	struct sockaddr_in server;
	uint16_t n1, n2, nRez = 0;
        uint16_t v1[100], v2[100], vRez[100];

	c = socket(AF_INET, SOCK_STREAM, 0);

	if (c < 0) {
		printf("Eroare la crearea socketului client\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1235);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");

	if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
		printf("Eroare la conectarea la server\n");
		return 1;
	}

	printf("n1 = ");
	scanf("%hu", &n1);

	printf("n2 = ");
	scanf("%hu", &n2);

	printf("Dati primul sir de nr: ");
	for (uint16_t i = 0; i < n1; i++) {
		scanf("%hu", &v1[i]);
	}

	printf("Dati al doilea sir de nr: ");
	for (uint16_t i = 0; i < n2; i++) {
		scanf("%hu", &v2[i]);
	}

	printf("Date trimise: n1 = %hu n2 = %hu\n", n1, n2);

	for (uint16_t i = 0; i < n1; i++)
		printf("%hu ", v1[i]);
	printf("\n");

	for (uint16_t i = 0; i < n2; i++)
		printf("%hu ", v2[i]);
	printf("\n");

	for (uint16_t i = 0; i < n1; i++)
		v1[i] = htons(v1[i]);

	for (uint16_t i = 0; i < n2; i++)
		v2[i] = htons(v2[i]);

	n1 = htons(n1);
	n2 = htons(n2);

	send(c, &n1, sizeof(n1), 0);

	n1 = ntohs(n1);
	send(c, v1, sizeof(uint16_t)*n1, 0);

	send(c, &n2, sizeof(n2), 0);

	n2 = ntohs(n2);
	send(c, v2, sizeof(uint16_t)*n2, 0);

	recv(c, &nRez, sizeof(nRez), MSG_WAITALL);
	nRez = ntohs(nRez);

	recv(c, vRez, sizeof(uint16_t)*nRez, MSG_WAITALL);

	printf("Sirul nr comune este: \n");
	for (uint16_t i = 0; i < nRez; i++){
		vRez[i] = ntohs(vRez[i]);
		printf("%hu", vRez[i]);
	}
	printf("\n");

	close(c);
	return 0;
}
