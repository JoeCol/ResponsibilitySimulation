package Agents;
import java.util.ArrayList;
import java.util.HashMap;

import CleaningEnvironment.DirtObservation;
import CleaningEnvironment.CleaningWorldCell.DirtLevel;
import Environment.Environment;
import Environment.Observation;
import Environment.Environment.AgentAction;
import Helper.Pair;
import Responsibility.Delegation;
import Responsibility.Responsibility;
import Responsibility.ResponsibilityModel.Node;

public class ManagerAgent extends Agent
{
    private HashMap<String,Boolean> cleanerAgentState = new HashMap<String,Boolean>();
    private HashMap<String,Integer> careValues = new HashMap<String,Integer>();

    public ManagerAgent(String _name) {
        super(_name);
        careValues.put("janitorial", 1000);
        careValues.put("safety", 500);
        careValues.put("cleanliness", 400);
        careValues.put("observe", 5);
        careValues.put("clean", 4);
        careValues.put("reportHuman", 100);
    }

    private void updateCareValues(Pair<Integer,Integer> location, DirtLevel dl)
    {
        switch (dl)
        {
            case dl_badDirt:
                careValues.put("cleanSCDirt(" + location.getFirst() + "," + location.getSecond() + ")", 7);
                careValues.put("clean(" + location.getFirst() + "," + location.getSecond() + ")", 6);
                break;
            case dl_clear:
                careValues.put("cleanSCDirt(" + location.getFirst() + "," + location.getSecond() + ")", 2);
                careValues.put("clean(" + location.getFirst() + "," + location.getSecond() + ")", 1);
                break;
            case dl_dirt:
                careValues.put("cleanSCDirt(" + location.getFirst() + "," + location.getSecond() + ")", 2);
                careValues.put("clean(" + location.getFirst() + "," + location.getSecond() + ")", 6);
                break;
            default:
                break;

        }
    }

    @Override
    public boolean accepts(Agent a, Environment env, Responsibility r) {
        return false;//Manager does not accept responsibility from others
    }

    @Override
    public boolean accepts(Environment env, Responsibility r) {
        return r.getName().contains("janitorial") || r.getName().contains("reportHuman");
    }

    @Override
    public int getCare(Responsibility res) {
        if (careValues.get(res.getName()) == null)
        {
            System.out.println(res.getName());
        }
        return careValues.get(res.getName());
    }

    @Override
    public ArrayList<Responsibility> largestNonConflict(ArrayList<Responsibility> assigned, Agent ag) 
    {
        ArrayList<Responsibility> lnc = new ArrayList<Responsibility>();
        ArrayList<Responsibility> clashingSet = new ArrayList<Responsibility>();
        for (int i = 1; i < assigned.size(); i++)
        {
            if (assigned.get(i).getName().contains("("))//If responsibility has a parameter it does clash with other responsiblities
            {
                clashingSet.add(assigned.get(i));
            }
            else //Responsibility does not clash with any other responsibility
            {
                lnc.add(assigned.get(i));
            }
        }
        //Pick the most cared about responsiblity and find the largest set which does not clash
        if (clashingSet.size() > 0)
        {
            //We can assume that only one can be picked, should be sorted so pick first
            lnc.add(clashingSet.get(0));
        }
        return lnc;
    }

    @Override
    public void setToWorkOn(ArrayList<Responsibility> toWorkOn)
    {
        
    }

    @Override
    public void observed() {
        for (Observation o : observations)
        {
            DirtObservation d = (DirtObservation)o;
            updateCareValues(d.getLocation(), d.getIsBadDirt());
        }
    }

    @Override
    public void processFinished() {
        // TODO 
    }

    @Override
    public void finish() {
        // TODO 
    }

    @Override
    public ArrayList<Delegation> getDelegations() {
        return this.delegations;
    }

    @Override
    public AgentAction getAction() {
        return AgentAction.aa_none;
    }

    @Override
    public void reason(Node nodet) {
        
    }

    @Override
    public void sendMsg(String msg) {
        String agent = msg.split(",")[0];
        boolean working = Boolean.parseBoolean(msg.split(",")[1]);
        cleanerAgentState.put(agent, working);
    }
	
}
