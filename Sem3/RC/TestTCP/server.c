#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

void deservireClient(int c) {
	uint16_t n = 0, size = 0, v[200], max = 0;
	char character, buffer[200];

	recv(c, &n, sizeof(n), MSG_WAITALL);
	n = ntohs(n);

	recv(c, buffer, n, MSG_WAITALL);

	int ok = 1, j = 0;
	while (ok){
		ok = 0;
		int aux = 0;
		for (int i = 0; i < n; i++)
			if (buffer[i] == buffer[j])
				aux++;

		if (aux > max) {
			ok = 1;
			character = buffer[j];
			max = aux;
		}
	}

	for(int i = 0; i < n; i++)
		if (buffer[i] == character){
			v[size++] = i;
			v[size - 1] = htons(v[size - 1]);
		}

	printf("PRELUCRATE\nChar: %c, Nr aparitii: %hu\n", character, size);

	send(c, &character, sizeof(char), 0);

	size = htons(size);

	send(c, &size, sizeof(size), 0);

	size = ntohs(size);

	send(c, v, size*sizeof(uint16_t), 0);
	

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
