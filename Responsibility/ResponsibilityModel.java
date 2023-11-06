package Responsibility;

import java.util.*;

import Agents.Agent;
import Environment.CleaningWorld;
import Environment.Environment;

interface NodeUpdate{
	void nodeUpdate(Environment env);
}

public class ResponsibilityModel 
{
    public class Node
    {
        int timet;
        Environment env;
        ArrayList<Agent> agents;
        Responsibilities res; //Only in centralised version
    }    
    
    private Node nodet = new Node();
    private boolean centralised = false;
    private ArrayList<NodeUpdate> nodeListeners = new ArrayList<NodeUpdate>();

    public void addWorldListeners(NodeUpdate u)
	{
		nodeListeners.add(u);
	}

    public void sendNodeUpdates()
    {
        for (NodeUpdate u : nodeListeners)
		{
			u.nodeUpdate(nodet.env);
		}
    }

    public void doStep()
    {
        nodet.timet = nodet.timet++;
        resolution();
        assignment();
        delegation();
        responsibilitySelection();
        actions();
    }

    public void setup(ArrayList<Agent> ag, Environment env)
    {
        Node node0 = new Node();
        node0.timet = 0;
        node0.agents = ag;
        node0.env = env;
        node0.res = new Responsibilities();
        nodes.add(node0);
    }

    private void resolution() 
    {
        for (Responsibility r : nodet.res.ActiveRes)
        {
            if (r.failed())
            {
                nodet.Assignments.remove(r);
                nodet.ActiveRes.add(r.getFailRes());
            }
            else if (r.fulfilled())
            {
                nodet.Assignments.remove(r);
            }
        }
    }

    private void assignment() 
    {
        ArrayList<Responsibility> unassignedRes = nodet.res.minus(nodet.res.Assignments.res);
        for (Responsibility r : unassignedRes)
        {
            ArrayList<Agent> acceptingAgent = new ArrayList<Agent>();
            for (Agent ag : nodet.agents)
            {
                if (ag.accepts(initial,r))
                {
                    acceptingAgent.add(ag);
                }
            }
            for (Agent ag : acceptingAgent)
            {
                nodet.res.Assignments.add(ag,r);
            }
        }
    }

    private void delegation() 
    {
        for (Agent ag : nodet.agents)
        {
            for (Delegation d : ag.getDelegations())
            {
                if (allAccept(ag, d.agents, d.res))
                {
                    nodet.res.Assignments.addAll(ag, d.agents, d.res, d.length);
                    nodet.res.Assignments.minus(ag,res);
                }
            }
        }
    }

    private void responsibilitySelection() 
    {
        for (Agent ag : nodet.agents)
        {
            ArrayList<Responsibility> assigned = nodet.res.Assignments.getSorted(ag);
            ArrayList<Responsibility> toWorkOn = new ArrayList<Responsibility>();
            toWorkOn.add(assigned.get(0));//Problem if no responsibilities assigned
            toWorkOn.add(largestNonConflict(assigned,ag));
            ag.setToWorkOn(toWorkOn);
        }
    }

    private void actions() 
    {
        for (Agent ag : nodet.agents)
        {
            nodet.env.applyAction(ag.getAction());
        }
    }
}
