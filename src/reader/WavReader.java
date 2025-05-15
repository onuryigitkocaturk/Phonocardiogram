package reader;

import javax.sound.sampled.*;
import java.io.*;

public class WavReader {
    public static double[] readWav(String filePath) throws Exception {
        File file = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();

        // Sadece 16-bit, mono PCM destekleniyor
        if (format.getSampleSizeInBits() != 16 || format.getChannels() != 1) {
            throw new UnsupportedAudioFileException("Only 16-bit mono PCM WAV files are supported.");
        }

        byte[] bytes = audioInputStream.readAllBytes();
        int numSamples = bytes.length / 2;
        double[] samples = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            // Little endian: low byte + high byte
            int low = bytes[2 * i] & 0xFF;
            int high = bytes[2 * i + 1]; // signed
            short sample = (short) ((high << 8) | low); // combine
            samples[i] = sample / 32768.0; // normalize to [-1.0, 1.0]
        }

        return samples;
    }
}
