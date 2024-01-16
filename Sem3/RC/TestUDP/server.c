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

	uint16_t n = 0, size = 0, v[200], max = 0;
	char character, buffer[200];

	recvfrom(s, &n, sizeof(n), MSG_WAITALL, (struct sockaddr *) &client, &l);
	n = ntohs(n);

	recvfrom(s, buffer, n, MSG_WAITALL, (struct sockaddr *) &client, &l);

	int j = 0;
	while (j < n){
		int aux = 0;
		for (int i = 0; i < n; i++)
			if (buffer[i] == buffer[j])
				aux++;

		if (aux > max) {
			character = buffer[j];
			max = aux;
		}
		j++;
	}

	for(int i = 0; i < n; i++)
		if (buffer[i] == character){
			v[size++] = i;
			v[size - 1] = htons(v[size - 1]);
		}

	printf("PRELUCRATE\nChar: %c, Nr aparitii: %hu\n", character, size);

	sendto(s, &character, sizeof(char), 0, (struct sockaddr *) &client, sizeof(client));

	size = htons(size);

	sendto(s, &size, sizeof(size), 0, (struct sockaddr *) &client, sizeof(client));

	size = ntohs(size);

	sendto(s, v, size*sizeof(uint16_t), 0, (struct sockaddr *) &client, sizeof(client));

	close(s);
}
