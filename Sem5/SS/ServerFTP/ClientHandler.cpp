#include "ClientHandler.h"

void ClientHandler::sendWelcomeMessage()
{
	const char* welcomeMsg = "220 Service ready for new user.\r\n";
	send(clientSocket, welcomeMsg, static_cast<int>(strlen(welcomeMsg)), 0);
}

void ClientHandler::sendResponse(const string& response)
{
	send(clientSocket, response.c_str(), static_cast<int>(response.length()), 0);
}

bool ClientHandler::starts_with(const string& str, const string& prefix) {
	string s = str;
	string p = prefix;

	transform(s.begin(), s.end(), s.begin(), ::toupper);
	transform(p.begin(), p.end(), p.begin(), ::toupper);

	return s.find(p) == 0;
}

ClientHandler::ClientHandler(SOCKET socket)
{
	clientSocket = socket;
	dataSocket = INVALID_SOCKET;
	transferMode = "binary";
	rootDirectory = "C:/FTP";
}

void ClientHandler::handle()
{
	sendWelcomeMessage();

	char buffer[BUFFER_SIZE];
	while (true) {
		memset(buffer, 0, BUFFER_SIZE);
		int bytesReceived = recv(clientSocket, buffer, BUFFER_SIZE, 0);

		if (bytesReceived <= 0) {
			cout << "Client disconnected.\n";
			break;
		}

		string command(buffer);
		command.erase(command.find_last_not_of("\n\r\t ") + 1); // remove trailing whitespaces
		cout << "Received: " << command << "\n";

		if (starts_with(command, "USER")) {
			username = command.substr(command.find(" ") + 1, command.length());
			sendResponse("331 User name okay, need password.\r\n");

			currentDirectory = rootDirectory + "/" + username;
		}
		else if (starts_with(command, "PASS")) {
			if (username.empty()) {
				sendResponse("503 Bad sequence of commands.\r\n");
				continue;
			}

			password = command.substr(command.find(" ") + 1, command.length());
			sendResponse("230 User logged in, proceed.\r\n");
		}
		else if (starts_with(command, "QUIT")) {
			sendResponse("221 Goodbye.\r\n");
			break;
		}
		else if (starts_with(command, "SYST")) {
			sendResponse("215 Windows_NT\r\n");
		}
		else if (starts_with(command, "TYPE A")) {
			transferMode = "ASCII";
			sendResponse("200 Type set to A\r\n");
		}
		else if (starts_with(command, "TYPE I")) {
			transferMode = "binary";
			sendResponse("200 Type set to I\r\n");
		}
		else if (starts_with(command, "PWD")) {
			if (currentDirectory.empty()) {
				sendResponse("530 Not logged in.\r\n");
				continue;
			}
			
			sendResponse("257 \"" + currentDirectory + "\"\r\n");
		}
		else if (starts_with(command, "CWD")) {
			handleCWD(command);
		}
		else if (starts_with(command, "PASV")) {
			handlePASV();
		}
		else if (starts_with(command, "STRU")) {
			handleSTRU(command);
		}
		else if (starts_with(command, "LIST")) {
			handleLIST();
		}
		else if (starts_with(command, "RETR")) {
			handleRETR(command);
		}
		else if (starts_with(command, "STOR")) {
			handleSTOR(command);
		}
		else if (starts_with(command, "DELE")) {
			handleDELE(command);
		}
		else if (starts_with(command, "NOOP")) {
			sendResponse("200 OK\r\n");
		}
		else {
			sendResponse("500 Unknown command.\r\n");
		}
	}

	closesocket(clientSocket);
}

