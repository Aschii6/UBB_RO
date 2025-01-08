package networking.utils;

import networking.rpc.ClientRpcWorker;
import services.IServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer {
    private IServices server;

    public RpcConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker = new ClientRpcWorker(server, client);

        return new Thread(worker);
    }

    @Override
    public void stop() throws ServerException {
        super.stop();
    }
}
