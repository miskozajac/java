package sekcia4perfOpt;

public class Main4 {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION = "./out/many-flowers.jpg";

    public static void main(String[] args) {

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
