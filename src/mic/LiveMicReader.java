package mic;

import javax.sound.sampled.*;
import java.util.function.DoubleConsumer;

public class LiveMicReader {
    private TargetDataLine microphone;
    private final DoubleConsumer sampleConsumer;
    private boolean running = false;

    public LiveMicReader(DoubleConsumer sampleConsumer) {
        this.sampleConsumer = sampleConsumer;
    }

    public void start() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();
        running = true;

        new Thread(() -> {
            byte[] buffer = new byte[2048];
            while (running) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                for (int i = 0; i < bytesRead - 1; i += 2) {
                    int low = buffer[i] & 0xFF;
                    int high = buffer[i + 1];
                    short sample = (short) ((high << 8) | low);
                    double normalized = sample / 32768.0;
                    sampleConsumer.accept(normalized);
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        microphone.stop();
        microphone.close();
    }
}
