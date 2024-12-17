#pragma once
#include <WS2tcpip.h>
#include <WinSock2.h>
#include <thread>
#include <iostream>
#include <string>

#define FTP_PORT 21

class Server
{
	int port;
	SOCKET serverSocket;

	bool initializeWinsock();
	bool createSocket();
	bool bindSocket();

	void handleClient(SOCKET clientSocket);
public:
	Server();
	~Server();

	bool start();
	void acceptClients();
};

