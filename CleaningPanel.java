import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;

import Agents.Agent;
import Environment.WorldCell;

public class CleaningPanel extends JPanel
{
	private static final long serialVersionUID = -9139436515145815500L;
	WorldCell[][] world;
	private HashMap<Agent, Pair<Integer,Integer>> locations;
	private HashMap<Agent, Color> colours;
	
	public CleaningPanel()
	{
		setDoubleBuffered(true);
		//Grid Layout goes for y number of rows, then x number of columns
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawString(java.time.ZonedDateTime.now().toString(),5,g.getFontMetrics().getHeight() - 5);
		g.setColor(Color.BLACK);
		if (world != null)
		{
			int widthOfCell = getWidth() / world[0].length;
			int heightOfCell = (getHeight() - g.getFontMetrics().getHeight()) / world.length;
			
			for (int x = 0; x < world[0].length; x++)
			{
				for (int y = 0; y < world.length; y++)
				{
					if (!world[y][x].isTraversable())// Wall
					{
						g.setColor(Color.BLACK);
						g.fillRect(1+(x * widthOfCell), g.getFontMetrics().getHeight() + (y * heightOfCell), widthOfCell, heightOfCell);
					}
					else if (!world[y][x].hasDirt()) //No dirt
					{
						g.setColor(Color.white);
						g.fillRect(1+(x * widthOfCell), g.getFontMetrics().getHeight() + (y * heightOfCell), widthOfCell, heightOfCell);
					}
					else // dirt
					{
						if (world[y][x].hasBadDirt())
						{
							g.setColor(Color.red);
						}
						else
						{
							g.setColor(Color.lightGray);
						}
						g.fillRect(1+(x * widthOfCell), g.getFontMetrics().getHeight() + (y * heightOfCell), widthOfCell, heightOfCell);
					}
					g.setColor(Color.black);
					g.drawString("("+ x + "," + y + ") Zone:" + world[y][x].getZoneID(), 2+(x * widthOfCell), (g.getFontMetrics().getHeight() * 2) + (y * heightOfCell));
				}
			}
			
			for (Agent ag : locations.keySet())
			{
				Pair<Integer,Integer> l = locations.get(ag);
				int x = l.getFirst();
				int y = l.getSecond();
				int tx = 1+(x * widthOfCell);
				int ty = g.getFontMetrics().getHeight() + (y * heightOfCell);
				g.setColor(colours.get(ag));
				g.fillOval(tx, ty, widthOfCell, heightOfCell);
				g.setColor(Color.BLACK);
				g.drawString(ag.getName(), tx, ty + (heightOfCell / 2));

			}
		}
	}

	public void setWorld(WorldCell[][] world2, HashMap<Agent, Pair<Integer,Integer>> agentLocations,
			HashMap<Agent, Color> agentColours) {
		world = world2;
		locations = agentLocations;
		colours = agentColours;
		invalidate();
		repaint();	
		
	}

}
