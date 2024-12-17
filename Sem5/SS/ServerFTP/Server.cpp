#include "Server.h"
#include "ClientHandler.h"

Server::Server()
{
	port = FTP_PORT;
	serverSocket = INVALID_SOCKET;
}

Server::~Server()
{
	if (serverSocket != INVALID_SOCKET) {
		closesocket(serverSocket);
	}
	WSACleanup();
}

bool Server::start()
{
    if (!initializeWinsock()) return false;
    if (!createSocket()) return false;
    if (!bindSocket()) return false;

    if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR) {
        std::cerr << "Listen failed.\n";
        return false;
    }

    std::cout << "FTP Server listening on port " << port << "\n";
    return true;
}

bool Server::initializeWinsock() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "WSAStartup failed.\n";
        return false;
    }
    return true;
}

bool Server::createSocket()
{
    serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (serverSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed.\n";
        WSACleanup();
        return false;
    }
    return true;
}

bool Server::bindSocket()
{
    sockaddr_in serverAddr = {};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(port);

    if (bind(serverSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        std::cerr << "Bind failed.\n";
        closesocket(serverSocket);
        WSACleanup();
        return false;
    }
    return true;
}

void Server::handleClient(SOCKET clientSocket) {
    ClientHandler clientHandler(clientSocket);
    clientHandler.handle();
}

void Server::acceptClients() {
    while (true) {
        sockaddr_in clientAddr;
        int clientAddrLen = sizeof(clientAddr);
        SOCKET clientSocket = accept(serverSocket, (sockaddr*)&clientAddr, &clientAddrLen);

        if (clientSocket == INVALID_SOCKET) {
            std::cerr << "Failed to accept client.\n";
            continue;
        }

        std::cout << "Client connected.\n";
        std::thread(&Server::handleClient, this, clientSocket).detach();
    }
}