void ClientHandler::handleCWD(const string& command) {
	std::string requestedDir = command.substr(4);
	
	if (requestedDir == "/" + username) {
		std::string fullPath = rootDirectory + "/" + username;

		if (filesystem::exists(fullPath)) {
			currentDirectory = fullPath;
			sendResponse("250 Requested file action okay, completed.\r\n");
		}
		else {
			try {
				if (filesystem::create_directory(fullPath)) {
					currentDirectory = fullPath;
					sendResponse("250 Requested file action okay, completed.\r\n");
				}
				else {
					sendResponse("550 Failed to create directory.\r\n");
				}
			}
			catch (const filesystem::filesystem_error& e) {
				sendResponse("550 Requested action not taken. Directory creation failed.\r\n");
				cout << "Error creating directory: " << e.what() << '\n';
			}
		}
	}
	else {
		sendResponse("550 Requested action not taken. Only allowed directory is be /" + username + ".\r\n");
	}
}

void ClientHandler::handlePASV() {
	cout << "Entering PASV mode...\n";
	SOCKET dataSocket = socket(AF_INET, SOCK_STREAM, 0);
	if (dataSocket == INVALID_SOCKET) {
		cout << "Error creating data socket.\n";
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	sockaddr_in addr;
	addr.sin_family = AF_INET;
	addr.sin_port = 0;
	if (inet_pton(AF_INET, "127.0.0.1", &addr.sin_addr) <= 0) {
		cout << "Error converting IP address.\n";
		closesocket(dataSocket);
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	if (bind(dataSocket, (sockaddr*)&addr, sizeof(addr)) == SOCKET_ERROR) {
		cout << "Error binding data socket.\n";
		closesocket(dataSocket);
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	if (listen(dataSocket, 1) == SOCKET_ERROR) {
		cout << "Error listening on data socket.\n";
		closesocket(dataSocket);
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	int addrLen = sizeof(addr);
	if (getsockname(dataSocket, (sockaddr*)&addr, &addrLen) == SOCKET_ERROR) {
		cout << "Error getting socket name.\n";
		closesocket(dataSocket);
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	int port = ntohs(addr.sin_port);
	int ip = ntohl(addr.sin_addr.s_addr);

	int ip1 = (ip >> 24) & 0xFF;
	int ip2 = (ip >> 16) & 0xFF;
	int ip3 = (ip >> 8) & 0xFF;
	int ip4 = ip & 0xFF;

	char response[200];
	sprintf_s(response, "227 Entering Passive Mode (%d,%d,%d,%d,%d,%d)\r\n",
		ip1, ip2, ip3, ip4, port / 256, port % 256);

	cout << "PASV response: " << response;
	sendResponse(response);

	this->dataSocket = dataSocket;
}


void ClientHandler::handleSTRU(const string& command) {
	string stru = command.substr(5);

	transform(stru.begin(), stru.end(), stru.begin(), ::toupper);

	if (stru == "F") {
		sendResponse("200 STRU set to F.\r\n");
	}
	else {
		sendResponse("504 Command not implemented for that parameter.\r\n");
	}
}

void ClientHandler::handleLIST() {
	if (currentDirectory.empty()) {
		sendResponse("530 Not logged in.\r\n");
		return;
	}

	if (dataSocket == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	sendResponse("150 File status okay; about to open data connection.\r\n");

	SOCKET dataConnection = accept(dataSocket, NULL, NULL);
	if (dataConnection == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	string files;
	for (const auto& entry : filesystem::directory_iterator(currentDirectory)) {
		string fileName = entry.path().filename().string();
		bool isDir = entry.is_directory();
		string fileSize = isDir ? "4096" : to_string(entry.file_size());

		auto ftime = filesystem::last_write_time(entry);
		auto sysTime = std::chrono::time_point_cast<std::chrono::system_clock::duration>(ftime - std::filesystem::file_time_type::clock::now() + std::chrono::system_clock::now());
		std::time_t cftime = chrono::system_clock::to_time_t(sysTime);
		std::tm timeInfo;
		localtime_s(&timeInfo, &cftime);
		char timeBuffer[80];
		std::strftime(timeBuffer, sizeof(timeBuffer), "%b %d %H:%M", &timeInfo);

		files += (isDir ? "d" : "-") + string("rw-r--r--") + " 1 " + username + " " + username + " " + fileSize + " " +
			string(timeBuffer) + " " + fileName + "\r\n";
	}

	send(dataConnection, files.c_str(), static_cast<int>(files.length()), 0);
	closesocket(dataConnection);
	dataSocket = INVALID_SOCKET;

	sendResponse("226 Closing data connection. Requested file action successful.\r\n");
}

void ClientHandler::handleRETR(const string& command) {
	std::string fileName = command.substr(5);
	fileName.erase(fileName.find_last_not_of("\r\n\t ") + 1);

	if (currentDirectory.empty()) {
		sendResponse("530 Not logged in.\r\n");
		return;
	}

	if (dataSocket == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	if (fileName.find("..") != string::npos)
	{
		sendResponse("550 Permission denied.\r\n");
	}

	std::string filePath = currentDirectory + "/" + fileName;

	if (!filesystem::exists(filePath)) {
		sendResponse("550 File not found.\r\n");
		return;
	}

	sendResponse("150 File status okay; about to open data connection.\r\n");

	SOCKET dataConnection = accept(dataSocket, NULL, NULL);
	if (dataConnection == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	ifstream file(filePath, ios::binary);
	if (!file.is_open()) {
		sendResponse("550 Failed to open file.\r\n");
		closesocket(dataConnection);
		dataSocket = INVALID_SOCKET;
		return;
	}

	char buffer[BUFFER_SIZE];
	while (!file.eof()) {
		file.read(buffer, BUFFER_SIZE);
		int bytesRead = static_cast<int>(file.gcount());
		int bytesSent = send(dataConnection, buffer, bytesRead, 0);

		if (bytesSent < 0) {
			sendResponse("426 Connection closed; transfer aborted.\r\n");
			break;
		}
	}

	file.close();
	closesocket(dataConnection);
	dataSocket = INVALID_SOCKET;

	sendResponse("226 Closing data connection. Requested file action successful.\r\n");
}

void ClientHandler::handleSTOR(const string& command)
{
	if (currentDirectory.empty()) {
		sendResponse("530 Not logged in.\r\n");
		return;
	}

	if (dataSocket == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	string fileName = command.substr(5);
	fileName.erase(fileName.find_last_not_of("\r\n\t ") + 1);

	string filePath = currentDirectory + "/" + fileName;

	sendResponse("150 File status okay; about to open data connection.\r\n");

	SOCKET dataConnection = accept(dataSocket, NULL, NULL);
	if (dataConnection == INVALID_SOCKET) {
		sendResponse("425 Can't open data connection.\r\n");
		return;
	}

	ofstream file(filePath, ios::binary);
	if (!file.is_open()) {
		sendResponse("550 Failed to create file.\r\n");
		closesocket(dataConnection);
		dataSocket = INVALID_SOCKET;
		return;
	}

	char buffer[BUFFER_SIZE];
	int bytesReceived;
	while ((bytesReceived = recv(dataConnection, buffer, BUFFER_SIZE, 0)) > 0) {
		file.write(buffer, bytesReceived);
	}

	file.close();
	closesocket(dataConnection);
	dataSocket = INVALID_SOCKET;

	if (bytesReceived < 0) {
		sendResponse("426 Connection closed; transfer aborted.\r\n");
	}
	else {
		sendResponse("226 Closing data connection. Requested file action successful.\r\n");
	}
}

void ClientHandler::handleDELE(const string& command)
{
	if (currentDirectory.empty()) {
		sendResponse("530 Not logged in.\r\n");
		return;
	}

	string rest = command.substr(command.find_first_of(" ") + 1);

	string fileName = rest.substr(rest.find_last_of("/") + 1);

	string filePath = currentDirectory + "/" + fileName;

	if (!filesystem::exists(filePath)) {
		sendResponse("550 File not found.\r\n");
		return;
	}

	if (filesystem::remove(filePath)) {
		sendResponse("250 Requested file action okay, completed.\r\n");
	}
	else {
		sendResponse("550 Requested action not taken.\r\n");
	}
}
