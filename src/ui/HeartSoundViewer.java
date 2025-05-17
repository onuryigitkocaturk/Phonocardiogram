package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import reader.WavReader;
import processor.PeakDetector;

public class HeartSoundViewer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        double[] samples = WavReader.readWav("dong3_HeartORG.wav");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true); // Otomatik y ekseni

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Phonocardiogram Signal");
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("");

        for (int i = 0; i < samples.length; i += 100) {
            series.getData().add(new XYChart.Data<>(i, samples[i]));
        }

        chart.getData().add(series);
        BorderPane root = new BorderPane(chart);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        int peakCount = PeakDetector.countPeaks(samples, 0.05);
        System.out.println("Peak Count: " + peakCount);

        double durationSec = samples.length / 44100.0;
        double bpm = PeakDetector.calculateBPM(peakCount, durationSec);
        System.out.println("Heart Rate (BPM): " + bpm);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
