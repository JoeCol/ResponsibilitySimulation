package Agents;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import Message;
import Pair;
import Routes;
import Environment.CleaningWorld;
import Environment.WorldCell;
import Environment.WorldCell.DirtLevel;
public class NaiveAgent extends Agent
{
    char zoneToClean = 0;
    boolean ready = true;
    public NaiveAgent(String _name, Routes routes, HashMap<Character, ArrayList<Pair<Integer, Integer>>> _zones) {
        super(_name, routes, _zones);
    }

    @Override
    public void observed(char zone, WorldCell.DirtLevel dl) {
        throw new UnsupportedOperationException("Unimplemented method 'observed'");
    }

    @Override
    public void receiveMessage(Message m) {
        throw new UnsupportedOperationException("Unimplemented method 'receiveMessage'");
    }

    @Override
    public void reason() 
    {
        if (actions.size() == 0)
        {
            goToZone(zoneToClean);
            cleanZone(zoneToClean);
            actions.add(CleaningWorld.AgentAction.aa_finish);
        }
    }

    @Override
    public void finish() {
        ready = true;
    }

    @Override
    public void updateNaiveList(ArrayDeque<Character> naiveQueue) {
        if (ready)
        {
            if (zoneToClean != 0)
            {
                naiveQueue.add(zoneToClean);
            }
            zoneToClean = naiveQueue.poll();
            ready = false;
        }
    }

    @Override
    public void processFinished() 
    {
        
    }
    
}