import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class CleanerAgent extends Agent
{
    private boolean sentValueLast = false;
    private Responsibility workingOn;

    public CleanerAgent(String _name, Routes routes, HashMap<Character, ArrayList<Pair<Integer, Integer>>> _zones) {
        super(_name, routes, _zones);
    }

    private void processRes(Responsibility attachedRes, String assignee) 
    {
        //Assuming capable
        addResponsibility(attachedRes, assignee);
        if (attachedRes.getName().matches("cleanBadDirt[A-Z]"))
        {
            careRes.put(attachedRes, 10);
        }
        else if (attachedRes.getName().matches("clean[A-Z]"))
        {
            careRes.put(attachedRes, 1);
        }
        else if (!attachedRes.getName().matches("cleanBadDirt[A-Z]|clean[A-Z]"))
        {
            careRes.put(attachedRes, 0);
        }
        for (Responsibility subRes : attachedRes.getSubRes())
        {
            processRes(subRes, assignee);
        }
    }

    @Override
    public void receiveMessage(Message m) 
    {
        if (m.content.equals("assignment"))
        {
            processRes(m.attachedRes, m.sender);
        }
    }

    @Override
    public void reason() {
        if (getDirty())
        {
            ArrayList<ArrayList<Responsibility>> res = getViable();
            ArrayList<Responsibility> toDo = getMostCared(res);
            for (Responsibility r : toDo)
            {
                Task t = r.getTask();
                for (Task.TaskAction a : t.getActions())
                {
                    if (actions.size() == 0 && a.actionToDo.matches("cleanroom[A-Z]"))
                    {
                        if (workingOn != null && r.getName() != workingOn.getName())
                        {
                            actions.clear();
                        }
                        char zone = a.actionToDo.charAt(a.actionToDo.length() - 1);
                        workingOn = r;
                        goToZone(zone);
                        cleanZone(zone);
                        actions.add(CleaningWorld.AgentAction.aa_finish);
                    }
                    else if (a.actionToDo.equals("sendStatus"))
                    {
                        boolean busy = actions.size() > 0;
                        if (busy != sentValueLast)
                        {
                            msgs.add(new Message(getName(), "Manager", "isBusy(" + busy + ")", null));
                            sentValueLast = busy;
                        }
                    }
                }
                for (Task.TaskState s : t.getStates())
                {
                    switch (s.stateToCheck)
                    {
                        default:
                            break;
                    }
                }
            }
            setDirty(false);
        }  
    }

    @Override
    public void finish() 
    {
        finishedRes.add(workingOn);
        workingOn = null;
    }

    @Override
    public void updateNaiveList(ArrayDeque<Character> naiveQueue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNaiveList'");
    }

    @Override
    public void observed(char zone, WorldCell.DirtLevel dl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'observed'");
    }

    @Override
    public void processFinished() {
        for (Responsibility r : finishedRes)
        {
            removeResponsibility(r);
        }
        finishedRes.clear();
    }
    
}
