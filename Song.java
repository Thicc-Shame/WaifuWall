import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Song {

    private Clip clip;
    private long duration, position;

    private boolean paused;

    private String name;

    public Song(File f) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        System.out.println(f.getName());
        AudioInputStream ais = AudioSystem.getAudioInputStream(f);
        clip = AudioSystem.getClip();
        clip.open(ais);
        duration = clip.getMicrosecondLength();
        position = 1;
        paused = true;
        name = f.getName().substring(0,f.getName().length()-4);
    }

    public void play() {
        if(paused) {
            clip.setMicrosecondPosition(position);
            clip.start();
            paused = false;
        }
    }

    public void pause() {
        if(!paused) {
            position = clip.getMicrosecondPosition();
            clip.stop();
            paused = true;
        }
    }

    public void restart() {
        position = 1;
        paused = true;
        play();
    }

    public void end() {
        paused = true;
        clip.close();
    }

    public String getName(){
        return name;
    }

    public boolean hasEnded() {
        position = clip.getMicrosecondPosition();
        return position == 0 || position == duration;
    }

    @Override
    public String toString() {
        double percent = 100* (float)(position) / (float)(duration);
        return "" + position + "/" + duration + " (" + percent + "%)";
    }
}
