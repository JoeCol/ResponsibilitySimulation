package CleaningEnvironment;

import Helper.Pair;

public class DirtObservation 
{
    private Pair<Integer,Integer> location;
    private Boolean isBadDirt;

    public DirtObservation(int x, int y, boolean hasBadDirt) 
    {
        location = new Pair<Integer,Integer>(x, y);
        isBadDirt = hasBadDirt;
    }
    
}
