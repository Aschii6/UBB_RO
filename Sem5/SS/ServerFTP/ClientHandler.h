#pragma once
#include <WS2tcpip.h>
#include <WinSock2.h>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <filesystem>
#include <chrono>
#include <thread>

using namespace std;

#define BUFFER_SIZE 1024

class ClientHandler
{
	SOCKET clientSocket;
	SOCKET dataSocket;

	string username, password;
	string rootDirectory;
	string currentDirectory;
	string transferMode;

	void sendWelcomeMessage();
	void sendResponse(const string& response);
	bool starts_with(const string& str, const std::string& prefix);

	void handleCWD(const string& command);
	void handlePASV();
	void handleSTRU(const string& command);
	void handleLIST();
	void handleRETR(const string& command);
	void handleSTOR(const string& command);
	void handleDELE(const string& command);
public:
	ClientHandler(SOCKET socket);
	
	void handle();
};

