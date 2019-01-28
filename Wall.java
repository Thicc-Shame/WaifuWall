import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Wall {

    JFrame frame;
    JLabel l;

    Configuration c;

    ArrayList<WallElement> active;
    Song current;

    public Wall(Configuration c) {
        this.c = c;
        frame = new JFrame("w wall");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        l = new JLabel();
        frame.add(l);
        active = new ArrayList<>();
        Random r = new Random();
        current = c.getNextSong();
        current.play();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch(e.getKeyChar()) {
                    case 'w':
                        current.pause();
                        break;
                    case 's':
                        current.play();
                        break;
                    case 'd':
                        current.end();
                        current = c.getNextSong();
                        current.restart();
                        current.play();
                        break;
                    case 'a':
                        current.restart();
                }
            }
        });
    }

    private synchronized ArrayList<WallElement> getActive() {
        return active;
    }

    public synchronized void getNewElement(boolean stagger) {
        Random r = new Random();
        ArrayList<WallElement> images = c.getImages();
        WallElement next = null;
        while(next == null) { //hopefully not dangerous. Exponentially likely to halt!
            int idx = r.nextInt(images.size());
            System.out.println("Attemping to add image " + idx);
            if(!images.get(idx).active) {
                next = images.get(idx);
                next.activate();
                if(stagger)
                    next.setpos((int)(Main.WIDTH*r.nextDouble()));
                active.add(next);
                System.out.println("Added");
            }
        }
    }

    public void startUp() {
        do {
            getNewElement(true);
        } while(active.size() < c.numImages);
        frame.setVisible(true);
        run();
    }

    public void run() {
        long dt = 0;
        long prev = System.currentTimeMillis();
        BufferedImage newbg;
        float hue = (prev % 360)/360.0f;
        ArrayList<WallElement> toremove = new ArrayList<>();
        while(true) {
            dt = System.currentTimeMillis() - prev;
            if(dt >= 100) {
                System.out.println("Active images: " + active.size());
                newbg = new BufferedImage(Main.WIDTH,Main.HEIGHT,BufferedImage.TYPE_INT_ARGB);
                Graphics g = newbg.getGraphics();
                Color c = Color.getHSBColor(hue,1f,0.7f);
                System.out.println(c.toString());
                hue = (hue + 0.015f);
                g.setColor(c);
                g.fillRect(0,0,Main.WIDTH,Main.HEIGHT);
                g.dispose();
                active.forEach(WallElement::tick); //flex on java 7
                toremove.clear();
                for (WallElement we : getActive()) {
                    if (we.getpos() > Main.WIDTH) {
                        we.deactivate();
                        //active.remove(we);
                        toremove.add(we);

                    } else {
                        newbg = we.render(newbg);
                    }
                }
                for(WallElement we : toremove) {
                    active.remove(we);
                    getNewElement(false);
                }
                newbg = writeSong(newbg);
                l.setIcon(new ImageIcon(newbg));
                prev = System.currentTimeMillis();
            }
            System.out.println("Song progress: " + current);
            System.out.println(current.hasEnded());
            if(current.hasEnded()) {
                current = c.getNextSong();
                current.restart();
            }
        }
    }

    public BufferedImage writeSong(BufferedImage bi) {
        Graphics g = bi.getGraphics();
        g.setFont(new Font("Consolas",Font.PLAIN,36));
        g.drawString(current.getName(),Main.WIDTH>>2,Main.HEIGHT-(Main.HEIGHT>>2));
        g.dispose();
        return bi;
    }

}
