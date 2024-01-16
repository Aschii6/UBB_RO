#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	int l = sizeof(server);

	c = socket(AF_INET, SOCK_DGRAM, 0);
	if (c < 0) {
		printf("Eroare la crearea socketului client\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");

	char buffer[200];
	char character;
	uint16_t n = 0, size = 0, v[200];

	printf("Dati un sir de char: ");

	fgets(buffer, 200, stdin);

	printf("Am citit: %s", buffer);

	n = strlen(buffer);

	n = htons(n);

	sendto(c, &n, sizeof(n), 0, (struct sockaddr *) &server, sizeof(server));

	n = ntohs(n);

	sendto(c, buffer, n, 0, (struct sockaddr *) &server, sizeof(server));

	recvfrom(c, &character, sizeof(character), MSG_WAITALL, (struct sockaddr *) &server, &l);

	recvfrom(c, &size, sizeof(size), MSG_WAITALL, (struct sockaddr *) &server, &l);

	size = ntohs(size);

	recvfrom(c, v, sizeof(uint16_t)*size, MSG_WAITALL, (struct sockaddr *) &server, &l);

	printf("Caracterul cel mai des intalnit: %c\n", character);

	printf("Apare pe pozitiile: ");

	for (uint16_t i = 0; i < size; i++) {
		v[i] = ntohs(v[i]);
		printf("%hu ", v[i]);
	}

	printf("\n");

	close(c);
}
