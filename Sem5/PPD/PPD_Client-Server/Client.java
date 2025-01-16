package com.example;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static final int PORT = 8081;
    private static final String HOST = "localhost";

    private final int sendDelay;

    public Client(int sendDelay) {
        this.sendDelay = sendDelay;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your id: ");

        String id = scanner.nextLine();

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            int numberOfProblems = 10;

            for (int i = 0; i < numberOfProblems; i++) {
                System.out.println("Sending for problem " + (i + 1));
                String filename = "C:/Users/Daniel/IdeaProjects/PPD_Client-Server/Client/src/main/resources/Results" + id + "_" + "P" + (i + 1) + ".txt";

                BufferedReader reader = new BufferedReader(new FileReader(filename));

                StringBuilder linesBuilder = new StringBuilder();
                int readLines = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    readLines++;

                    String[] tokens = line.split(",");
                    String participantId = tokens[0].substring(1);
                    int points = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));

                    linesBuilder.append(participantId).append(",").append(points).append(";");

                    if (readLines == 20) {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("requestType", "add");
                        jsonObject.put("data", linesBuilder.toString());
                        jsonObject.put("countryId", id);

                        linesBuilder.setLength(0);
                        readLines = 0;

                        out.println(jsonObject);
                        out.flush();

                        try {
                            Thread.sleep(sendDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!linesBuilder.isEmpty()) {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("requestType", "add");
                    jsonObject.put("data", linesBuilder.toString());
                    jsonObject.put("countryId", id);

                    out.println(jsonObject);
                    out.flush();

                    try {
                        Thread.sleep(sendDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                /*ExecutorService executorService = Executors.newSingleThreadExecutor();

                Future<String> future = executorService.submit(() -> {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("requestType", "information");

                    out.println(jsonObject.toString());

                    String response;

                    try {
                        response = in.readLine();

                        JSONObject responseJson = new JSONObject(response);

                        return responseJson.getString("data");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error";
                    }
                });*/
            }

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("requestType", "results");
                jsonObject.put("countryId", id);

                out.println(jsonObject);

                try {
                    String response = in.readLine();

                    if (response == null) {
                        return "No response from server";
                    }

                    JSONObject responseJson = new JSONObject(response);

                    return responseJson.getString("data");
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            });

            String response = future.get();
            System.out.println(response);

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
