package Responsibility;

import java.util.*;

import Agents.Agent;

public class Assignment 
{
    int timeStart;
    int timeEnd;
    Agent assignee;
    ArrayList<Agent> assignedAgents = new ArrayList<Agent>();
    Responsibility res;

    public Assignment(Agent delegatedBy, ArrayList<Agent> acceptingAgent, Responsibility r, int start, int end) 
    {
        timeStart = start;
        timeEnd = end;
        res = r;
        assignedAgents.addAll(acceptingAgent);
        assignee = delegatedBy;
    }

    @Override
    public boolean equals(Object b)
    {
        Assignment toCompare = (Assignment)b;
        if (res == toCompare.res && 
            timeStart == toCompare.timeStart && 
            timeEnd == toCompare.timeEnd && 
            assignee == toCompare.assignee)
        {
            return true;
        }
        return false;
    }
}
