Phonocardiogram Monitoring System - Java Implementation

Project Structure:
├── reader/
│   └── WavReader.java
├── processor/
│   └── PeakDetector.java
├── ui/
│   └── HeartSoundViewer.java
├── heart_sample.wav     ← heart sound input file
└── README.txt

Description:
This Java-based project simulates a phonocardiogram (PCG) system. It reads a heart sound signal from a `.wav` file, visualizes the waveform using JavaFX, and calculates the heart rate in BPM (beats per minute) via peak detection.

Requirements:
- Java 17 or later
- JavaFX installed and configured
- A valid `.wav` file (e.g., heart_sample.wav)

How to Run:
1. Place `heart_sample.wav` in the root directory of the project.
2. Compile and run `HeartSoundViewer.java`.
3. A graphical window will display the waveform, and the heart rate (BPM) will be printed to the console.

Notes:
- The sample rate is assumed to be 44.1 kHz.
- You may need to adjust the threshold value in `PeakDetector` depending on the signal's amplitude.

Developed by:
Computer Engineering Team
COM2044 – Object-Oriented Programming Course
Ankara University – Spring 2025
