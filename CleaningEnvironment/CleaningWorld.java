package CleaningEnvironment;
import java.awt.Color;
import java.awt.Component;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.nio.file.*;
import java.io.FileWriter;
import java.io.IOException;

import Agents.Agent;
import Agents.ManagerAgent;
import CleaningEnvironment.DirtRecord.Record;
import Environment.Environment;
import Helper.Pair;
import Responsibility.ResponsibilityModel.Node;

public class CleaningWorld extends Environment
{
	String saveLocation;
	ArrayList<Agent> agents = new ArrayList<Agent>();
	HashMap<Agent, Color> agentColours = new HashMap<Agent, Color>();
	HashMap<Pair<Integer, Integer>, Character> zoneSquares = new HashMap<Pair<Integer, Integer>, Character>();
	HashSet<Character> zones = new HashSet<Character>();
	int totalDirt = 0;
	int totalSCDirt = 0;
	
	Random r = new Random();
	
	int currentTime;
	//Variables for naive cleaner
	ArrayDeque<Character> naiveQueue = new ArrayDeque<Character>();
	boolean naive;

	//GUI Panel
	CleaningPanel guiPanel = new CleaningPanel();

	//variables for dirt management
	int dirtEveryX = 5;
	int badDirtEveryX = 15;
	ArrayList<Pair<Integer,Integer>> possibleDirtLocations = new ArrayList<Pair<Integer,Integer>>();
	
	public void setup_agents() 
	{
		int i = 0;
		Collections.shuffle(possibleDirtLocations);
		for (Agent a : agents)
		{
			a.setNewLocation(possibleDirtLocations.get(i).getFirst(),possibleDirtLocations.get(i).getSecond());
			agentColours.put(agents.get(i), new Color(r.nextInt(0xFFFFFF)));
			i++;
		}
		Collections.shuffle(possibleDirtLocations);
	}
	
	private void addDirt(boolean bad, int time) 
	{
		if (possibleDirtLocations.size() > 0)
		{
			Collections.shuffle(possibleDirtLocations);//to ensure that dirt is not evenly distributed as it is cleaned.
			Pair<Integer,Integer> newDirt = possibleDirtLocations.remove(0);
			getCell(newDirt.getFirst(),newDirt.getSecond()).setDirty(bad, time);
			totalDirt++;
			if (bad)
			{
				totalSCDirt++;
			}
		}
	}

