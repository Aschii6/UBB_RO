package com.example.sem10;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    TextField textField = new TextField();
    TextArea textArea = new TextArea();
    Button button = new Button("Start printing");
    PrintToTextAreaRunnable printToTextAreaRunnable = new PrintToTextAreaRunnable(textArea);

    private static class PrintToTextAreaRunnable implements Runnable {
        private TextArea textArea;

        public PrintToTextAreaRunnable(TextArea textArea) {
            this.textArea = textArea;
        }

        public void run() {
            textArea.appendText(Thread.currentThread().getName() + " started...\n");
            for (int i = 0; i < 10; i++) {
                textArea.appendText(Thread.currentThread().getName() + " - Number:" + i + "\n");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            textArea.appendText(Thread.currentThread().getName() + " finished");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        button.setOnAction(event -> {
//            printToTextAreaRunnable.run();
            Thread th = new Thread(printToTextAreaRunnable);
            th.start();
        });
        BorderPane root = new BorderPane();
        root.setTop(textField);
        root.setCenter(button);
        root.setBottom(textArea);
        primaryStage.setTitle("Hello Threads");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}