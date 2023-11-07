package Responsibility;

import java.util.*;

import Agents.Agent;

public class Delegation {

    private Responsibility res;
    private ArrayList<Agent> delegatingTo = new ArrayList<Agent>();
    private int length;

    public ArrayList<Agent> getAgents() 
    {
        return delegatingTo;
    }

    public Responsibility getResponsibility() 
    {
        return res;
    }

    public int getLength() 
    {
        return length;
    }

}
