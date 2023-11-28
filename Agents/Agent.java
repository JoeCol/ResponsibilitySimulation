package Agents;
import java.util.ArrayList;
import java.util.HashMap;

import Environment.Environment;
import Environment.Observation;
import Environment.Environment.AgentAction;
import Responsibility.Delegation;
import Responsibility.Responsibility;
import Responsibility.ResponsibilityModel.Node;

public abstract class Agent 
{
    private String name;
	private int x;
	private int y;
	protected HashMap<Responsibility,Integer> careRes = new HashMap<Responsibility,Integer>();
	protected ArrayList<Delegation> delegations = new ArrayList<Delegation>();
	protected ArrayList<Observation> observations = new ArrayList<Observation>();

	public abstract boolean accepts(Agent a, Environment env, Responsibility r);
	public abstract boolean accepts(Environment env, Responsibility r);
	public abstract int getCare(Responsibility res);
	public abstract ArrayList<Responsibility> largestNonConflict(ArrayList<Responsibility> assigned, Agent ag);
	public abstract void setToWorkOn(ArrayList<Responsibility> toWorkOn);
	public abstract void observed();
	public abstract void processFinished();
    public abstract void reason(Node nodet);
    public abstract void finish();
	public abstract ArrayList<Delegation> getDelegations();
	public abstract AgentAction getAction();
	public abstract void sendMsg(String msg);

	public Agent(String _name)
	{
		name = _name;
		x = 0;
		y = 0;
	}

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
		
		return viableLst;
	}

    public String getName() {
        return name;
    }

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	public void setNewLocation(int x2, int y2) 
	{
		x = x2;
		y = y2;
	}

    public void addObservation(Observation observed)
	{
		observations.add(observed);
    }
	
    
	
	
}
