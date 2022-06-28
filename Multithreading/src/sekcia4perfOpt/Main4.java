package sekcia4perfOpt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main4 {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedImage originalImg = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImg = new BufferedImage(originalImg.getWidth(),originalImg.getHeight(), BufferedImage.TYPE_INT_RGB);

        long start = System.currentTimeMillis();
        //singleThreadSolution(originalImg,resultImg);
        multiThreadSolution(originalImg,resultImg,8);
        long end = System.currentTimeMillis();

        File outputFile = new File(DESTINATION);

        ImageIO.write(resultImg,"jpg",outputFile);

        System.out.println(end-start);
    }

    public static void multiThreadSolution(BufferedImage orgImg, BufferedImage resImg, int pocetVlakien) throws InterruptedException {
        List<Thread> vlakna = new ArrayList<>();
        int vyskaCastiObrazku = orgImg.getHeight() / pocetVlakien;

        for (int i = 0; i < pocetVlakien; i++) {
            final int fi = i;
            Thread vlakno = new Thread(new Runnable() {
                @Override
                public void run() {
                    //nemôžem použiť i, tu musi byť ako final premenna
                    int topCorner = vyskaCastiObrazku * fi;
                    prefarbyObrazok(orgImg,resImg,topCorner,vyskaCastiObrazku);
                }
            });

            vlakna.add(vlakno);
        }

        for (Thread t : vlakna) {
            t.start();
        }

        for (Thread t : vlakna) {
            t.join();
        }
    }

    public static void singleThreadSolution(BufferedImage orgImg, BufferedImage resImg) {
        prefarbyObrazok(orgImg,resImg,0,orgImg.getHeight());
    }

    public static void prefarbyObrazok(BufferedImage originalImg, BufferedImage resultImg, int topCorner, int height) {
        for (int x = 0; x < originalImg.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImg.getHeight(); y++) {
                prefarbyPixel(originalImg,resultImg,x,y);
            }
        }
    }

    public static void prefarbyPixel(BufferedImage originalImg, BufferedImage resultImg, int x, int y) {
        int rgb = originalImg.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red,green,blue)) {
            //purpurova je miesanica červenej a modrej, táto konkretna viac cervenej nez modrej a zelena skoro zmazana
            //Math.min vrati mensiu hodnotu z dvoch zadaných
            newRed = Math.min(255, red + 10);

            //vrati vačšiu hodnotu z dvoch zadaných
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBZFarieb(newRed,newGreen,newBlue);

        resultImg.setRGB(x,y,newRGB);
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red-green) < 30 && Math.abs(red-blue) < 30 && Math.abs(blue-green) < 30;
    }

    public static int createRGBZFarieb(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        //FF je 255
        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
