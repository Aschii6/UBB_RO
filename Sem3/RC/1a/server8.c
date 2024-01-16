#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

void deservireClient(int c) {
    uint16_t n1 = 0, n2 = 0, nRez = 0;
    uint16_t v1[100], v2[100], vRez[100];

    recv(c, &n1, sizeof(n1), MSG_WAITALL);
    n1 = ntohs(n1);

    recv(c, v1, sizeof(uint16_t)*n1, MSG_WAITALL);

    recv(c, &n2, sizeof(n2), MSG_WAITALL);
    n2 = ntohs(n2);

    recv(c, v2, sizeof(uint16_t)*n2, MSG_WAITALL);

    printf("n1 = %hu\nn2 = %hu\n", n1, n2);
    printf("Liste primite: ");
    for (uint16_t i = 0; i < n1; i++) {
	    v1[i] = ntohs(v1[i]);
	    printf("%hu ", v1[i]);
    }
    printf("\n");

    for (uint16_t i = 0; i < n2; i++) {
	    v2[i] = ntohs(v2[i]);
	    printf("%hu ", v2[i]);
    }
    printf("\n");

    for (uint16_t i = 0; i < n1; i++) {
	    int ok = 0;
    	for (uint16_t j = 0; j < n2; j++) {
		if (v1[i] == v2[j])
			ok = 1;
	}
	if (ok == 1) {
		vRez[nRez++] = v1[i];
		vRez[nRez - 1] = htons(vRez[nRez - 1]);
	}
    }
    
    nRez = htons(nRez);
    send(c, &nRez, sizeof(nRez), 0);
    send(c, vRez, sizeof(uint16_t)*nRez, 0);
    printf("Final deservire un client\n");
    close(c);
}


int main() {
	int s;
	struct sockaddr_in server, client;
	int c, l;

	s = socket(AF_INET, SOCK_STREAM, 0);
	if (s < 0) {
		printf("Err la crearea socketului server\n");
		return 1;
	}

	memset(&server, 0, sizeof(server));
	server.sin_port = htons(1235);
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;

	if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
		printf("Err la bind\n");
		return 1;
	}

	listen(s, 5);

	l = sizeof(client);
	memset(&client, 0, sizeof(client));

	while (1) {
		c = accept(s, (struct sockaddr *) &client, &l);
		printf("S-a conectat un client\n");

		if (fork() == 0) {
			deservireClient(c);
			return 0;
		}
	}
}
