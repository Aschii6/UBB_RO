#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	uint8_t a, n = 0, v[100];
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

	printf("a = ");
	scanf("%hhd", &a);

	sendto(c, &a, sizeof(a), 0, (struct sockaddr *) &server, sizeof(server));

	recvfrom(c, &n, sizeof(n), MSG_WAITALL, (struct sockaddr *) &server, &l);

	recvfrom(c, v, n, MSG_WAITALL, (struct sockaddr *) &server, &l);

	for (uint8_t i = 0; i < n; i++)
		printf("%hhd ", v[i]);
	printf("\n");

	close(c);
}
