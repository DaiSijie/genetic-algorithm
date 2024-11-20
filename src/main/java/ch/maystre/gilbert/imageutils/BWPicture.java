/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.imageutils;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import static ch.maystre.gilbert.ui.CumulativeSelection.RANDOM;

/**
 * Pictures with 8 levels of grey
 */
public class BWPicture {

    private final int width;
    private final int height;
    private final int[][] data;

    public BWPicture(int width, int height, boolean fillWithRandom){
        this.width = width;
        this.height = height;
        data = new int[height][width];
        if(fillWithRandom) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    data[y][x] = to8Greyscale(RANDOM.nextInt() & 0xFF);
                }
            }
        }
    }

    public BWPicture(BufferedImage image){
        this.width = image.getWidth();
        this.height = image.getHeight();
        data = new int[height][width];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                data[y][x] = to8Greyscale(image.getRGB(x, y) & 0xFF);
            }
        }
    }

    public BWPicture copy(){
        BWPicture toReturn = new BWPicture(width, height, false);
        for(int y = 0; y < height; y++){
            toReturn.data[y] = Arrays.copyOf(data[y], data[y].length);
        }
        return toReturn;
    }

    public void setPixel(int x, int y, int b){
        data[y][x] = b;
    }

    public int getPixel(int x, int y){
        return data[y][x];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public BufferedImage toBufferedImage(){
        BufferedImage toReturn = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                toReturn.setRGB(x, y, to256Greyscale(data[y][x]) * 0x00010101);
            }
        }
        return toReturn;
    }

    private int to8Greyscale(int in){
        return in / 32;
    }

    private int to256Greyscale(int in){
        return in * 32;
    }

}
