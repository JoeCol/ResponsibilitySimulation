package Responsibility;

import java.util.*;

import Agents.Agent;
import Environment.Environment;

public class ResponsibilityModel 
{
    public class Node
    {
        public int timet;
        public Environment env;
        public ArrayList<Agent> agents;
        Responsibilities res; //Only in centralised version
    }    
    
    private Node nodet = new Node();
    private boolean centralised = false;//Todo: Still need to implement non-centralised
    private ArrayList<NodeUpdate> nodeListeners = new ArrayList<NodeUpdate>();

    public void addWorldListeners(NodeUpdate nodeUpdate)
	{
		nodeListeners.add(nodeUpdate);
	}

    public void sendNodeUpdates()
    {
        for (NodeUpdate u : nodeListeners)
		{
			u.nodeUpdate(nodet);
		}
    }

    public void doStep()
    {
        nodet.timet = nodet.timet++;
        resolution();
        assignment();
        reason();
        delegation();
        responsibilitySelection();
        actions();
        sendNodeUpdates();
        //Record node
    }

    private void reason() 
    {
        for (Agent ag : nodet.agents)
        {
            ag.reason();
        }
    }

    public void setup(ArrayList<Agent> ag, Environment env)
    {
        Node node0 = new Node();
        node0.timet = 0;
        node0.agents = ag;
        node0.env = env;
        node0.res = new Responsibilities();
        sendNodeUpdates();
        //Record node
    }

    private void resolution() 
    {
        for (Responsibility r : nodet.res.getActiveResponsibilities())
        {
            switch (r.evaluate(nodet.env)) 
            {
                case re_failed:
                    nodet.res.removeAllAssignments(r);
                    nodet.res.AddActiveRes(r.getFailRes());
                    break;
                case re_success:
                    nodet.res.removeAllAssignments(r);
                    break;
                default:
                    break;
            }
        }
    }

    private void assignment() 
    {
        ArrayList<Responsibility> unassignedRes = nodet.res.getUnassignedResponsibilities();
        for (Responsibility r : unassignedRes)
        {
            ArrayList<Agent> acceptingAgent = new ArrayList<Agent>();
            for (Agent ag : nodet.agents)
            {
                if (ag.accepts(nodet.env,r))
                {
                    acceptingAgent.add(ag);
                }
            }
            nodet.res.addAssignment(acceptingAgent,r, nodet.timet, Integer.MAX_VALUE);
        }
    }

    private void delegation() 
    {
        for (Agent ag : nodet.agents)
        {
            for (Delegation d : ag.getDelegations())
            {
                if (allAccept(ag, d.getAgents(), d.getResponsibility()))
                {
                    int end = Math.max(nodet.timet + d.getLength(), Integer.MAX_VALUE);
                    nodet.res.addAssignment(ag, d.getAgents(), d.getResponsibility(), nodet.timet, end);
                    //nodet.res.removeAssignment(ag, d.getResponsibility()); Change to theory, still responsible when delegated
                }
            }
        }
    }

    private boolean allAccept(Agent ag, ArrayList<Agent> agents, Responsibility responsibility) {
        for (Agent a : agents)
        {
            if (a.accepts(ag, nodet.env, responsibility))
            {
                return false;
            }
        }
        return true;
    }

    private void responsibilitySelection() 
    {
        for (Agent ag : nodet.agents)
        {
            ArrayList<Responsibility> assigned = nodet.res.getSortedResponsibilitiesForAgent(ag);
            ArrayList<Responsibility> toWorkOn = new ArrayList<Responsibility>();
            toWorkOn.add(ag.largestNonConflict(assigned,ag));
            ag.setToWorkOn(toWorkOn);
        }
    }

    private void actions() 
    {
        for (Agent ag : nodet.agents)
        {
            nodet.env.applyAction(ag, ag.getAction());
        }
    }

    public void addStartingResponsibilities(ArrayList<Responsibility> all) 
    {
        nodet.res.AddActiveRes(all);
    }
}
