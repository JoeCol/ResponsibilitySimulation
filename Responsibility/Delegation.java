package Responsibility;

import java.util.*;

import Agents.Agent;

public class Delegation {

    private Responsibility res;
    private ArrayList<Agent> delegatingTo = new ArrayList<Agent>();
    private int length;

    public Delegation(Responsibility res, ArrayList<Agent> delegatingTo) {
        this.res = res;
        this.delegatingTo = delegatingTo;
        this.length = Integer.MAX_VALUE;
    }

    public Delegation(Responsibility res, ArrayList<Agent> delegatingTo, int length) {
        this.res = res;
        this.delegatingTo = delegatingTo;
        this.length = length;
    }

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
