package CleaningEnvironment;
import java.awt.Color;
import java.awt.Component;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Random;
import java.nio.file.*;
import java.io.FileWriter;
import java.io.IOException;

import Agents.Agent;
import CleaningEnvironment.DirtRecord.Record;
import Environment.Environment;
import Environment.WorldCell;
import Helper.Pair;
import Responsibility.ResponsibilityModel.Node;

public class CleaningWorld extends Environment
{
	String saveLocation;
	ArrayList<Agent> agents = new ArrayList<Agent>();
	HashMap<Agent, Color> agentColours = new HashMap<Agent, Color>();
	HashMap<Character, ArrayList<Pair<Integer, Integer>>> zoneSquares = new HashMap<Character, ArrayList<Pair<Integer, Integer>>>();
	
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
		}
	}

	public CleaningWorld(String[] args) 
	{
		String worldFileLocation = "";
		for (int i = 0; i < args.length; i++)
		{
			switch (args[i]) {
				case "saveLoca":
					saveLocation = args[++i];
					break;
				case "worldFile":
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
			
			world = new WorldCell[height][width];
			
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
					world[y][x] = new CleaningWorldCell(zoneID);
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

	private void observeDirt(Agent agent) 
	{
		//Get zone for agent
		char zone = getCell(agent.getX(),agent.getY()).getZoneID();
		ArrayList<DirtObservation> observed = new ArrayList<DirtObservation>();
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				if (getCell(x, y).getZoneID() == zone)
				{
					if (getCell(x, y).hasDirt())
					{
						observed.add(new DirtObservation(x,y,getCell(x, y).hasBadDirt()));
					}
				}
			}
		}
		agent.addObservations(observed);
	}

	private void clean(Agent ag) 
	{
		if (getCell(ag.getX(),ag.getY()).clean(currentTime))
		{
			possibleDirtLocations.add(new Pair<Integer,Integer>(ag.getX(),ag.getY()));
		}
	}
	
	//Change environment percepts
	private void moveAgent(Agent ag, int x, int y)
	{
		if (getCell(x, y).isTraversable())//Needs work for obstructed
		{
			ag.setNewLocation(x,y);
		}
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
	public void saveData(String saveLoc) 
	{
		try {
			String filename = "DirtRecord_";
			if (!Files.exists(Paths.get(saveLoc)))
			{
				Files.createDirectories(Paths.get(saveLoc));
			}
			int fileNo = 1;
			while (Files.exists(Paths.get(saveLoc + filename + fileNo + ".csv")))
			{
				fileNo++;
			}
			
			FileWriter fw = new FileWriter(saveLoc + filename + fileNo + ".csv");
			fw.write("x,y,appeared,cleaned,isBadDirt" + System.lineSeparator());
			for (int x = 0; x < getWidth(); x++)
			{
				for (int y = 0; y < getHeight(); y++)
				{
					DirtRecord dr = getCell(x, y).getDirtRecord();
					for (Record r : dr.records)
					{
						fw.write(x + "," + y + "," + r.toString());
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
}
