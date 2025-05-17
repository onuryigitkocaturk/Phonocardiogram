package processor;

public class PeakDetector {

    public static int countPeaks(double[] data, double threshold) {
        int peaks = 0;
        int lastPeakIndex = -10000;
        int minDistance = 1000;

        for (int i = 1; i < data.length - 1; i++) {
            if (data[i] > threshold &&
                    data[i] > data[i - 1] &&
                    data[i] > data[i + 1] &&
                    (i - lastPeakIndex) > minDistance) {

                peaks++;
                lastPeakIndex = i;
            }
        }

        return peaks;
    }

    public static double calculateBPM(int peakCount, double durationInSeconds) {
        return (peakCount / durationInSeconds) * 60.0;
    }
}