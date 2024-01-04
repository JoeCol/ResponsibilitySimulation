package Agents;
import java.util.ArrayDeque;
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
import Responsibility.Responsibility;
import Responsibility.ResponsibilityModel.Node;
import Responsibility.TaskResponsibility;
import Responsibility.Responsibility.Concludes;
import Responsibility.TaskResponsibility.TaskAction;
import Responsibility.TaskResponsibility.TaskState;

public class ManagerAgent extends Agent
{
    private HashMap<String,Boolean> cleanerAgentState = new HashMap<String,Boolean>();
    private HashMap<String,Integer> careValues = new HashMap<String,Integer>();
    private ArrayList<Responsibility> workOn = new ArrayList<Responsibility>();
    private Character currentZone = '0';
    private CleaningWorld env;
    private ArrayDeque<Agent> cleanerAgents = new ArrayDeque<Agent>();
    private Node node;
    private int numberOfSCDirt = 0;

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
        return r.getName().compareTo("report")!=0;
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
        //Get delegation agent
        ArrayList<Agent> delegateTo = new ArrayList<Agent>();
        if (cleanerAgentState.containsValue(false))
        {
            for (Entry<String, Boolean> as : cleanerAgentState.entrySet())
            {
                if (as.getValue() == false)
                {
                    for (Agent a : cleanerAgents)
                    {
                        if (a.getName().equals(as.getKey()))
                        {
                            delegateTo.add(a);
                        }
                    }
                    break;//Only delegate to one agent
                }
            }
        }
        //If no free agent, called during initialisation no need to delegate
        if (delegateTo.size() == 0 && cleanerAgents.size() > 0)
        {
            Agent next = cleanerAgents.pop();
            delegateTo.add(next);
            cleanerAgents.push(next);
        }
        for (Observation o : observations)
        {
            DirtObservation d = (DirtObservation)o;
            updateCareValues(d.getLocation(), d.getIsBadDirt());
            //If observed dirt delegate if normal dirt or delegate if safety critial dirt and free agent
            //Delegate to cleaner
            if ((d.getIsBadDirt() == DirtLevel.dl_badDirt && cleanerAgentState.containsValue(false)) || 
                (d.getIsBadDirt() == DirtLevel.dl_dirt))
            {
                String resName = "clean";
                if (d.getIsBadDirt() == DirtLevel.dl_badDirt)
                {
                    resName += "SCDirt";
                }
                resName += "(" + d.getLocation().getFirst() + "," + d.getLocation().getSecond() + ")";
                ArrayList<Responsibility> subRes = new ArrayList<Responsibility>();
                ArrayList<TaskAction> clean = new ArrayList<TaskAction>();
                clean.add(new TaskAction("clean(" + d.getLocation().getFirst() + "," + d.getLocation().getSecond() + ")"));
                TaskResponsibility cleantile = new TaskResponsibility(clean, new ArrayList<TaskState>());
                //Add delegation
                Responsibility delegated = new Responsibility(resName, subRes, cleantile, Concludes.rt_oneshot);
                
                if (!node.getResponsibilities(this).isAssigned(delegated))
                {
                    delegations.add(new Delegation(delegated, delegateTo));
                }
                //I've delegated so I no longer care about the square to clean, assume it is clean
                updateCareValues(d.getLocation(), DirtLevel.dl_clear);
            }
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
                            String coord = action[1].substring(0,action[1].indexOf(")"));
                            String[] icoords = coord.split(",");
                            Pair<Integer, Integer> toGo = new Pair<Integer,Integer>(Integer.valueOf(icoords[0]),Integer.valueOf(icoords[1]));
                            //Do the cleaning, this should only trigger when safety critial dirt is there and the other agents are busy
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
                            break;
                        case "sendReport":
                            //System.out.println("Report that safety critial dirt levels have exceeded safe levels");
                            env.addExceeded();
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
                                Pair<Integer,Integer> toGoO = env.getZoneLocation(zoneToObserve);
                                envAction = Helper.Routes.getNextMove(env, new Pair<Integer,Integer>(getX(),getY()), toGoO);
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
            cleanerAgents.push(nodet.agents.get(0));//Know that manager agent is agent 2
            cleanerAgents.push(nodet.agents.get(1));
            //Setup care values for observe, now know how many rooms there are
            for (Character z : env.getZones())
            {
                careValues.put("observe(" + z + ")", 5);
            }
        }
        numberOfSCDirt = env.getTotalSCDirt();
    }

    @Override
    public void sendMsg(String msg) {
        String agent = msg.split(",")[0];
        boolean working = Boolean.parseBoolean(msg.split(",")[1]);
        cleanerAgentState.put(agent, working);
    }

    @Override
    public boolean canDo(Responsibility r) {
        if (r.getName().compareToIgnoreCase("safety") == 0 && numberOfSCDirt >= 5) //We know the task is to compare against 5 SC dirt appearences
        {
            return false;
        }
        return r.getName().compareTo("report")!=0;
    }
	
}
