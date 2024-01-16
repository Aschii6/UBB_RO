#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int s;
	struct sockaddr_in server, client;
	int l;
	uint16_t n = 0;
	char msg[100];

	s = socket(AF_INET, SOCK_DGRAM, 0);
	if (s < 0) {
		printf("Eroare la crearea socketului server\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;

	if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
		printf("Eroare la bind\n");
		return 1;
	}

	l = sizeof(client);
	memset(&client, 0, sizeof(client));

	recvfrom(s, &n, sizeof(n), MSG_WAITALL, (struct sockaddr *) &client, &l);

	n = ntohs(n);

	n = recvfrom(s, msg, n, MSG_WAITALL, (struct sockaddr *) &client, &l);

	msg[n] = '\0';

	printf("Primit: %s\n", msg);

	for (int i = 0 ; i < n / 2; i++){
		char aux = msg[i];
		msg[i] = msg[n - i - 1];
		msg[n - i - 1] = aux;
	}

	printf("Trimis: %s\n", msg);

	n = htons(n);

	sendto(s, &n, sizeof(n), 0, (struct sockaddr *) &client, sizeof(client));

	n = ntohs(n);

	sendto(s, msg, n, 0, (struct sockaddr *) &client, sizeof(client));

	close(s);
}

