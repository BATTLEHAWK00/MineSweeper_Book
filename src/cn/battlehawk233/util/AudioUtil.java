package cn.battlehawk233.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.util.Objects;

public class AudioUtil {
    private static final AudioUtil instance = new AudioUtil();

    public static AudioUtil getInstance() {
        return instance;
    }

    public void PlaySound(String path) {
        try {
            BufferedInputStream inputStream =
                    new BufferedInputStream(Objects.requireNonNull(getClass().getResource(path)).openStream());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AudioUtil.getInstance().PlaySound("");
    }
}
