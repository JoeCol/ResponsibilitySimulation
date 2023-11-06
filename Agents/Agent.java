package Agents;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import Message;
import Pair;
import Routes;
import Environment.CleaningWorld;
import Environment.WorldCell;
import Environment.CleaningWorld.AgentAction;
import Environment.WorldCell.DirtLevel;
import Responsibility.Responsibility;
import Responsibility.Responsibility.ResType;

public abstract class Agent 
{
    private String name;
	private boolean dirty = false;
    protected ArrayDeque<Message> msgs = new ArrayDeque<Message>();
    protected ArrayList<Responsibility> responsibilities = new ArrayList<Responsibility>();
	protected HashMap<Responsibility,String> assigned = new HashMap<Responsibility,String>();
	protected ArrayList<Responsibility> finishedRes = new ArrayList<Responsibility>();
	protected HashMap<Responsibility,Integer> careRes = new HashMap<Responsibility,Integer>();
    protected ArrayDeque<CleaningWorld.AgentAction> actions = new ArrayDeque<CleaningWorld.AgentAction>();
    private Routes routePlanner;
    private Pair<Integer, Integer> location;
    private WorldCell[][] world;
    private HashMap<Character, ArrayList<Pair<Integer, Integer>>> zones;

    protected ArrayList<Responsibility> getMostCared(ArrayList<ArrayList<Responsibility>> possible) 
    {
		//Get the list of unique items
		ArrayList<ArrayList<Responsibility>> fullResUnique = new ArrayList<ArrayList<Responsibility>>();
		for (int i = 0; i < possible.size(); i++)
		{
			ArrayList<Responsibility> resUnique = new ArrayList<Responsibility>();
			ArrayList<Responsibility> lst = possible.get(i);
			for (int j = 0; j < lst.size(); j++)
			{
				boolean unique = false;
				for (int k = 0; k < possible.size(); k++)
				{
					if (i != k)
					{
						if (!(possible.get(k).contains(lst.get(j))))
						{
							unique = true;
							break;
						}
					}
				}
				if (unique)
				{
					resUnique.add(lst.get(j));
				}
			}
			fullResUnique.add(resUnique);
		}
		int index = 0;
		int max = 0;
		int i = 0;
		for (ArrayList<Responsibility> res : fullResUnique)
		{
			int thisMax = 0;
			for (Responsibility r : res)
			{
				thisMax = careRes.get(r) > thisMax ? careRes.get(r) : thisMax;
			}
			if (thisMax > max)
			{
				index = i;
				max = thisMax;
			}
			i++;
		}
		return possible.get(index);
	}

	protected ArrayList<ArrayList<Responsibility>> getViable() 
    {
		ArrayList<ArrayList<Responsibility>> viableLst = new ArrayList<ArrayList<Responsibility>>();
		ArrayList<Responsibility> commonLst = new ArrayList<Responsibility>();
		ArrayList<Responsibility> individual = new ArrayList<Responsibility>();

	    for (Responsibility res : responsibilities)
		{
			if (res.getName().matches("cleanBadDirt[A-Z]"))
			{
				individual.add(res);
			}
			else if (res.getName().matches("clean[A-Z]"))
			{
				individual.add(res);
			}
			else if (res.getName().matches("observe[A-Z]"))
			{
				individual.add(res);
			}
			else if (!res.getName().matches("cleanBadDirt[A-Z]|clean[A-Z]|observe[A-Z]"))
			{
				commonLst.add(res);
			}
		}
		if (individual.size() > 0)
		{
			for (Responsibility res : individual)
			{
				ArrayList<Responsibility> tmp = new ArrayList<Responsibility>();
				tmp.addAll(commonLst);
                tmp.add(res);
				tmp.sort(new Comparator<Responsibility>() {
					@Override
					public int compare(Responsibility o1, Responsibility o2) {
						int rst = careRes.get(o1).compareTo(careRes.get(o2));
						return -rst;//largest first
					}
					
				});
				viableLst.add(tmp);
			}
		}
		else
		{
			viableLst.add(commonLst);
		}
		
		return viableLst;
	}

    protected void cleanZone(char zone)
    {
        ArrayList<Pair<Integer, Integer>> allSquares = zones.get(zone);
		Pair<Integer, Integer> prevSquare = allSquares.get(0);
		Pair<Integer, Integer> nextSquare;
		actions.add(CleaningWorld.AgentAction.aa_clean);
		for (int i = 1; i < allSquares.size(); i++)
		{
			nextSquare = allSquares.get(i);
			actions.addAll(routePlanner.actionsToZone(world, prevSquare, nextSquare));
			actions.add(CleaningWorld.AgentAction.aa_clean);
			prevSquare = nextSquare;
		}
    }

    //Add move actions to agentactions stack
	protected void goToZone(char zone) 
	{
		ArrayDeque<CleaningWorld.AgentAction> moveActions = routePlanner.actionsToZone(world, location, zones.get(zone).get(0));
        actions.addAll(moveActions);
	}

	protected void observe(char zone)
	{
		actions.add(CleaningWorld.AgentAction.aa_observedirt);
	}

	protected boolean getDirty()
	{
		return dirty;
	}

	protected void setDirty(boolean _dirty)
	{
		dirty = _dirty;
	}

    protected void addResponsibility(Responsibility r, String assignee)
    {
        responsibilities.add(r);
		assigned.put(r, assignee);
		dirty = true;
		msgs.add(new Message(getName(), assignee, "accepted", r));
    }

	protected void removeResponsibility(Responsibility r)
    {
        responsibilities.remove(r);
		String assignee = assigned.get(r);
		assigned.remove(r);
		msgs.add(new Message(name, assignee, "finished", r));
		dirty = true;
		if (r.getType() == Responsibility.ResType.rt_repeat)
		{
			addResponsibility(r, assignee);
		}
    }

	protected void informComplete(Responsibility r)
	{
		String assignee = assigned.get(r);
		msgs.add(new Message(name, assignee, "finished", r));
	}

	protected void delegate(Responsibility r, String to)
	{
		Message toDelegate = new Message(name, to, "assignment", r);
		msgs.add(toDelegate);
		assigned.put(r, to);
	}

    public abstract void observed(char zone, WorldCell.DirtLevel dl);
    public abstract void receiveMessage(Message m);
	public abstract void processFinished();
    public abstract void reason();
    public abstract void finish();

    public void updateLocation(Pair<Integer,Integer> _location, WorldCell[][] _world)
    {
        location = _location;
        world = _world;
    }

    public CleaningWorld.AgentAction getAction()
    {
        if (actions.isEmpty())
        {
            return CleaningWorld.AgentAction.aa_none;
        }
        return actions.peek();
    }

    public Agent(String _name, Routes routes, HashMap<Character, ArrayList<Pair<Integer, Integer>>> _zones)
    {
        name = _name;
        routePlanner = routes;
        zones = _zones;
    }

    public boolean hasMessageToSend()
    {
        return !msgs.isEmpty();
    }

    public Message getNextMessage() {
        return msgs.poll();
    }

    public String getName() {
        return name;
    }

    public abstract void updateNaiveList(ArrayDeque<Character> naiveQueue);

    public void actionFinished(boolean actionFinished) 
	{
		if (actions.size() > 0 && actionFinished)
		{
			actions.remove();
		}
    }
}
