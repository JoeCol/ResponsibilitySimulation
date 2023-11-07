package Responsibility;

import java.util.*;

import Agents.Agent;

public class Responsibilities 
{
    private ArrayList<Responsibility> activeResponsibilities = new ArrayList<Responsibility>();
    private HashSet<Assignment> assignments = new HashSet<Assignment>();

    public ArrayList<Responsibility> getActiveResponsibilities() 
    {
        return activeResponsibilities;
    }

    public void removeAllAssignments(Responsibility r) 
    {
        assignments.removeIf(assignment -> (assignment.res == r));
    }

    public void AddActiveRes(ArrayList<Responsibility> r) 
    {
        activeResponsibilities.addAll(r);
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
                sortedResponsiblities.add(a.res);
            }
        }
        sortedResponsiblities.sort(new Comparator<Responsibility>() {
            @Override
            public int compare(Responsibility o1, Responsibility o2) {
                return Integer.compare(ag.getCare(o1), ag.getCare(o2));
            }
            
        });
        return sortedResponsiblities;
    }

}
