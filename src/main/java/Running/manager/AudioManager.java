package main.java.Running.manager;

import javax.sound.sampled.*;
import java.io.File;

public class AudioManager {

    private Clip bgmClip;

    private Clip voiceClip;

    // =========================
    // 播放 BGM
    // =========================

    public void playBgm(String path) {

        stopBgm();

        try {

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                            new File(path)
                    );

            bgmClip =
                    AudioSystem.getClip();

            bgmClip.open(audio);

            // 无限循环
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);

            bgmClip.start();

        } catch (Exception e) {

            throw new RuntimeException(
                    "无法播放 BGM: " + path,
                    e
            );
        }
    }

    // =========================
    // 停止 BGM
    // =========================

    public void stopBgm() {

        if (bgmClip != null) {

            bgmClip.stop();
            bgmClip.close();

            bgmClip = null;
        }
    }

    // =========================
    // 播放角色语音
    // =========================

    public void playVoice(String path) {

        stopVoice();

        try {

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                            new File(path)
                    );

            voiceClip =
                    AudioSystem.getClip();

            voiceClip.open(audio);

            voiceClip.start();

        } catch (Exception e) {

            throw new RuntimeException(
                    "无法播放语音: " + path,
                    e
            );
        }
    }

    // =========================
    // 停止角色语音
    // =========================

    public void stopVoice() {

        if (voiceClip != null) {

            voiceClip.stop();
            voiceClip.close();

            voiceClip = null;
        }
    }
}