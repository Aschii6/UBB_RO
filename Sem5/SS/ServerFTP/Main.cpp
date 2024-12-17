#include <iostream>
#include "Server.h"

int main()
{
    Server server;

    server.start();

    server.acceptClients();

    return 0;
}
