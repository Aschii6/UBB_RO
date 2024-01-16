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
	server.sin_port = htons(3456);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("172.30.116.123");

	if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
		printf("Eroare la conectarea la server\n");
		return 1;
	}

	char buffer[200];

	int read = recv(c, buffer, sizeof(buffer), 0);

	buffer[read] = '\0';

	printf("%s", buffer);

	char* name = "DanielLechea\n";

	send(c, name, strlen(name), 0);

	read = recv(c, buffer, sizeof(buffer), 0);

	buffer[read] = '\0';

	printf("%s", buffer);

	char* problemNumber = "12\n";

	send(c, problemNumber, strlen(problemNumber), 0);

	read = recv(c, buffer, sizeof(buffer), 0);

	buffer[read] = '\0';

	printf("%s", buffer);

	close(c);
}
