#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	uint16_t n, suma, v[100];

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

	printf("n = ");
	scanf("%hu", &n);

	for (uint16_t i = 0; i < n; i++){
		printf("e = ");
		scanf("%hu", &v[i]);
		v[i] = htons(v[i]);
	}

	n = htons(n);

	send(c, &n, sizeof(n), 0);

	n = ntohs(n);

	send(c, v, sizeof(uint16_t)*n, 0);

	recv(c, &suma, sizeof(suma), 0);

	suma = ntohs(suma);

	printf("Suma primita este: %hu\n", suma);

	close(c);
}
