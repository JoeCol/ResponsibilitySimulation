package Agents;

import java.util.ArrayList;

import Environment.Environment;
import Environment.Environment.AgentAction;
import Responsibility.Delegation;
import Responsibility.Responsibility;
import Responsibility.ResponsibilityModel.Node;

public class SRAgent extends Agent 
{

    public SRAgent(String _name) {
        super(_name);
    }

    @Override
    public boolean accepts(Agent a, Environment env, Responsibility r) {
        return true;//Agent accepts any delegation of responsibiity
    }

    @Override
    public boolean accepts(Environment env, Responsibility r) {
        return true;//Agent accepts any arising responsibility
    }

    @Override
    public int getCare(Responsibility res) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCare'");
    }

    @Override
    public ArrayList<Responsibility> largestNonConflict(ArrayList<Responsibility> assigned, Agent ag) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'largestNonConflict'");
    }

    @Override
    public void setToWorkOn(ArrayList<Responsibility> toWorkOn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setToWorkOn'");
    }

    @Override
    public void observed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'observed'");
    }

    @Override
    public void processFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processFinished'");
    }

    @Override
    public void reason(Node nodet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reason'");
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finish'");
    }

    @Override
    public ArrayList<Delegation> getDelegations() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDelegations'");
    }

    @Override
    public AgentAction getAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAction'");
    }

    @Override
    public void sendMsg(String msg) {
    
    }

    @Override
    public boolean canDo(Responsibility r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canDo'");
    }

}
