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
	uint16_t a = 0, b = 0, d = 0, m = 0;

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
	recvfrom(s, &b, sizeof(b), MSG_WAITALL, (struct sockaddr *) &client, &l);

	a = ntohs(a);
	b = ntohs(b);

	uint16_t altA = a, altB = b;

	if (a != 0 && b != 0){

		while (a != b){
			if (a > b)
				a -= b;
			else
				b -= a;
		}

		d = a;
		m = altA * (altB / d);

	}

	d = htons(d);
	m = htons(m);

	sendto(s, &d, sizeof(d), 0, (struct sockaddr *) &client, sizeof(client));
	sendto(s, &m, sizeof(m), 0, (struct sockaddr *) &client, sizeof(client));

	close(s);
}

