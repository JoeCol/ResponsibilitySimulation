import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerAgent extends Agent
{
	private boolean dirty = false;
    private HashMap<Character,WorldCell.DirtLevel> zoneObserved = new HashMap<Character,WorldCell.DirtLevel>();
    private ArrayDeque<String> freeAgents = new ArrayDeque<String>();
    private ArrayDeque<String> sendToAgent = new ArrayDeque<String>();
    private Responsibility workingOn = null;

    public ManagerAgent(String _name, Routes routes, HashMap<Character, ArrayList<Pair<Integer, Integer>>> _zones) 
    {
        super(_name, routes, _zones);
        freeAgents.push("Cleaner 1");
        freeAgents.push("Cleaner 2");

        sendToAgent.push("Cleaner 1");
        sendToAgent.push("Cleaner 2");
    }

    private void updateCareValues() 
	{
        for (Responsibility r : responsibilities)
        {
            if (r.getName().matches("cleanBadDirt[A-Z]"))
			{
				char zone = r.getName().toLowerCase().charAt(r.getName().length() - 1);
                if (zoneObserved.containsKey(zone) && zoneObserved.get(zone) != WorldCell.DirtLevel.dl_clear)
                {
                    careRes.put(r, 7);
                }
                else
                {
                    careRes.put(r, 2);
                }
			}
			else if (r.getName().matches("clean[A-Z]"))
			{
				char zone = r.getName().toLowerCase().charAt(r.getName().length() - 1);
                if (zoneObserved.containsKey(zone) && zoneObserved.get(zone) != WorldCell.DirtLevel.dl_clear)
                {
                    careRes.put(r, 6);
                }
                else
                {
                    careRes.put(r, 1);
                }
			}
        }
	}

    private void processRes(Responsibility attachedRes, String assignee) 
    {
        //Assuming capable
        responsibilities.add(attachedRes);
        if (attachedRes.getName().matches("cleanBadDirt[A-Z]"))
        {
            careRes.put(attachedRes, 2);
        }
        else if (attachedRes.getName().matches("clean[A-Z]"))
        {
            careRes.put(attachedRes, 1);
        }
        else if (attachedRes.getName().matches("observe[A-Z]"))
        {
            careRes.put(attachedRes, 5);
        }
        else if (!attachedRes.getName().matches("cleanBadDirt[A-Z]|clean[A-Z]|observe[A-Z]"))
        {
            switch(attachedRes.getName())
            {
                case "report":
                    careRes.put(attachedRes, 100);
                    break;
                case "safety":
                    careRes.put(attachedRes, 500);
                    break;
                case "janitorial":
                    careRes.put(attachedRes, 1000);
                    break;
                case "cleanliness":
                    careRes.put(attachedRes, 400);
                    break;
                case "observe":
                    careRes.put(attachedRes, 8);
                    break;
                case "clean":
                    careRes.put(attachedRes, 4);
                    break;
            }
        }
        msgs.add(new Message(getName(), assignee, "accepted", attachedRes));
        for (Responsibility subRes : attachedRes.getSubRes())
        {
            processRes(subRes, assignee);
        }
    }

    @Override
    public void observed(char zone, WorldCell.DirtLevel dl) 
    {
        zoneObserved.put(zone, dl);
        updateCareValues();
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.content.equals("assignment"))
        {
            dirty = true;
            processRes(m.attachedRes, m.sender);
            updateCareValues();
        }
        else if (m.content.startsWith("isBusy", 0))
        {
            if (m.content.contains("true"))
            {
                freeAgents.remove(m.sender);
            }
            else
            {
                freeAgents.add(m.sender);
            }
        }
    }

    @Override
    public void reason() {
        if (dirty)
        {
            actions.clear();
            dirty = false;
        }
        ArrayList<ArrayList<Responsibility>> res = getViable();
        ArrayList<Responsibility> toDo = getMostCared(res);
        for (Responsibility r : toDo)
        {
            Task t = r.getTask();
            for (Task.TaskAction a : t.getActions())
            {
                char zone = 0;
                if (actions.size() == 0 && a.actionToDo.matches("observe[A-Z]"))
                {
                    zone = a.actionToDo.charAt(a.actionToDo.length() - 1);
                    goToZone(zone);
                    observe(zone);
                    actions.add(CleaningWorld.AgentAction.aa_finish);
                    workingOn = r;
                }
                else if (a.actionToDo.matches("cleanroom[A-Z]"))
                {
                    zone = a.actionToDo.charAt(a.actionToDo.length() - 1);
                    if (zoneObserved.get(zone) == WorldCell.DirtLevel.dl_badDirt)
                    {
                        if (!freeAgents.isEmpty())
                        {
                            delegate(r,freeAgents.pop());
                        }
                        else
                        {
                            goToZone(zone);
                            cleanZone(zone);
                            actions.add(CleaningWorld.AgentAction.aa_finish);
                            workingOn = r;
                        }
                    }
                    else if (zoneObserved.get(zone) == WorldCell.DirtLevel.dl_dirt)
                    {
                        String ag = sendToAgent.pop();
                        delegate(r,ag);
                        sendToAgent.push(ag);
                    }
                }
                else if (a.actionToDo.equals("sendToHuman"))
                {
                    System.out.println("A report to human");
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
        
    }

    @Override
    public void finish() {
        finishedRes.add(workingOn);
        workingOn = null;
    }

    @Override
    public void updateNaiveList(ArrayDeque<Character> naiveQueue) {
        throw new UnsupportedOperationException("Unimplemented method 'updateNaiveList'");
    }

    @Override
    public void processFinished() {
        for (Responsibility r : finishedRes)
        {
            boolean allCompleted = true;
            for (Responsibility subRes : finishedRes)
            {
                if (!finishedRes.contains(subRes))
                {
                    allCompleted = false;
                }
            }
            if (allCompleted)
            {
                if (r.getType() == Responsibility.ResType.rt_oneshot)
                {
                    responsibilities.remove(r);
                    removeResponsibility(r);
                }
                else
                {
                    informComplete(r);
                }
            }
        }
        finishedRes.clear();
    }
}
