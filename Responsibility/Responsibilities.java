package Responsibility;

import java.util.*;

import Agents.Agent;

public class Responsibilities 
{
    private ArrayList<Responsibility> activeResponsibilities = new ArrayList<Responsibility>();
    private HashSet<Assignment> assignments = new HashSet<Assignment>();

    public Responsibilities() 
    {
        
    }

    public Responsibilities(ArrayList<Responsibility> startingResponsibilities) 
    {
        addActiveRes(startingResponsibilities);
    }

    public ArrayList<Responsibility> getActiveResponsibilities() 
    {
        return activeResponsibilities;
    }

    public void removeAllAssignments(Responsibility r) 
    {
        assignments.removeIf(assignment -> (assignment.res != r));
    }

    public void addActiveRes(Responsibility r) 
    {
        activeResponsibilities.add(r);
    }

    public void addActiveRes(ArrayList<Responsibility> r) 
    {
        for (Responsibility ar : r)
        {
            activeResponsibilities.add(ar);
            addActiveRes(ar.getSubRes());
        }
    }

    public ArrayList<Responsibility> getUnassignedResponsibilities()
    {
        HashSet<Responsibility> assigned = new HashSet<Responsibility>();
        for (Assignment a : assignments)
        {
            assigned.add(a.res);
        } //This needs testing
        ArrayList<Responsibility> unassigned = activeResponsibilities;
        unassigned.removeAll(assigned);
        return unassigned;
    }

    public void addAssignment(ArrayList<Agent> acceptingAgent, Responsibility r, int start, int end) 
    {
        assignments.add(new Assignment(null, acceptingAgent, r, start, end));//null is initial
    }

    public void addAssignment(Agent delegatingAgent, ArrayList<Agent> acceptingAgent, Responsibility r, int start, int end) 
    {
        assignments.add(new Assignment(delegatingAgent, acceptingAgent, r, start, end));
    }

    public ArrayList<Responsibility> getSortedResponsibilitiesForAgent(Agent ag) 
    {
        ArrayList<Responsibility> sortedResponsiblities = new ArrayList<Responsibility>();
        for (Assignment a : assignments)
        {
            if (a.assignedAgents.contains(ag))
            {
                sortedResponsiblities.add(a.res); //Sub responsibilities have their own assignment
            }
        }
        sortedResponsiblities.sort(new Comparator<Responsibility>() {
            @Override
            public int compare(Responsibility o1, Responsibility o2) {
                return Integer.compare(ag.getCare(o2), ag.getCare(o1));
            }
            
        });
        return sortedResponsiblities;
    }

	public void AddActiveRes(Responsibility responsibility) 
    {
        activeResponsibilities.add(responsibility);
	}

    public boolean isAssigned(Responsibility subRes) 
    {
        for (Assignment a : assignments)
        {
            if (a.res == subRes)
            {
                return true;
            }
        }
        return false;
    }

}
