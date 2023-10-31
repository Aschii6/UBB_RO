#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;

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

	char buffer[200];
	char character;
	uint16_t n = 0, size = 0, v[200];

	printf("Dati un sir de char: ");

	fgets(buffer, 200, stdin);

	printf("Am citit: %s", buffer);

	n = strlen(buffer);
	
	n = htons(n);

	send(c, &n, sizeof(n), 0);

	n = ntohs(n);

	send(c, buffer, n, 0);

	recv(c, &character, sizeof(character), MSG_WAITALL);

	recv(c, &size, sizeof(size), MSG_WAITALL);

	size = ntohs(size);

	recv(c, v, sizeof(uint16_t)*size, MSG_WAITALL);

	printf("Caracterul cel mai des intalnit: %c\n", character);

	printf("Apare pe pozitiile: ");

	for (uint16_t i = 0; i < size; i++) {
		v[i] = ntohs(v[i]);
		printf("%hu ", v[i]);
	}

	printf("\n");

	close(c);
}
