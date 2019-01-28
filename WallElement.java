import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class WallElement {

    //x position, y position, horizontal velocity, depth
    private int x, y, v;
    private double d;//depth is scale and transparency
    private BufferedImage bi;

    boolean active;

    private int startx;


    public WallElement(int y, int v, double d, BufferedImage bi0) {
        this.y = y;
        this.v = v;
        this.d = Math.sqrt(d); //d is on (0,1]
        int newwidth = (int)(bi0.getWidth()*d);
        int newheight = (int)(bi0.getHeight()*d);
        this.bi = new BufferedImage(newwidth,newheight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        Image scaled = bi0.getScaledInstance(newwidth,newheight,Image.SCALE_SMOOTH);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)d));
        g.drawImage(scaled,0,0,null);
        g.dispose();
        this.x = this.startx = -newwidth;
        active = false;
    }

    public static WallElement generate(BufferedImage bi0, int height) {
        Random r = new Random();
        int ry = r.nextInt(height);
        int rv = (int)Math.round((r.nextDouble()+1)*10);
        double rd = r.nextDouble();
        return new WallElement(ry,rv,rd,bi0);
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
        this.x = startx;
    }

    public int getpos() {
        return x;
    }

    public BufferedImage render(BufferedImage bg) {
        Graphics g = bg.getGraphics();
        g.drawImage(bi,x,y,null);
        g.dispose();
        return bg;
    }

    public void tick() {
        x += v;
        //System.out.println(x);
        //System.out.println(y);
    }

    public void setpos(int value) {
        this.x = value;
    }

}
