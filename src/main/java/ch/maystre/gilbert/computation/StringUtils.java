/*
 * Gilbert Maystre
 * 21.01.18
 */

package ch.maystre.gilbert.computation;

public class StringUtils {

    private StringUtils(){}

    /**
     * Formats a closeness nicely
     *
     * @param closeness the closeness needs to be in interval [0, 1]
     *
     * @return a nicely formatted string
     */
    public static String formatCloseness(double closeness){
        closeness *= 100;
        String toReturn = "" + closeness;
        if(toReturn.length() > 5){
            toReturn = toReturn.substring(0, 5);
        }

        if(toReturn.contains(".")){
            String[] splitted = toReturn.split("\\.");
            String nat = splitted[0];
            String flo = splitted[1];
            if(nat.length() != 2){
                nat = "0" + nat;
            }
            while(flo.length() < 2){
                flo += "0";
            }
            toReturn = nat + "." + flo;
        }
        else{
            toReturn += ".00";
        }

        return toReturn + "%";
    }

}
