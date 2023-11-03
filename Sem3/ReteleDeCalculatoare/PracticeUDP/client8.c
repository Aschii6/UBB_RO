#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
	int c;
	struct sockaddr_in server;
	uint16_t a, b, d = 0, m = 0;
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
	scanf("%hu", &a);

	printf("b = ");
	scanf("%hu", &b);

	a = htons(a);
	b = htons(b);

	sendto(c, &a, sizeof(a), 0, (struct sockaddr *) &server, sizeof(server));
	sendto(c, &b, sizeof(b), 0, (struct sockaddr *) &server, sizeof(server));

	recvfrom(c, &d, sizeof(d), MSG_WAITALL, (struct sockaddr *) &server, &l);
	recvfrom(c, &m, sizeof(m), MSG_WAITALL, (struct sockaddr *) &server, &l);

	a = ntohs(a);
	b = ntohs(b);

	d = ntohs(d);
	m = ntohs(m);

	if (d == 0 || m == 0){
		printf("Nu s-au primit datele bine\n");
	}
	else {
		printf("Ptr a = %hu si b = %hu am primit cmmdc = %hu si cmmmc = %hu\n", a, b, d, m);
	}

	close(c);
}
