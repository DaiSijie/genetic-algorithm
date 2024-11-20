/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.computation;

import ch.maystre.gilbert.imageutils.BWPicture;

public class Metrics {

    private Metrics(){}

    public static long distance(BWPicture pic1, BWPicture pic2){
        if(pic1.getHeight() != pic2.getHeight() || pic1.getWidth() != pic2.getWidth()){
            return Long.MAX_VALUE;
        }

        long toReturn = 0;
        for(int y = 0; y < pic1.getHeight(); y++){
            for(int x = 0; x < pic2.getWidth(); x++){
                toReturn += Math.abs(pic1.getPixel(x, y) - pic2.getPixel(x, y));
            }
        }

        return toReturn;
    }

    public static double closeness(int idealWidth, int idealHeight, long dst){
        Double total = 8d * idealWidth * idealHeight;
        return 1 - dst/total;
    }

}
