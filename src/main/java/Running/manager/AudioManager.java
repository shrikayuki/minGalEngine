package main.java.Running.manager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private Clip bgmClip;
    private String currentBgm;

    public void playBgm(File file) {
        if (file == null || !file.isFile()) {
            System.err.println("BGM not found: " + file);
            stopBgm();
            return;
        }
        try {
            stopBgm();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(file)) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip = clip;
                currentBgm = file.getName();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play BGM: " + e.getMessage());
            stopBgm();
        }
    }

    public void playVoice(File file) {
        if (file == null || !file.isFile()) {
            System.err.println("Voice not found: " + file);
            return;
        }
        try {
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(file)) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play voice: " + e.getMessage());
        }
    }

    public void stopBgm() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
        currentBgm = null;
    }

    public String getCurrentBgm() { return currentBgm; }

    public void restoreBgm(File file) {
        if (file == null) stopBgm();
        else playBgm(file);
    }
}
