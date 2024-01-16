#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;

	char buffer[100], rez[100];

	int n = 0;

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

	printf("Dati un sir: ");

	fgets(buffer, 100, stdin);

	//printf("Am citit: %s", buffer);
	
	n = strlen(buffer);
	n = htons(n);

	send(c, &n, sizeof(n), 0);

	n = ntohs(n);

	send(c, buffer, n, 0);

	int read = recv(c, rez, n, MSG_WAITALL);

	strcpy(rez, rez + 1);

	rez[n] = '\0';

	printf("Sirul primit: %s\n", rez);

	close(c);
}
