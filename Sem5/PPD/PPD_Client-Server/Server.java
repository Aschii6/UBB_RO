package com.example;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int PORT = 8081;

    private final ExecutorService readerPool;
    private final ExecutorService writerPool;

    private final MyLinkedList linkedList = new MyLinkedList();
    private final SyncedQueue queue = new SyncedQueue();

    private final PrintWriter logger;

    private long startTime = 0;

    public Server(int readerThreads, int writerThreads) {
        this.readerPool = Executors.newFixedThreadPool(readerThreads);
        this.writerPool = Executors.newFixedThreadPool(writerThreads);

        try {
            FileWriter fw = new FileWriter("src/main/resources/log.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            this.logger = new PrintWriter(bw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            writerPool.submit(() -> {
                while (true) {
                    Input input = queue.remove();

                    if (input == null) {
                        Thread.sleep(500);
                        continue;
                    }

                    MyNode node = new MyNode(input.countryId(), input.id(), input.points());
                    linkedList.add(node);
                }
            });

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                readerPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        queue.addReadingTask();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);

                String requestType = jsonObject.getString("requestType");
                String countryId = jsonObject.getString("countryId");

                logRequest(countryId + " " + requestType);

                if (requestType.equals("add")) {
                    String data = jsonObject.getString("data");

                    String[] lines = data.split(";");

                    for (String input : lines) {
                        String[] tokens = input.split(",");
                        String participantId = tokens[0];
                        int points = Integer.parseInt(tokens[1]);

                        Input inputObj = new Input(countryId, participantId, points);
                        queue.push(inputObj);
                    }
                } else if (requestType.equals("information")) {

                } else if (requestType.equals("results")) {
                    queue.readingTaskFinished();

                    CompletableFuture<List<MyNode>> future = CompletableFuture.supplyAsync(() -> {
                        while (!queue.isFinished()) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        return linkedList.getResultsSorted();
                    });

                    future.thenAccept(results -> {
                        logRequest("Sending results to " + countryId);

                        Map<String, Integer> countryPoints = new HashMap<>();

                        for (MyNode node : results) {
                            countryPoints.put(node.getCountryId(), countryPoints.getOrDefault(node.getCountryId(), 0) + node.getPoints());
                        }

                        JSONObject response = new JSONObject();

                        StringBuilder responseBuilder = new StringBuilder();

                        for (Map.Entry<String, Integer> entry : countryPoints.entrySet()) {
                            responseBuilder.append(entry.getKey()).append(",").append(entry.getValue()).append(";");
                        }

                        response.put("data", responseBuilder.toString());

                        out.println(response);
                        out.flush();

                        long endTime = System.currentTimeMillis();

                        logRequest("Finished processing in " + (endTime - startTime) + "ms");

                    }).exceptionally(e -> {
                        System.out.println("Error getting results: " + e.getMessage());
                        return null;
                    }).orTimeout(10, java.util.concurrent.TimeUnit.SECONDS);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void logRequest(String logMessage) {
        String log = "[" + System.currentTimeMillis() + "] " + logMessage + "\n";
        logger.write(log);
        logger.flush();
    }
}
