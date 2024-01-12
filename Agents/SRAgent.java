package Agents;

import java.util.ArrayList;

import Environment.Environment;
import Environment.Observation;
import Environment.Environment.AgentAction;
import Responsibility.Delegation;
import Responsibility.Responsibility;
import Responsibility.TaskResponsibility;
import Responsibility.Responsibility.Concludes;
import Responsibility.ResponsibilityModel.Node;
import SREnvironment.FireObservation;
import SREnvironment.HumanObservation;

public class SRAgent extends Agent 
{
    private Node currentNode;
    private ArrayList<Responsibility> workOn;

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
        workOn = toWorkOn;
    }

    @Override
    public void observed() {
        for (Observation obs : observations)
        {
            if (obs.getType() == "fire")
            {
                FireObservation fo = ((FireObservation)obs);
                TaskResponsibility putOutFire = new TaskResponsibility(null, null);
                Responsibility r = new Responsibility("FireAt", new ArrayList<Responsibility>(), putOutFire, Concludes.rt_oneshot);
                currentNode.getResponsibilities(this).addActiveRes(r);
            }
            else if (obs.getType() == "human")
            {
                HumanObservation ho = ((HumanObservation)obs);
                TaskResponsibility rescueHuman = new TaskResponsibility(null, null);
                Responsibility r = new Responsibility("HumanAt", new ArrayList<Responsibility>(), rescueHuman, Concludes.rt_oneshot);
                currentNode.getResponsibilities(this).addActiveRes(r);
            }
        }
        observations.clear();
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
        currentNode = nodet;

        //Check if able to sync responsibilities with other agents
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
        AgentAction toRet = AgentAction.aa_none;
        for (Responsibility resToWorkOn : workOn)
        {
            switch (resToWorkOn.getName())
            {
                //ConfirmZone" + c + "Clear
                //HumanAt
                //FireAt
                case "PrioritiseHumans"://Need to work out exactly how this will work, perhaps add/minus care values
                    break;
                case "ConfirmAllZonesClear"://No action needed
                    break;
                default:
                    System.out.println("Not implemented:" + resToWorkOn.getName());
                    break;
            }
        }
        return toRet;
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
