package CleaningEnvironment;

import Helper.Pair;
import CleaningEnvironment.CleaningWorldCell.DirtLevel;
import Environment.Observation;

public class DirtObservation extends Observation
{
    private Pair<Integer,Integer> location;
    private DirtLevel cellState;

    public DirtObservation(int x, int y, boolean hasDirt, boolean hasBadDirt) 
    {
        super("Dirt");
        location = new Pair<Integer,Integer>(x, y);
        cellState = hasBadDirt ? DirtLevel.dl_badDirt : DirtLevel.dl_dirt;
        cellState = hasDirt ? cellState : DirtLevel.dl_clear;
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    public DirtLevel getIsBadDirt() {
        return cellState;
    }
    
}
