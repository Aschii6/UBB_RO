#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	uint16_t n;
	char msg[100];
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

	printf("String: ");

	fgets(msg, 100, stdin);

	n = strlen(msg) - 1;

	msg[n] = '\0';

	n = htons(n);

	sendto(c, &n, sizeof(n), 0, (struct sockaddr *) &server, sizeof(server));

	n = ntohs(n);

	sendto(c, msg, n, 0, (struct sockaddr *) &server, sizeof(server));

	recvfrom(c, &n, sizeof(n), MSG_WAITALL, (struct sockaddr *) &server, &l);

	n = ntohs(n);

	uint16_t read = 0;
	read = recvfrom(c, msg, n, MSG_WAITALL, (struct sockaddr *) &server, &l);

	msg[read] = '\0';

	printf("Primit: %s\n", msg);

	close(c);
}
