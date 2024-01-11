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
        private Responsibilities res; //Only in centralised version
        private HashMap<Agent,Responsibilities> agentRes;//Decentralised version

        public Responsibilities getResponsibilities(Agent ag)
        {
            if (centralised)
            {
                return agentRes.get(ag);
            }
            else
            {
                return res;
            }
        }
    }    
    
    private Node nodet = new Node();
    private boolean centralised = false;//Todo: Still need to implement non-centralised
    private ArrayList<NodeUpdate> nodeListeners = new ArrayList<NodeUpdate>();
    private ArrayList<Responsibility> startingResponsibilities = new ArrayList<Responsibility>();

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
        nodet.timet++;
        resolution();
        if (!centralised)
        {
            for (Agent ag : nodet.agents)
            {
                assignment(nodet.getResponsibilities(ag).getUnassignedResponsibilities(), ag);
            }
        }
        else
        {
            assignment(nodet.res.getUnassignedResponsibilities());
        }
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
            ag.reason(nodet);
        }
    }

    public void setup(ArrayList<Agent> ag, Environment env)
    {
        nodet = new Node();
        nodet.timet = 0;
        nodet.agents = ag;
        nodet.env = env;
        nodet.res = new Responsibilities(startingResponsibilities);
        sendNodeUpdates();
        //Allow agents to setup
        reason();
    }

    public void setup(ArrayList<Agent> ag, Environment env, HashMap<Agent,Responsibilities> startingResponsibilities)
    {
        centralised = false;
        nodet = new Node();
        nodet.timet = 0;
        nodet.agents = ag;
        nodet.env = env;
        for (Agent a : nodet.agents)
        {
            nodet.agentRes.put(a, startingResponsibilities.get(a));
        }
        sendNodeUpdates();
        //Allow agents to setup
        reason();
    }

    private void resolution() 
    {
        if (!centralised)
        {
            for (Agent ag : nodet.agents)
            {
                ArrayDeque<Responsibility> resToAdd = new ArrayDeque<Responsibility>();
                for (Responsibility r : nodet.getResponsibilities(ag).getActiveResponsibilities())
                {
                    r.doResolution(nodet);
                    if (r.failed(nodet.agents))
                    {
                        nodet.getResponsibilities(ag).removeAllAssignments(r);
                        resToAdd.addAll(r.getFailRes());
                    }
                    if (r.fulfilled())
                    {
                        nodet.getResponsibilities(ag).removeAllAssignments(r);
                    }
                }
                for (Responsibility r : resToAdd)
                {
                    nodet.getResponsibilities(ag).addActiveRes(r);
                }
            }
        }
        else
        {
            ArrayDeque<Responsibility> resToAdd = new ArrayDeque<Responsibility>();
            for (Responsibility r : nodet.res.getActiveResponsibilities())
            {
                r.doResolution(nodet);
                if (r.failed(nodet.agents))
                {
                    nodet.res.removeAllAssignments(r);
                    resToAdd.addAll(r.getFailRes());
                }
                if (r.fulfilled())
                {
                    nodet.res.removeAllAssignments(r);
                }
            }
            for (Responsibility r : resToAdd)
            {
                nodet.res.addActiveRes(r);
            }
        }
    }

    private void assignment(ArrayList<Responsibility> unassignedRes, Agent ag) 
    {
        for (Responsibility r : unassignedRes)
        {
            //Assign subresponsibilities
            assignment(r.getSubRes());
            //Check if all sub responsibilities assigned
            boolean allAssigned = true;
            for (Responsibility subRes : r.getSubRes())
            {
                if (!nodet.getResponsibilities(ag).isAssigned(subRes))
                {
                    allAssigned = false;
                    break;
                }
            }
            if (allAssigned && !nodet.getResponsibilities(ag).isAssigned(r))
            {
                ArrayList<Agent> acceptingAgent = new ArrayList<Agent>();
                for (Agent a : nodet.agents)
                {
                    if (a.accepts(nodet.env,r))
                    {
                        acceptingAgent.add(a);
                    }
                }
                nodet.getResponsibilities(ag).addAssignment(acceptingAgent,r, nodet.timet, Integer.MAX_VALUE);
            }
        }
    }

    private void assignment(ArrayList<Responsibility> unassignedRes) 
    {
        for (Responsibility r : unassignedRes)
        {
            //Assign subresponsibilities
            assignment(r.getSubRes());
            //Check if all sub responsibilities assigned
            boolean allAssigned = true;
            for (Responsibility subRes : r.getSubRes())
            {
                if (!nodet.res.isAssigned(subRes))
                {
                    allAssigned = false;
                    break;
                }
            }
            if (allAssigned && !nodet.res.isAssigned(r))
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
    }

    private void delegation() 
    {
        for (Agent ag : nodet.agents)
        {
            ArrayList<Delegation> delegated = new ArrayList<Delegation>();
            for (Delegation d : ag.getDelegations())
            {
                nodet.res.AddActiveRes(d.getResponsibility());
                if (allAccept(ag, d.getAgents(), d.getResponsibility()))
                {
                    int end = Math.max(nodet.timet + d.getLength(), Integer.MAX_VALUE);
                    nodet.res.addAssignment(ag, d.getAgents(), d.getResponsibility(), nodet.timet, end);
                    delegated.add(d);
                    //nodet.res.removeAssignment(ag, d.getResponsibility()); Change to theory, still responsible when delegated
                }
            }
            ag.delegateSuccess(delegated);
        }
        
    }

    private boolean allAccept(Agent ag, ArrayList<Agent> agents, Responsibility responsibility) {
        for (Agent a : agents)
        {
            if (!a.accepts(ag, nodet.env, responsibility))
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
            toWorkOn.addAll(ag.largestNonConflict(assigned,ag));
            ag.setToWorkOn(toWorkOn);
        }
    }

    private void actions() 
    {
        nodet.env.newStep();
        for (Agent ag : nodet.agents)
        {
            nodet.env.applyAction(ag, ag.getAction());
        }
    }

    public void addStartingResponsibilities(ArrayList<Responsibility> all) 
    {
        startingResponsibilities = all;
    }
}
