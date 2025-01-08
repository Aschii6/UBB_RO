package networking.rpc;

import model.Game;
import model.Player;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Optional;

public class ClientRpcWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    public ClientRpcWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();

            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        while (connected) {
            try {

                Object request = input.readObject();
                Response response = handleRequest((Request) request);

                if (response != null) {
                    sendResponse(response);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;

        String handlerName = "handle" + (request).type();

        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    private Response handleLOGIN(Request request) {
        Player player = (Player) request.data();

        try {
            Optional<Player> opt = server.login(player, this);

            if (opt.isPresent()) {
                return new Response.Builder().type(ResponseType.OK).data(opt.get()).build();
            } else {
                return new Response.Builder().type(ResponseType.ERROR).data("Auth failed").build();
            }

        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        Player player = (Player) request.data();

        try {
            server.logout(player, this);
            connected = false;

            return okResponse;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSAVE_GAME(Request request) {
        try {
            server.saveGame((Game) request.data());

            return okResponse;
        } catch (Exception e ) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_RANKINGS(Request request) {
        try {
            return new Response.Builder().type(ResponseType.OK).data(server.getRankings()).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void update() throws Exception {
        Response response = new Response.Builder().type(ResponseType.UPDATE).build();
        sendResponse(response);
    }
}
