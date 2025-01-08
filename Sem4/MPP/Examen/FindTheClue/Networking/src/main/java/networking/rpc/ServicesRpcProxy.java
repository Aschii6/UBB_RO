package networking.rpc;

import model.Configuration;
import model.Game;
import model.Player;
import model.Ranking;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Optional<Player> login(Player player, IObserver client) throws Exception {
        initializeConnection();

        Request request = new Request.Builder().type(RequestType.LOGIN).data(player).build();

        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            this.client = client;

            Player p = (Player) response.data();

            return Optional.of(p);
        }

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
//            closeConnection();
            throw new Exception(err);
        }

        return Optional.empty();
    }

    @Override
    public void logout(Player player, IObserver client) throws Exception {
         Request request = new Request.Builder().type(RequestType.LOGOUT).data(player).build();

            sendRequest(request);
            Response response = readResponse();

            closeConnection();

            if (response.type() == ResponseType.ERROR) {
                String err = response.data().toString();
                throw new Exception(err);
        }
    }

    @Override
    public void saveGame(Game game) throws Exception {
        Request request = new Request.Builder().type(RequestType.SAVE_GAME).data(game).build();

        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public Configuration getConfiguration() throws Exception {
        Request request = new Request.Builder().type(RequestType.GET_CONFIGURATION).build();

        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            System.out.println(err);
        }

        return (Configuration) response.data();
    }

    @Override
    public Ranking[] getRankings() throws Exception {
        Request request = new Request.Builder().type(RequestType.GET_RANKINGS).build();

        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }

        return (Ranking[]) response.data();
    }

    private void closeConnection() {
        finished = true;

        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (Exception e) {
            throw new Exception("Error initializing connection " + e);
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE;
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.UPDATE) {
            try {
                client.update();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
