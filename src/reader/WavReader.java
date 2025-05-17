package reader;

import javax.sound.sampled.*;
import java.io.*;

public class WavReader {

    public static double[] readWav(String filePath) throws Exception {
        File file = new File(filePath);
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioFormat baseFormat = in.getFormat();

        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                1,
                2,
                baseFormat.getSampleRate(),
                false
        );

        AudioInputStream din = AudioSystem.getAudioInputStream(targetFormat, in);

        byte[] audioBytes = din.readAllBytes();
        int numSamples = audioBytes.length / 2;
        double[] samples = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            int low = audioBytes[2 * i] & 0xFF;
            int high = audioBytes[2 * i + 1];
            short sample = (short) ((high << 8) | low);
            samples[i] = sample / 32768.0;
        }

        din.close();
        return samples;
    }
}