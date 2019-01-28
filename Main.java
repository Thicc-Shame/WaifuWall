import java.io.IOException;
import java.util.Scanner;

public class Main {

    static int WIDTH = 1920;
    static int HEIGHT = 1080;

    public static void main(String[] args) {

        String path = new Scanner(System.in).nextLine();
        Configuration c = null;
        try {
            c = new Configuration(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        Wall w = new Wall(c);
        w.startUp();
    }

}
