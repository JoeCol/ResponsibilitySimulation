package SREnvironment;
import java.awt.Color;
import java.awt.Component;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Map.Entry;

import java.nio.file.*;
import java.io.FileWriter;
import java.io.IOException;

import Agents.Agent;
import CleaningEnvironment.DirtObservation;
import Environment.Environment;
import Helper.Pair;
import Responsibility.ResponsibilityModel.Node;

/*
 * TODO: Add fires that block agents being rescued to allow moral responsibility to have an effect
 */
public class SRWorld extends Environment
{
	String saveLocation;
	ArrayList<Agent> agents = new ArrayList<Agent>();
	ArrayList<Human> humans = new ArrayList<Human>();
	HashMap<Agent, Human> pickedUp = new HashMap<Agent, Human>();
	HashMap<Agent, Color> agentColours = new HashMap<Agent, Color>();
	HashMap<Pair<Integer, Integer>, Character> zoneSquares = new HashMap<Pair<Integer, Integer>, Character>();
	HashSet<Character> zones = new HashSet<Character>();
	
	Random r = new Random();
	
	int currentTime;
	int timeToRescue;
	int timeToPutoutFire;
	int totalTime;

	int totalToRescue = 3;
	int totalFire = 3;

	//GUI Panel
	SRPanel guiPanel = new SRPanel();

	//Variables for finding locations for fire/human/agents
	ArrayList<Pair<Integer,Integer>> possibleLocations = new ArrayList<Pair<Integer,Integer>>();
	
	public void setup_agents() 
	{
		int i = 0;
		Collections.shuffle(possibleLocations);
		for (Agent a : agents)
		{
			a.setNewLocation(possibleLocations.get(i).getFirst(),possibleLocations.get(i).getSecond());
			agentColours.put(agents.get(i), new Color(r.nextInt(0xFFFFFF)));
			i++;
		}
		Collections.shuffle(possibleLocations);
	}
	
	private void addFireAndHumans() 
	{
		Collections.shuffle(possibleLocations);
		for (int fire = 0; fire < totalFire; fire++)
		{
			Pair<Integer,Integer> newPos = possibleLocations.remove(0);
			getCell(newPos.getFirst(),newPos.getSecond()).setOnFire();
		}

		for (int human = 0; human < totalToRescue; human++)
		{
			Pair<Integer,Integer> newPos = possibleLocations.remove(0);
			Human h = new Human(newPos.getFirst(),newPos.getSecond());
			humans.add(h);
		}
	}

	public SRWorld(String[] args) 
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
				case "totaltorescue":
					totalToRescue = Integer.parseInt(args[++i]);
					break;
				case "totalfire":
					totalFire = Integer.parseInt(args[++i]);
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
			
			world = new SRWorldCell[height][width];
			
			for (int y = 0; y < world.length; y++)
			{
				line = fr.readLine();
				for (int x = 0; x < world[0].length; x++)
				{
					char zoneID = line.charAt(x);
					zoneSquares.put(new Pair<Integer, Integer>(x,y), zoneID);
					if (zoneID != '0')//0 is reserved for walls
					{
						possibleLocations.add(new Pair<Integer, Integer>(x,y));
						zones.add(zoneID);
					}
					world[y][x] = new SRWorldCell(zoneID);//Change this back to 5
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
		addFireAndHumans();
    }

    public SRWorld() 
	{

	}
	
	//Change environment percepts
	private void moveAgent(Agent ag, int x, int y)
	{
		if (getCell(x, y).isTraversable())//Needs work for obstructed
		{
			ag.setNewLocation(x,y);
			if (pickedUp.containsKey(ag))
			{
				pickedUp.get(ag).setNewPos(x,y);
			}
		}
	}

	private void observeRoom(Agent ag)
	{
		Character zone = getZoneIDForAgent(ag);
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				if (getCell(x, y).getZoneID() == zone && getCell(x, y).isOnFire())
				{
					ag.addObservation(new FireObservation(x,y));
				}
				else if (getCell(x, y).getZoneID() == zone && getHumanAt(x, y) != null)
				{
					ag.addObservation(new HumanObservation(x,y));
				}
			}
		}
		ag.observed();
	}

	private void pickUpHuman(Agent ag, int x, int y)
	{
		Human h = getHumanAt(x, y);
		if (h != null)
		{
			h.setTimePickedUp(currentTime);
			pickedUp.put(ag, h);
		}
	}

	private void putDownHuman(Agent ag, int x, int y)
	{
		Human h = pickedUp.get(ag);
		if (h != null)
		{
			if (getCell(x, y).isEscape())
			{
				h.setTimeRescued(currentTime);
			}
			pickedUp.remove(ag);
		}
	}

	private void putOutFire(int x, int y)
	{
		getCell(x, y).putOutFire(currentTime);
	}

	public Human getHumanAt(int x, int y)
	{
		for (Human h : humans)
		{
			if (h.x == x && h.y == y)
			{
				return h;
			}
		}
		return null;
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

	private SRWorldCell getCell(int x, int y) 
	{
		if (y == -1 || x == -1 || x >= world[0].length || y >= world.length)
		{
			System.out.println("Out of bounds (" + x + "," + y + ")");
		}
		return (SRWorldCell)world[y][x];
	}

	@Override
	public void applyAction(Agent ag, AgentAction action) 
	{
		switch (action) 
		{
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
			case aa_pickup:
				pickUpHuman(ag, ag.getX(), ag.getY());
				break;
			case aa_putdown:
				putDownHuman(ag, ag.getX(), ag.getY());
				break;
			case aa_putoutfire:
				putOutFire(ag.getX(), ag.getY());
				break;
			case aa_observeroom:
				observeRoom(ag);
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
			String filename = "SRRecord_";
			if (!Files.exists(Paths.get(saveLocation)))
			{
				Files.createDirectories(Paths.get(saveLocation));
			}
			int fileNo = 1;
			while (Files.exists(Paths.get(saveLocation + filename + fileNo + ".csv")))
			{
				fileNo++;
			}
			
			FileWriter fw = new FileWriter(saveLocation + filename + fileNo + ".csv");
			fw.write("type,length,wait" + System.lineSeparator());
			for (Human h : humans)
			{
				fw.write("human," + h.getRescueLength() + "," + h.getTimePickedUp() + System.lineSeparator());
			}
			for (int x = 0; x < getWidth(); x++)
			{
				for (int y = 0; y < getHeight(); y++)
				{
					if (getCell(x, y).wasOnFire())
					{
						fw.write("fire," + getCell(x, y).timePutOut() + ",0" + System.lineSeparator());
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
		guiPanel.setWorld((SRWorldCell[][])world, agents, humans, agentColours);
	}

	@Override
	public void nodeUpdate(Node node) {
		currentTime = node.timet;
		agents = node.agents;
		if (currentTime == 0)
		{
			setup_agents();
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
        return true;
    }
}
