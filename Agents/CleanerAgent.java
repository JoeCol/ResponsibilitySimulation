package Agents;
import java.util.ArrayList;
import Environment.Environment;
import Environment.Environment.AgentAction;
import Responsibility.Delegation;
import Responsibility.Responsibility;

public class CleanerAgent extends Agent
{
    public CleanerAgent(String _name) {
        super(_name);
    }

    @Override
    public boolean accepts(Agent a, Environment env, Responsibility r) {
        return r.getName().contains("cleanSquare");
    }

    @Override
    public boolean accepts(Environment env, Responsibility r) {
        return r.getName().contains("cleanSquare");
    }

    @Override
    public int getCare(Responsibility res) {
        return 0;
    }

    @Override
    public Responsibility largestNonConflict(ArrayList<Responsibility> assigned, Agent ag) {
        return null;
    }

    @Override
    public void setToWorkOn(ArrayList<Responsibility> toWorkOn) {
        //TODO
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
    public void reason() {
        // TODO 
    }

    @Override
    public void finish() {
        // TODO 
    }

    @Override
    public ArrayList<Delegation> getDelegations() {
        // TODO 
        return this.delegations;
    }

    @Override
    public AgentAction getAction() {
        return AgentAction.aa_none;
    }
    
}
