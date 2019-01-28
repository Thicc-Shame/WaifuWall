import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Configuration {

    private ArrayList<WallElement> images;
    private ArrayList<Song> songs;
    private ArrayList<Song> played;
    int numImages;

    public Configuration(String path) throws FileNotFoundException, NullPointerException, IOException, LineUnavailableException, UnsupportedAudioFileException {
        File dir = new File(path);
        System.out.println(dir);
        //if(!dir.isDirectory()) throw new FileNotFoundException();
        images = new ArrayList<>();
        songs = new ArrayList<>();
        played = new ArrayList<>();
        if(dir.listFiles() == null) {
            return;
        }
        for(File f : dir.listFiles()) {
            if(f.getName().endsWith(".wav")) {
                songs.add(new Song(f));
            } else if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
                images.add(WallElement.generate(ImageIO.read(f),Main.HEIGHT));
            }
        }
        numImages = images.size() / 5;
    }

    public Configuration(String path, int n) throws FileNotFoundException, NullPointerException, IOException, LineUnavailableException, UnsupportedAudioFileException {
        File dir = new File(path);
        if(!dir.isDirectory()) throw new FileNotFoundException();
        images = new ArrayList<>();
        songs = new ArrayList<>();
        played = new ArrayList<>();
        if(dir.listFiles() == null) {
            return;
        }
        for(File f : dir.listFiles()) {
            System.out.println(f);
            if(f.getName().endsWith(".wav")) {
                songs.add(new Song(f));
            } else if(f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
                images.add(WallElement.generate(ImageIO.read(f),Main.HEIGHT));
            }
        }
        numImages = n;
    }

    public ArrayList<WallElement> getImages() {
        return images;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Song getNextSong() {
        Random r = new Random();
        if(songs.size() == 0) {
            songs.addAll(played);
            played.clear();
        }
        Song s = songs.remove(r.nextInt(songs.size()));
        played.add(s);
        return s;
    }

}
