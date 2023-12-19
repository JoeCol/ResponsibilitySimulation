package CleaningEnvironment;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.ArrayList;

import Agents.Agent;

public class CleaningPanel extends JPanel
{
	private static final long serialVersionUID = -9139436515145815500L;
	CleaningWorldCell[][] world;
	private HashMap<Agent, Color> colours;
	private ArrayList<Agent> agents;
	
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
					//g.drawString("("+ x + "," + y + ") Zone:" + world[y][x].getZoneID(), 2+(x * widthOfCell), (g.getFontMetrics().getHeight() * 2) + (y * heightOfCell));
				}
			}
			
			for (Agent ag : agents)
			{
				int x = ag.getX();
				int y = ag.getY();
				int tx = 1+(x * widthOfCell);
				int ty = g.getFontMetrics().getHeight() + (y * heightOfCell);
				g.setColor(colours.get(ag));
				g.fillOval(tx, ty, widthOfCell, heightOfCell);
				g.setColor(Color.BLACK);
				g.drawString(ag.getName(), tx, ty + (heightOfCell / 2));

			}
		}
	}

	public void setWorld(CleaningWorldCell[][] world2, ArrayList<Agent> _agents,
			HashMap<Agent, Color> agentColours) {
		world = world2;
		agents = _agents;
		colours = agentColours;
		invalidate();
		repaint();	
		
	}

}
