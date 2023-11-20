package CleaningEnvironment;

import Helper.Pair;
import Environment.Observation;

public class DirtObservation extends Observation
{
    private Pair<Integer,Integer> location;
    private Boolean isBadDirt;

    public DirtObservation(int x, int y, boolean hasBadDirt) 
    {
        super("Dirt");
        location = new Pair<Integer,Integer>(x, y);
        isBadDirt = hasBadDirt;
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    public Boolean getIsBadDirt() {
        return isBadDirt;
    }
    
}
