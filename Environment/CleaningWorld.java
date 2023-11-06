package Environment;
import java.awt.Color;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Random;

import Message;
import Pair;
import Routes;
import Settings;
import Agents.Agent;
import Agents.CleanerAgent;
import Agents.ManagerAgent;
import Agents.NaiveAgent;
import Environment.WorldCell.DirtLevel;
import Responsibility.SetupResponsibilities;

interface UpdateToWorld{
	void worldUpdate(int time, int dirt, int badDirt, WorldCell[][] world, HashMap<Agent, Pair<Integer,Integer>> agentLocations, HashMap<Agent, Color> agentColours);
}

public class CleaningWorld extends Environment
{
	public enum AgentAction {aa_moveup, aa_movedown, aa_moveright, aa_moveleft, aa_clean, aa_observedirt, aa_moveupleft, aa_moveupright, aa_movedownleft, aa_movedownright, aa_finish, aa_none}
	Routes routeToZones = new Routes();
	WorldCell[][] world;
	int remainingSteps = 100;
	int totalTime = 100;
	int simSpeed = 350;
	Settings currentSettings;
	String saveLocation;
	ArrayDeque<Message> msgs = new ArrayDeque<Message>();
	ArrayList<UpdateToWorld> worldListeners = new ArrayList<UpdateToWorld>();
	ArrayList<Agent> agents = new ArrayList<Agent>();
	HashMap<Agent, Color> agentColours = new HashMap<Agent, Color>();
	HashMap<Character, ArrayList<Pair<Integer, Integer>>> zoneSquares = new HashMap<Character, ArrayList<Pair<Integer, Integer>>>();
	
	HashMap<Agent, Pair<Integer,Integer>> agentLocations = new HashMap<Agent, Pair<Integer,Integer>>();
	HashMap<Agent, String> workingOn = new HashMap<Agent, String>();
	HashMap<Agent, HashMap<String, Integer>> agentCares = new HashMap<Agent, HashMap<String,Integer>>();
	
	Random r = new Random();
	private HashMap<Agent, Integer> cleanCountdown = new HashMap<Agent, Integer>();
	private Integer cleanLength = 30;
	//Variables for naive cleaner
	ArrayDeque<Character> naiveQueue = new ArrayDeque<Character>();
	boolean naive;

	//variables for dirt management
	Random dirtR = new Random();
	Random badDirtR = new Random();
	int totalDirt = 0;
	int totalBadDirt = 0;
	ArrayList<Pair<Integer,Integer>> possibleDirtLocations = new ArrayList<Pair<Integer,Integer>>();
	DirtRecord dirtRecord = new DirtRecord();
	
	public Settings getSettings()
	{
		return currentSettings;
	}
	
	public void addWorldListeners(UpdateToWorld u)
	{
		worldListeners.add(u);
	}
	
	public void setup_agents(boolean naive) 
	{
		//Randomly position agents
		this.naive = naive;
		if (naive)
		{
			agents.add(new NaiveAgent("Naive 1",routeToZones,zoneSquares));
			agents.add(new NaiveAgent("Naive 2",routeToZones,zoneSquares));
			agents.add(new NaiveAgent("Naive 3",routeToZones,zoneSquares));
		}
		else
		{
			agents.add(new ManagerAgent("Manager",routeToZones,zoneSquares));
			agents.add(new CleanerAgent("Cleaner 1",routeToZones,zoneSquares));
			agents.add(new CleanerAgent("Cleaner 2",routeToZones,zoneSquares));
			
			SetupResponsibilities.generateDelegatedRes(zoneSquares.size() - 1);
			msgs.add(new Message("initial", "Manager", "assignment", SetupResponsibilities.setupManagerResponsiblities(zoneSquares.size() - 1)));
			msgs.add(new Message("initial", "Cleaner 1", "assignment", SetupResponsibilities.setupCleanerResponsiblity()));
			msgs.add(new Message("initial", "Cleaner 2", "assignment", SetupResponsibilities.setupCleanerResponsiblity()));
		}
		agentColours.put(agents.get(0), new Color(r.nextInt(0xFFFFFF)));
		agentColours.put(agents.get(1), new Color(r.nextInt(0xFFFFFF)));
		agentColours.put(agents.get(2), new Color(r.nextInt(0xFFFFFF)));
		int x = 1;
		int y = 1;
		for (Agent a : agents)
		{
			agentLocations.put(a, new Pair<Integer,Integer>(x++, y));
		}
	}
	
