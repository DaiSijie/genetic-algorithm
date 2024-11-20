/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.imageutils;

import ch.maystre.gilbert.computation.AtomicDouble;
import ch.maystre.gilbert.computation.Metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static ch.maystre.gilbert.ui.CumulativeSelection.RANDOM;

public class PictureCumulator {

    public static final int START_CHILDREN_NUMBER = 2;

    public static final int START_SWITCH_PROBABILITY = 3;

    public static final int START_SWITCH_RANGE = 2;

    private final BWPicture ideal;

    private BWPicture current;

    private AtomicLong counter = new AtomicLong(0);

    private AtomicDouble closeness = new AtomicDouble(0);

    // the probability that a pixel gets modified
    private AtomicDouble switchProbability = new AtomicDouble(Math.pow(10, -START_SWITCH_PROBABILITY));

    // the range into which the pixel can be modified
    private AtomicInteger switchRange = new AtomicInteger(START_SWITCH_RANGE);

    // the number of children each round gets (in addition to the current one!)
    private AtomicInteger childrenNumber = new AtomicInteger(START_CHILDREN_NUMBER);

    public PictureCumulator(BWPicture ideal){
        this.ideal = ideal;
        this.current = new BWPicture(ideal.getWidth(), ideal.getHeight(), true);
    }

    public BWPicture next(){
        BWPicture[] toCompare = new BWPicture[childrenNumber.get() + 1];
        toCompare[0] = current;

        for(int i = 1; i < toCompare.length; i++){
            toCompare[i] = tweak(current.copy());
        }

        int bestIndex = -1;
        long best = Long.MAX_VALUE;
        for(int i = 0; i < toCompare.length; i++){
            long dst = Metrics.distance(toCompare[i], ideal);
            if(dst < best){
                bestIndex = i;
                best = dst;
            }
        }

        counter.incrementAndGet();
        closeness.set(Metrics.closeness(ideal.getWidth(), ideal.getHeight(), best));
        current = toCompare[bestIndex];
        return current;
    }

    public void setSwitchProbability(double switchProbability){
        this.switchProbability.set(switchProbability);
    }

    public void setSwitchRange(int switchRange){
        this.switchRange.set(switchRange);
    }

    public void setChildrenNumber(int childrenNumber){
        this.childrenNumber.set(childrenNumber);
    }

    public long getGeneration(){
        return counter.get();
    }

    public double getCloseness(){
        return closeness.get();
    }

    private BWPicture tweak(BWPicture toTweak){
        int n = (int) Math.round(toTweak.getHeight() * toTweak.getWidth() * switchProbability.get());
        for(int i = 0; i < n; i++){
            int x  = RANDOM.nextInt(toTweak.getWidth());
            int y = RANDOM.nextInt(toTweak.getHeight());
            toTweak.setPixel(x, y, newColor(toTweak.getPixel(x, y)));
        }
        return toTweak;
    }

    private int newColor(int current){
        int toReturn = current;
        toReturn += RANDOM.nextInt(2 * switchRange.get() + 1) - switchRange.get();
        toReturn = Math.max(0, toReturn);
        toReturn = Math.min(7, toReturn);
        return toReturn;
    }

}
