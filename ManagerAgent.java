import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerAgent extends Agent
{
	
    private HashMap<Character,WorldCell.DirtLevel> zoneObserved = new HashMap<Character,WorldCell.DirtLevel>();
    private ArrayDeque<String> freeAgents = new ArrayDeque<String>();
    private ArrayDeque<String> sendToAgent = new ArrayDeque<String>();
    private Responsibility workingOn = null;

    public ManagerAgent(String _name, Routes routes, HashMap<Character, ArrayList<Pair<Integer, Integer>>> _zones) 
    {
        super(_name, routes, _zones);
        freeAgents.add("Cleaner 1");
        freeAgents.add("Cleaner 2");

        sendToAgent.add("Cleaner 1");
        sendToAgent.add("Cleaner 2");
    }

    private void updateCareValues() 
	{
        for (Responsibility r : responsibilities)
        {
            if (r.getName().matches("cleanBadDirt[A-Z]"))
			{
				char zone = r.getName().charAt(r.getName().length() - 1);
                if (zoneObserved.containsKey(zone) && zoneObserved.get(zone) == WorldCell.DirtLevel.dl_badDirt)
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
				char zone = r.getName().charAt(r.getName().length() - 1);
                if (zoneObserved.containsKey(zone) && zoneObserved.get(zone) == WorldCell.DirtLevel.dl_dirt)
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
        addResponsibility(attachedRes,assignee);
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
        if (getDirty())
        {
            actions.clear();
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
                            observed(zone, WorldCell.DirtLevel.dl_clear);
                            if (!freeAgents.isEmpty())
                            {
                                Responsibility delegated = new Responsibility(r.getName(), r.getSubRes(), r.getTask(), Responsibility.ResType.rt_oneshot);
                                delegate(delegated,freeAgents.remove());
                                finishedRes.add(r);
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
                            Responsibility delegated = new Responsibility(r.getName(), r.getSubRes(), r.getTask(), Responsibility.ResType.rt_oneshot);
                            observed(zone, WorldCell.DirtLevel.dl_clear);
                            String ag = sendToAgent.remove();
                            delegate(delegated,ag);
                            sendToAgent.add(ag);
                            finishedRes.add(r);
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
            setDirty(false);
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
            for (Responsibility subRes : r.getSubRes())
            {
                if (!finishedRes.contains(subRes))
                {
                    allCompleted = false;
                }
            }
            if (allCompleted)
            {
                removeResponsibility(r);
            }
        }
        finishedRes.clear();
    }
}
