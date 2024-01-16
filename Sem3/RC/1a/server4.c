#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

void deservireClient(int c) {
    uint16_t n1, n2;
    char v1[100], v2[100], vRez[200];
    uint16_t i1 = 0, i2 = 0, iRez = 0;

    recv(c, &n1, sizeof(n1), MSG_WAITALL);
    n1 = ntohs(n1);

    recv(c, v1, sizeof(char)*n1, MSG_WAITALL);

    recv(c, &n2, sizeof(n2), MSG_WAITALL);
    n2 = ntohs(n2);

    recv(c, v2, sizeof(char)*n2, MSG_WAITALL);

    printf("n1 = %d n2 = %d\n", n1, n2);
    printf("Liste primite: ");
    for (uint16_t i = 0; i < n1; i++)
	    printf("%c ", v1[i]);

    for (uint16_t i = 0; i < n2; i++)
	    printf("%c ", v2[i]);
    printf("\n");

    while (i1 < n1 && i2 < n2) {
        if (v1[i1] <= v2[i2]) {
            vRez[iRez++] = v1[i1++];
        } else {
            vRez[iRez++] = v2[i2++];
        }
    }

    while (i1 < n1) {
        vRez[iRez++] = v1[i1++];
    }

    while (i2 < n2) {
        vRez[iRez++] = v2[i2++];
    }

    send(c, vRez, sizeof(char)*iRez, 0);
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
	server.sin_port = htons(1234);
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