	private void addDirt(boolean bad, int time) 
	{
		if (possibleDirtLocations.size() > 0)
		{
			Collections.shuffle(possibleDirtLocations);//to ensure that dirt is not evenly distributed as it is cleaned.
			Pair<Integer,Integer> newDirt = possibleDirtLocations.remove(0);
			getCell(newDirt.getFirst(),newDirt.getSecond()).setDirty(bad, time);
			totalDirt++;
			if (bad) {totalBadDirt++;}
			dirtRecord.addRecord(remainingSteps, totalDirt, totalBadDirt);
		}
	}

	public CleaningWorld(int simSteps, int dirtInt, int badDirtInt, String worldLoc, String saveLoc, int _simSpeed)
	{
		currentSettings = new Settings(0, 0, simSteps, dirtInt, badDirtInt, worldLoc);
		simSpeed = _simSpeed;
		totalTime = simSteps;
		saveLocation = saveLoc;
		try
		{
			zoneSquares.clear();
			remainingSteps = currentSettings.getSimulationSteps();
			RandomAccessFile fr = new RandomAccessFile(currentSettings.getWorldFileLocation(), "r");
			String line = fr.readLine();
			currentSettings.setWidthOfMap(line.length());
			int height = 1;
			while (fr.readLine() != null) {height++;}
			currentSettings.setHeightOfMap(height);
			fr.seek(0);
			
			world = new WorldCell[currentSettings.getHeightOfMap()][currentSettings.getWidthOfMap()];
			
			for (int y = 0; y < world.length; y++)
			{
				line = fr.readLine();
				for (int x = 0; x < world[0].length; x++)
				{
					char zoneID = line.charAt(x);
					zoneSquares.putIfAbsent(zoneID, new ArrayList<Pair<Integer, Integer>>());
					zoneSquares.get(zoneID).add(new Pair<Integer, Integer>(x,y));
					if (zoneID != '0')//0 is reserved for walls
					{
						possibleDirtLocations.add(new Pair<Integer, Integer>(x,y));
					}
					world[y][x] = new WorldCell(zoneID);
				}
			}
			/*for (int y = 0; y < getHeight(); y++)
			{
				for (int x = 0; x < getWidth(); x++)
				{
					System.out.print(getCell(x,y).getZoneID());
				}
				System.out.println();
			}*/
			//Add zones for naive cleaner
			for (char zoneID : zoneSquares.keySet())
			{
				naiveQueue.add(zoneID);
			}
			naiveQueue.remove('0');//Remove wall room
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void observeDirt(Agent agent) 
	{
		//Get zone for agent
		Pair<Integer,Integer> l = agentLocations.get(agent);
		char zone = getCell(l.getFirst(),l.getSecond()).getZoneID();
		boolean hasDirt = false;
		boolean hasBadDirt = false;
		//System.out.println(zone + " observed");
		for (int i = 0; i < world.length; i++)
		{
			WorldCell[] row = world[i];
			for (int j = 0; j < row.length; j++)
			{
				WorldCell cell = row[j];
				if (cell.getZoneID() == zone)
				{
					if (cell.hasBadDirt())
					{
						hasDirt = true;
						hasBadDirt = true;
						//System.out.println(zone + " has bad dirt at " + j + " " + i);
						break;
					}
					else if (cell.hasDirt())
					{
						hasDirt = true;
						//System.out.println(zone + " has dirt at " + j + " " + i);
					}
				}
			}
		}
		if (hasBadDirt)
		{
			agent.observed(zone,WorldCell.DirtLevel.dl_badDirt);
		}
		else if (hasDirt)
		{
			agent.observed(zone,WorldCell.DirtLevel.dl_dirt);
		}
		else
		{
			agent.observed(zone,WorldCell.DirtLevel.dl_clear);
		}
	}

	private void clean(int x, int y, int time) 
	{
		if (getCell(x,y).hasDirt())
		{
			boolean badDirt = false;
			if (getCell(x,y).hasBadDirt())
			{
				totalBadDirt--;
				badDirt = true;
			}
			totalDirt--;
			possibleDirtLocations.add(new Pair<Integer,Integer>(x,y));
			dirtRecord.addRecord(remainingSteps, totalDirt, totalBadDirt);
			getCell(x, y).clean(time);
			dirtRecord.addTimeRecord(badDirt, getCell(x, y).timeAlive());
		}
	}
	
	//Change environment percepts
	private void moveAgent(Agent ag, int x, int y)
	{
		agentLocations.put(ag, new Pair<Integer,Integer>(x,y));		
	}

	private Agent getAgent(String s)
	{
		for (Agent a : agents)
		{
			if (a.getName().equals(s))
			{
				return a;
			}
		}
		return null;
	}

	public void start() 
	{
		for (UpdateToWorld u : worldListeners)
		{
			u.worldUpdate(remainingSteps, totalDirt, totalBadDirt, world, agentLocations, agentColours);
		}
		while (remainingSteps > 0)
		{
			while (!msgs.isEmpty())
			{
				Message m = msgs.pop();
				if (!m.getReceiver().equals("initial"))
				{
					Agent a = getAgent(m.getReceiver());
					a.receiveMessage(m);
				}
				else
				{
					//System.out.println("Initial has received:" + m.content + " from " + m.sender);
				}
			}
			for (Agent a : agents)
			{
				Pair<Integer,Integer> agentLocation = agentLocations.get(a);
				a.updateLocation(agentLocation, world);
				if (naive)
				{
					a.updateNaiveList(naiveQueue);
				}
				a.reason();
				AgentAction action = a.getAction();
				boolean actionFinished = true;
				switch (action)
				{
					case aa_clean:
						actionFinished = false;
						if (cleanCountdown.containsKey(a) && cleanCountdown.get(a) == 0)
						{
							clean(agentLocation.getFirst(), agentLocation.getSecond(),remainingSteps); 
							cleanCountdown.remove(a);
							actionFinished = true;
						}
						else if (cleanCountdown.containsKey(a))
						{
							cleanCountdown.put(a,cleanCountdown.get(a) - 1);
						}
						else
						{
							cleanCountdown.put(a,cleanLength);
						}
						break;
					case aa_movedown:
						moveAgent(a, agentLocation.getFirst(), agentLocation.getSecond() + 1);
						break;
					case aa_moveleft:
						moveAgent(a, agentLocation.getFirst() - 1, agentLocation.getSecond());
						break;
					case aa_moveright:
						moveAgent(a, agentLocation.getFirst() + 1, agentLocation.getSecond());
						break;
					case aa_moveup:
						moveAgent(a, agentLocation.getFirst(), agentLocation.getSecond() - 1);
						break;
					case aa_movedownleft:
						moveAgent(a, agentLocation.getFirst() - 1, agentLocation.getSecond() + 1);
						break;
					case aa_movedownright:
						moveAgent(a, agentLocation.getFirst() + 1, agentLocation.getSecond() + 1);
						break;
					case aa_moveupleft:
						moveAgent(a, agentLocation.getFirst() - 1, agentLocation.getSecond() - 1);
						break;
					case aa_moveupright:
						moveAgent(a, agentLocation.getFirst() + 1, agentLocation.getSecond() - 1);
						break;
					case aa_observedirt:
						observeDirt(a);
						break;
					case aa_finish:
						a.finish();
						break;
					case aa_none:
						break;
					default:
						break;
				}
				a.actionFinished(actionFinished);
				while (a.hasMessageToSend())
				{
					Message m = a.getNextMessage();
					msgs.add(m);
				}
				a.processFinished();
			}
			
			//Do dirt step
			//Do dirt step
			int dirtNum = dirtR.nextInt(currentSettings.getDirtInterval());
			int badDirtNum = badDirtR.nextInt(currentSettings.getBadDirtInterval());
			if (dirtNum == 0)
			{
				addDirt(false,remainingSteps);
			}
			if (badDirtNum == 0)
			{
				addDirt(true,remainingSteps);
			}
			for (UpdateToWorld u : worldListeners)
			{
				u.worldUpdate(remainingSteps, totalDirt, totalBadDirt, world, agentLocations, agentColours);
			}
			remainingSteps--;

			//Simulation speed
			if (simSpeed > 0)
			{
				try {
					Thread.sleep(simSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		save();
		System.exit(0);
	}

	public int getHeight() 
	{
		return world.length;
	}

	public int getWidth() 
	{
		return world[0].length;
	}

	private WorldCell getCell(int x, int y) 
	{
		if (y == -1 || x == -1 || x >= world[0].length || y >= world.length)
		{
			System.out.println("Out of bounds (" + x + "," + y + ")");
		}
		return world[y][x];
	}

	private void save() 
	{
		dirtRecord.saveToFile(saveLocation,totalTime);
	}
}
