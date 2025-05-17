package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mic.LiveMicReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartSoundLiveApp extends Application {

    private static final int WINDOW_SIZE = 500;
    private final LinkedList<Double> dataBuffer = new LinkedList<>();
    private final List<Double> sharedBuffer = new ArrayList<>();
    private final Object bufferLock = new Object();

    private XYChart.Series<Number, Number> series;
    private NumberAxis xAxis;
    private int counter = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(WINDOW_SIZE);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Live Phonocardiogram Signal");
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);

        series = new XYChart.Series<>();
        chart.getData().add(series);

        LiveMicReader micReader = new LiveMicReader(sample -> {
            synchronized (bufferLock) {
                sharedBuffer.add(sample);
            }
        });

        try {
            micReader.start();
        } catch (Exception e) {
            System.err.println("Failed to start microphone reader: " + e.getMessage());
            e.printStackTrace();
        }


        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            List<Double> toDraw;
            synchronized (bufferLock) {
                toDraw = new ArrayList<>(sharedBuffer);
                sharedBuffer.clear();
            }

            if (!toDraw.isEmpty()) {
                Platform.runLater(() -> {
                    for (double sample : toDraw) {
                        if (series.getData().size() > WINDOW_SIZE) {
                            series.getData().remove(0);
                        }
                        series.getData().add(new XYChart.Data<>(counter, sample));

                        if (counter > WINDOW_SIZE) {
                            xAxis.setLowerBound(counter - WINDOW_SIZE);
                            xAxis.setUpperBound(counter);
                        }
                        counter++;
                    }
                });
            }
        }, 0, 20, TimeUnit.MILLISECONDS);

        BorderPane root = new BorderPane(chart);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Live Heart Sound Viewer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
