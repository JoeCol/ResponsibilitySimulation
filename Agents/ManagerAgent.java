package Agents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import CleaningEnvironment.CleaningWorld;
import CleaningEnvironment.DirtObservation;
import CleaningEnvironment.CleaningWorldCell.DirtLevel;
import Environment.Environment;
import Environment.Observation;
import Environment.Environment.AgentAction;
import Helper.Pair;
import Responsibility.Delegation;
import Responsibility.Responsibilities;
import Responsibility.Responsibility;
import Responsibility.ResponsibilityModel.Node;
import Responsibility.TaskResponsibility;
import Responsibility.Responsibility.Concludes;
import Responsibility.TaskResponsibility.TaskAction;

public class ManagerAgent extends Agent
{
    private HashMap<String,Boolean> cleanerAgentState = new HashMap<String,Boolean>();
    private HashMap<String,Integer> careValues = new HashMap<String,Integer>();
    private ArrayList<Responsibility> workOn = new ArrayList<Responsibility>();
    private Character currentZone = '0';
    private CleaningWorld env;
    private ArrayList<Agent> cleanerAgents = new ArrayList<Agent>();
    private String lastDelegatingAgent = "none";
    private Node node;

    public ManagerAgent(String _name) {
        super(_name);
        careValues.put("janitorial", 1000);
        careValues.put("safety", 500);
        careValues.put("cleanliness", 400);
        careValues.put("observe", 8);
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
        return !r.getName().contains("report");
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
        for (int i = 0; i < assigned.size(); i++)
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
        workOn = toWorkOn;
    }

    @Override
    public void observed() {
        for (Observation o : observations)
        {
            DirtObservation d = (DirtObservation)o;
            updateCareValues(d.getLocation(), d.getIsBadDirt());
        }
        observations.clear();
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
        AgentAction envAction = AgentAction.aa_none;
        for (Responsibility r : workOn)
        {
            if (r.getResponsibilityType().getTypeName().equals("Task"))
            {
                TaskResponsibility tr = (TaskResponsibility)r.getResponsibilityType();
                for (TaskAction ta : tr.getActions())
                {
                    String[] action = ta.actionToDo.split("\\(");
                    switch(action[0])
                    {
                        case "clean":
                            //Choose delegation and need to check if already delegated
                            if (!node.getResponsibilities(this).isAssigned(r))
                            {
                                String toDelegate = "none";
                                if (cleanerAgentState.containsValue(false))
                                {
                                    for (Entry<String, Boolean> as : cleanerAgentState.entrySet())
                                    {
                                        if (as.getValue() == false)
                                        {
                                            toDelegate = as.getKey();
                                            break;
                                        }
                                    }
                                }
                                if (toDelegate.equals("none"))
                                {
                                    String coord = action[1].substring(0,action[1].indexOf(")"));
                                    String[] icoords = coord.split(",");
                                    Pair<Integer, Integer> toGo = new Pair<Integer,Integer>(Integer.valueOf(icoords[0]),Integer.valueOf(icoords[1]));
                                    if (r.getName().contains("SC"))
                                    {
                                        
                                        if (getX() == toGo.getFirst() && getY() == toGo.getSecond())
                                        {
                                            envAction = AgentAction.aa_clean;
                                            env.recordAction(this, ta.actionToDo);
                                        }
                                        else
                                        {
                                            envAction = Helper.Routes.getNextMove(env, new Pair<Integer,Integer>(getX(),getY()), toGo);
                                            env.recordAction(this, envAction.toString());
                                        }
                                    }
                                    else
                                    {
                                        Responsibility delegated = new Responsibility(r.getName(), r.getSubRes(), r.getResponsibilityType(), Concludes.rt_oneshot);
                                        ArrayList<Agent> delegateTo = new ArrayList<Agent>();
                                        toDelegate = lastDelegatingAgent;
                                        for (Agent a : cleanerAgents)
                                        {
                                            if (a.getName() != toDelegate)
                                            {
                                                delegateTo.add(a);
                                                lastDelegatingAgent = a.getName();
                                                break;
                                            }
                                        }
                                        delegations.add(new Delegation(delegated, delegateTo));
                                        //I've delegated so I no longer care about the square to clean, assume it is clean
                                        updateCareValues(toGo, DirtLevel.dl_clear);
                                    }
                                }
                                else
                                {
                                    Responsibility delegated = new Responsibility(r.getName(), r.getSubRes(), r.getResponsibilityType(), Concludes.rt_oneshot);
                                    ArrayList<Agent> delegateTo = new ArrayList<Agent>();
                                    for (Agent a : cleanerAgents)
                                    {
                                        if (a.getName().equals(toDelegate))
                                        {
                                            delegateTo.add(a);
                                            lastDelegatingAgent = a.getName();
                                        }
                                    }
                                    delegations.add(new Delegation(delegated, delegateTo));
                                }
                            }
                            break;
                        case "sendReport":
                            System.out.println("Report that safety critial dirt levels have exceeded safe levels");
                            env.recordAction(this, ta.actionToDo);
                            break;
                        case "observe":
                            char zoneToObserve = action[1].charAt(0);
                            if (currentZone == zoneToObserve)
                            {
                                envAction = AgentAction.aa_observedirt;
                                env.recordAction(this, ta.actionToDo);
                            }
                            else
                            {
                                Pair<Integer,Integer> toGo = env.getZoneLocation(zoneToObserve);
                                envAction = Helper.Routes.getNextMove(env, new Pair<Integer,Integer>(getX(),getY()), toGo);
                                env.recordAction(this, envAction.toString());
                            }
                            break;
                    }
                }
            }
        }
        return envAction;
    }

    @Override
    public void reason(Node nodet) {
        currentZone = ((CleaningWorld)nodet.env).getZoneIDForAgent(this);
        env = (CleaningWorld)nodet.env;
        node = nodet;
        if (cleanerAgents.isEmpty())
        {
            cleanerAgents.add(nodet.agents.get(0));//Know that manager agent is agent 2
            cleanerAgents.add(nodet.agents.get(1));
            //Setup care values for observe, now know how many rooms there are
            for (Character z : env.getZones())
            {
                careValues.put("observe(" + z + ")", 5);
            }
        }
    }

    @Override
    public void sendMsg(String msg) {
        String agent = msg.split(",")[0];
        boolean working = Boolean.parseBoolean(msg.split(",")[1]);
        cleanerAgentState.put(agent, working);
    }
	
}
