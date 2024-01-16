#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	uint16_t i, len, n1, n2;
	char v[100], rez[100];
	int l = sizeof(server);

	rez[0] = '\0';

	c = socket(AF_INET, SOCK_DGRAM, 0);
	if (c < 0) {
		printf("Eroare la crearea socketului client\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1234);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");

	printf("Dati sir: ");
	fgets(v, 100, stdin);

	n1 = strlen(v);

	//v[n1] = '\0';
	
	printf("i = ");
	scanf("%hu", &i);

	printf("l = ");
	scanf("%hu", &len);

	n1 = htons(n1);
	sendto(c, &n1, sizeof(n1), 0, (struct sockaddr *) &server, sizeof(server));
	n2 = ntohs(n1);

	sendto(c, v, n1, 0, (struct sockaddr *) &server, sizeof(server));

	i = htons(i);
	sendto(c, &i, sizeof(i), 0, (struct sockaddr *) &server, sizeof(server));

	len = htons(len);
	sendto(c, &len, sizeof(len), 0, (struct sockaddr *) &server, sizeof(server));

	recvfrom(c, &n2, sizeof(n2), MSG_WAITALL, (struct sockaddr *) &server, &l);
	n2 = ntohs(n2);

	recvfrom(c, rez, n2, MSG_WAITALL, (struct sockaddr *) &server, &l);

	printf("Sir primit: %s\n", rez);

	close(c);
}