	public CleaningWorld(String[] args) 
	{
		String worldFileLocation = "";
		for (int i = 0; i < args.length; i++)
		{
			switch (args[i].toLowerCase()) {
				case "savelocation":
					saveLocation = args[++i];
					break;
				case "worldfile":
					worldFileLocation = args[++i];
					break;
				default:
					break;
			}
		}
		try
		{
			zoneSquares.clear();
			RandomAccessFile fr = new RandomAccessFile(worldFileLocation, "r");
			String line = fr.readLine();
			int width = line.length();
			int height = 1;
			while (fr.readLine() != null) {height++;}
			fr.seek(0);
			
			world = new CleaningWorldCell[height][width];
			
			for (int y = 0; y < world.length; y++)
			{
				line = fr.readLine();
				for (int x = 0; x < world[0].length; x++)
				{
					char zoneID = line.charAt(x);
					zoneSquares.put(new Pair<Integer, Integer>(x,y), zoneID);
					if (zoneID != '0')//0 is reserved for walls
					{
						possibleDirtLocations.add(new Pair<Integer, Integer>(x,y));
						zones.add(zoneID);
					}
					world[y][x] = new CleaningWorldCell(zoneID,1);//Change this back to 5
				}
			}
			fr.close();
			/*for (int y = 0; y < getHeight(); y++)
			{
				for (int x = 0; x < getWidth(); x++)
				{
					System.out.print(getCell(x,y).getZoneID());
				}
				System.out.println();
			}*/
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }

    public CleaningWorld() 
	{

	}

	//Used to get default care values into manager agent
	public void setupManagerAgentHelper(Agent manager)
	{
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				manager.addObservation(new DirtObservation(x,y,getCell(x,y).hasDirt(), getCell(x, y).hasBadDirt()));
			}
		}
		manager.observed();
	}

	private void observeDirt(Agent agent) 
	{
		//Get zone for agent
		char zone = getCell(agent.getX(),agent.getY()).getZoneID();
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				if (getCell(x, y).getZoneID() == zone)
				{
					agent.addObservation(new DirtObservation(x,y,getCell(x,y).hasDirt(), getCell(x, y).hasBadDirt()));
				}
			}
		}
		agent.observed();
	}

	private void clean(Agent ag) 
	{
		int x = ag.getX();
		int y = ag.getY();
		boolean wasSCDirt = getCell(x, y).hasBadDirt();
		if (getCell(x,y).clean(currentTime))
		{
			possibleDirtLocations.add(new Pair<Integer,Integer>(x,y));
			totalDirt--;
			if (wasSCDirt)
			{
				totalSCDirt--;
			}
		}
		ag.addObservation(new DirtObservation(x,y,getCell(x,y).hasDirt(), getCell(x, y).hasBadDirt()));
		ag.observed();
	}
	
	//Change environment percepts
	private void moveAgent(Agent ag, int x, int y)
	{
		if (getCell(x, y).isTraversable())//Needs work for obstructed
		{
			ag.setNewLocation(x,y);
		}
	}

	public HashSet<Character> getZones()
	{
		return zones;
	}

	public int getHeight() 
	{
		return world.length;
	}

	public int getWidth() 
	{
		return world[0].length;
	}

	private CleaningWorldCell getCell(int x, int y) 
	{
		if (y == -1 || x == -1 || x >= world[0].length || y >= world.length)
		{
			System.out.println("Out of bounds (" + x + "," + y + ")");
		}
		return (CleaningWorldCell)world[y][x];
	}

	@Override
	public void applyAction(Agent ag, AgentAction action) 
	{
		switch (action) 
		{
			case aa_clean:
				clean(ag);
				break;
			case aa_movedown:
				moveAgent(ag, ag.getX(), ag.getY() + 1);
				break;
			case aa_moveleft:
				moveAgent(ag, ag.getX() - 1, ag.getY());
				break;
			case aa_moveright:
				moveAgent(ag, ag.getX() + 1, ag.getY());
				break;
			case aa_moveup:
				moveAgent(ag, ag.getX(), ag.getY() - 1);
				break;
			case aa_movedownleft:
				moveAgent(ag, ag.getX() - 1, ag.getY() + 1);
				break;
			case aa_movedownright:
				moveAgent(ag, ag.getX() + 1, ag.getY() + 1);
				break;
			case aa_moveupleft:
				moveAgent(ag, ag.getX() - 1, ag.getY() - 1);
				break;
			case aa_moveupright:
				moveAgent(ag, ag.getX() + 1, ag.getY() - 1);
				break;
			case aa_observedirt:
				observeDirt(ag);
				break;
			case aa_none:
				break;
			default:
				break;
		}
	}

	@Override
	public void saveData() 
	{
		try {
			String filename = "DirtRecord_";
			if (!Files.exists(Paths.get(System.getProperty("user.dir") + saveLocation)))
			{
				Files.createDirectories(Paths.get(System.getProperty("user.dir") + saveLocation));
			}
			int fileNo = 1;
			while (Files.exists(Paths.get(System.getProperty("user.dir") + saveLocation + filename + fileNo + ".csv")))
			{
				fileNo++;
			}
			
			FileWriter fw = new FileWriter(System.getProperty("user.dir") + saveLocation + filename + fileNo + ".csv");
			fw.write("x,y,appeared,cleaned,isBadDirt" + System.lineSeparator());
			for (int x = 0; x < getWidth(); x++)
			{
				for (int y = 0; y < getHeight(); y++)
				{
					DirtRecord dr = getCell(x, y).getDirtRecord();
					for (Record r : dr.records)
					{
						fw.write(x + "," + y + "," + r.toString() + System.lineSeparator());
					}
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Component getGUIPanel() {
		return guiPanel;
	}

	@Override
	public void updateGUIPanel(Node node) {
		guiPanel.setWorld((CleaningWorldCell[][])world, agents, agentColours);
	}

	@Override
	public void nodeUpdate(Node node) {
		currentTime = node.timet;
		agents = node.agents;
		if (currentTime == 0)
		{
			setup_agents();
		}
		else if (currentTime % dirtEveryX == 0)//TODO fix this
		{
			addDirt(currentTime % badDirtEveryX == 0, currentTime);
		}
	}

	public Character getZoneIDForAgent(Agent ag) 
	{
		int x = ag.getX();
		int y = ag.getY();
		for (Pair<Integer, Integer> z : zoneSquares.keySet())
		{
			if (x == z.getFirst() && y == z.getSecond())
			{
				return zoneSquares.get(z);
			}
		}
		return null;
	}

	public Pair<Integer, Integer> getZoneLocation(Character zone) {
		for (Entry<Pair<Integer, Integer>, Character> a : zoneSquares.entrySet())
		{
			if (a.getValue() == zone)
			{
				return a.getKey();
			}
		}
		return null;
	}

	public boolean actionWasTaken(String actionToDo) {
		for (Pair<Agent,String> act : previousActions)
		{
			if (act.getSecond().contentEquals(actionToDo))
			{
				return true;
			}
		}
		return false;
	}

    public boolean stateIsValid(String stateToCheck) 
	{
		switch (stateToCheck)
		{
			case "cdc<5":
				//TODO add actual check
				return getTotalSCDirt() < 5;
		}
        return true;
    }

	public int getTotalDirt() {
		return totalDirt;
	}

    public int getTotalSCDirt() {
        return totalSCDirt;
    }
}
