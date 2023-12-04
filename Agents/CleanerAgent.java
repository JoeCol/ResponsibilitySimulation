package Agents;
import java.util.ArrayList;

import CleaningEnvironment.CleaningWorld;
import Environment.Environment;
import Environment.Environment.AgentAction;
import Helper.Pair;
import Responsibility.Delegation;
import Responsibility.Responsibility;
import Responsibility.TaskResponsibility;
import Responsibility.ResponsibilityModel.Node;
import Responsibility.TaskResponsibility.TaskAction;

public class CleanerAgent extends Agent
{
    private ArrayList<Responsibility> toWork;
    private boolean lastSentActive = false;
    private Agent manager = null;
    private CleaningWorld env;

    public CleanerAgent(String _name) {
        super(_name);
    }

    @Override
    public boolean accepts(Agent a, Environment env, Responsibility r) {
        return r.getName().contains("clean");
    }

    @Override
    public boolean accepts(Environment env, Responsibility r) {
        return r.getName().contains("report");
    }

    @Override
    public int getCare(Responsibility res) {
        if (res.getName().contains("report"))
        {
            return 0;
        }
        else if (res.getName().contains("SC")) //clean safety critial dirt
        {
            return 10;
        }
        else if (res.getName().contains("clean"))
        {
            return 1;
        }
        return -1;//Should not get to this point
    }

    @Override
    public ArrayList<Responsibility> largestNonConflict(ArrayList<Responsibility> assigned, Agent ag) {
        ArrayList<Responsibility> lnfClean = new ArrayList<Responsibility>();
        ArrayList<Responsibility> lnfCleanSC = new ArrayList<Responsibility>();
        for (Responsibility r : assigned)
        {
            if (r.getName().contains("report"))
            {
                lnfClean.add(r);
                lnfCleanSC.add(r);
            }
            else if (r.getName().contains("SC"))
            {
                lnfCleanSC.add(r);
            }
            else 
            {
                lnfClean.add(r);
            }
        }
        if (lnfCleanSC.size() > 1)
        {
            return lnfCleanSC; // We know that SC is a higher care value so should be chosen
        }
        else
        {
            return lnfClean; //There are no SC to clean
        }
    }

    @Override
    public void setToWorkOn(ArrayList<Responsibility> toWorkOn) 
    {
        toWork = toWorkOn;
    }

    @Override
    public void observed() {
        // TODO 
    }

    @Override
    public void processFinished() {
        // TODO 
    }

    @Override
    public void reason(Node nodet) {
        if (manager == null)
        {
            for (Agent a : nodet.agents)
            {
                if (a.getName().contains("M1"))
                {
                    manager = a;
                }
            }
        }
        env = (CleaningWorld)nodet.env;
        //No reasoning required on responsibilities to delegate
    }

    @Override
    public void finish() {
        // TODO 
    }

    @Override
    public ArrayList<Delegation> getDelegations() {
        return this.delegations;//Never delegates
    }

    @Override
    public AgentAction getAction() 
    {
        boolean sendMsg = false;
        AgentAction envAction = AgentAction.aa_none;
        for (Responsibility r : toWork)
        {
            if (r.getResponsibilityType().getTypeName().equals("Task"))
            {
                TaskResponsibility tr = (TaskResponsibility)r.getResponsibilityType();
                for (TaskAction ta : tr.getActions())
                {
                    String action = ta.actionToDo;
                    if (action.contains("sendStatus"))
                    {
                        sendMsg = !(toWork.size() > 1 && lastSentActive) || !(toWork.size() == 1 && !lastSentActive);//(not (Got work and sent working)) or (not (no work and sent not working))
                        if (sendMsg)
                        {
                            boolean working = toWork.size() > 1;
                            manager.sendMsg(getName() + "," + working);
                            env.recordAction(this,ta.actionToDo);
                            lastSentActive = working;
                        }
                    }
                    else if (action.contains("clean"))
                    {
                        String coord = action.substring(action.indexOf("(") + 1, action.indexOf(")"));
                        String[] icoords = coord.split(",");
                        Pair<Integer, Integer> toGo = new Pair<Integer,Integer>(Integer.getInteger(icoords[0]),Integer.getInteger(icoords[1]));
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
                }
            }
        }
        return envAction;
    }

    @Override
    public void sendMsg(String msg) {
        //Not listening
    }
    
}
