package Agents;
import java.util.ArrayList;
import Environment.Environment;
import Environment.Environment.AgentAction;
import Responsibility.Delegation;
import Responsibility.Responsibility;

public class ManagerAgent extends Agent
{

    @Override
    public boolean accepts(Agent a, Environment env, Responsibility r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public boolean accepts(Environment env, Responsibility r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public int getCare(Responsibility res) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCare'");
    }

    @Override
    public Responsibility largestNonConflict(ArrayList<Responsibility> assigned, Agent ag) {
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
    public void reason() {
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
	
}
