package ir.mrgkrahimy.pso_implementation;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mojtaba on 2/20/2017.
 */
public class WavPlayer {

    public void playClip() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        AudioListener audioListener = new AudioListener();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("drop.wav"));
        try {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(audioListener);
            clip.open(audioInputStream);
            try {
                clip.start();
                audioListener.waitUntilDone();
            }finally {
                clip.close();
            }
        }finally {
            audioInputStream.close();
        }
    }

    private class AudioListener implements LineListener {

        private boolean done = false;
        @Override
        public void update(LineEvent event) {
            LineEvent.Type type = event.getType();
            if (type == LineEvent.Type.STOP || type == LineEvent.Type.CLOSE)
                done=true;
        }

        public synchronized void waitUntilDone() throws InterruptedException{
            while (!done){
                wait();
            }
        }
    }
}
