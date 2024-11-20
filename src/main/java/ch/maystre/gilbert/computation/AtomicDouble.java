/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.computation;

public class AtomicDouble {

    private volatile double value;

    public AtomicDouble(double initialValue){
        this.value = initialValue;
    }

    public synchronized double get(){
        return value;
    }

    public synchronized void set(double newValue){
        value = newValue;
    }


}
