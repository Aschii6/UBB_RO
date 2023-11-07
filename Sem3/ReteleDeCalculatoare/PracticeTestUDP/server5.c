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
	uint8_t a = 0, n = 0, v[100];

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

	recvfrom(s, &a, sizeof(a), MSG_WAITALL, (struct sockaddr *) &client, &l);

	for (uint8_t i = 1; i <= a; i++)
		if (a % i == 0)
			v[n++] = i;

	sendto(s, &n, sizeof(n), 0, (struct sockaddr *) &client, sizeof(client));

	sendto(s, v, n, 0, (struct sockaddr *) &client, sizeof(client));

	close(s);
}
