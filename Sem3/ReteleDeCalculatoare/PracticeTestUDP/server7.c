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
	uint16_t i = 0, len = 0, n1 = 0, n2 = 0;
	char v[100], rez[100];
	v[0] = '\0';
	rez[0] = '\0';

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

	recvfrom(s, &n1, sizeof(n1), MSG_WAITALL, (struct sockaddr *) &client, &l);
	n1 = ntohs(n1);

	recvfrom(s, v, n1, MSG_WAITALL, (struct sockaddr *) &client, &l);

	recvfrom(s, &i, sizeof(i), MSG_WAITALL, (struct sockaddr *) &client, &l);
	i = ntohs(i);

	recvfrom(s, &len, sizeof(len), MSG_WAITALL, (struct sockaddr *) &client, &l);
	len = htons(len);

	if (i + len <= n1)
		strncpy(rez, v + i, len);

	printf("i = %hu l = %hu rez: %s\n", i, len, rez);

	n2 = strlen(rez);

	n2 = htons(n2);
	sendto(s, &n2, sizeof(n2), 0, (struct sockaddr *) &client, sizeof(client));
	n2 = ntohs(n2);

	sendto(s, rez, n2, 0, (struct sockaddr *) &client, sizeof(client));

	close(s);
}
