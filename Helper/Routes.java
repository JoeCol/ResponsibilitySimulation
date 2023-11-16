package Helper;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

import CleaningEnvironment.CleaningWorld;
import Environment.WorldCell;

/*
 *  Precalculates best route to zone start
 */
public class Routes
{
	private int[][] fillSurrounding(WorldCell[][] tmpWorld, int[][] costs, Pair<Integer,Integer> startLocation, HashMap<String, Boolean> checked) 
	{
		boolean done = false;
		while (!done)
		{
			done = true;
			for (int x = 0; x < tmpWorld[0].length; x++)
			{
				for (int y = 0; y < tmpWorld.length; y++)
				{
					if (checked.get(x+","+y) == null)
					{
						if (!tmpWorld[y][x].isTraversable())
						{
							checked.put(x+","+y, true);
						}
						else
						{
							//Get Surrounding Requires surround wall
							int minimum = Integer.MAX_VALUE;
							minimum = Math.min(costs[y + 1][x + 1], minimum);
							minimum = Math.min(costs[y + 1][x + -1], minimum);
							minimum = Math.min(costs[y + 1][x], minimum);
							minimum = Math.min(costs[y][x - 1], minimum);
							minimum = Math.min(costs[y][x + 1], minimum);
							minimum = Math.min(costs[y - 1][x + 1], minimum);
							minimum = Math.min(costs[y - 1][x -1], minimum);
							minimum = Math.min(costs[y - 1][x], minimum);
							if (minimum != Integer.MAX_VALUE && minimum != 0)
							{
								costs[y][x] = minimum + 1;
								checked.put(x+","+y, true);
							}
						}
						//System.out.println(checked.size() + " vs " + (tmpWorld.length * tmpWorld[0].length));
						done = checked.size() == tmpWorld.length * tmpWorld[0].length;
					}
				}
			}
		}
		return costs;
	}

	
	private int[][] generateCosts(WorldCell[][] world, int destX, int destY)
	{
		//arrays are stored y, x
		int[][] costs = new int[world.length][world[0].length];
		for (int[] rows : costs)
		{
			Arrays.fill(rows, Integer.MAX_VALUE);
		}
		costs[destY][destX] = 1;
		HashMap<String, Boolean> checked = new HashMap<String, Boolean>();
		checked.put(destX+","+destY, true);
		costs = fillSurrounding(world, costs, new Pair<Integer,Integer>(destX, destY), checked);
		
		/*for (int[] rows : costs)
		{
			for (int i : rows)
			{
				System.out.print(i + ",");
			}
			System.out.println();
		}*/
		return costs;
	}

	public ArrayDeque<CleaningWorld.AgentAction> actionsToZone(WorldCell[][] world, Pair<Integer,Integer> agentLocation, Pair<Integer,Integer> agentDestination) 
	{
		int[][] costs = generateCosts(world, agentDestination.getFirst(), agentDestination.getSecond());
		
		Pair<Integer,Integer> travXY = agentLocation;
		ArrayDeque<CleaningWorld.AgentAction> toRet = new ArrayDeque<CleaningWorld.AgentAction>();
		
		int[] costNext = new int[9];
		int min = Integer.MAX_VALUE;
		int minIndex = 0;
		while (!samePair(travXY, agentDestination))
		{
			min = Integer.MAX_VALUE;
			minIndex = 0;
			//Get costs of surrounding squares Cost Array is Y X
			costNext[0] = costs[travXY.getSecond() - 1][travXY.getFirst() - 1];//Top left
			costNext[1] = costs[travXY.getSecond() - 1][travXY.getFirst()];//Top middle
			costNext[2] = costs[travXY.getSecond() - 1][travXY.getFirst() + 1];//Top right
			costNext[3] = costs[travXY.getSecond()][travXY.getFirst() - 1];//middle left
			costNext[4] = min; //Middle square
			costNext[5] = costs[travXY.getSecond()][travXY.getFirst() + 1]; //Middle right
			costNext[6] = costs[travXY.getSecond() + 1][travXY.getFirst() - 1]; //bottom left square
			costNext[7] = costs[travXY.getSecond() + 1][travXY.getFirst()]; //bottom middle square;
			costNext[8] = costs[travXY.getSecond() + 1][travXY.getFirst() + 1]; //bottom left square;
			//Find minimum, and its index
			for (int i = 1; i < 9; i = i+2)
			{
				if (costNext[i] < min)
				{
					min = costNext[i];
					minIndex = i;
				}
			}
			for (int i = 0; i < 9; i = i+2)
			{
				if (costNext[i] < min)
				{
					min = costNext[i];
					minIndex = i;
				}
			}
			//choose action
			switch(minIndex)
			{
			case 0:
				toRet.add(CleaningWorld.AgentAction.aa_moveupleft);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() - 1, travXY.getSecond() - 1);
				break;
			case 1:
				toRet.add(CleaningWorld.AgentAction.aa_moveup);
				travXY = new Pair<Integer,Integer>(travXY.getFirst(), travXY.getSecond() - 1);
				break;
			case 2:
				toRet.add(CleaningWorld.AgentAction.aa_moveupright);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() + 1, travXY.getSecond() - 1);
				break;
			case 3:
				toRet.add(CleaningWorld.AgentAction.aa_moveleft);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() - 1, travXY.getSecond());
				break;
			/*case 4:
				travXY = new IntegerPair<Integer,Integer>(travXY.getFirst(), travXY.getSecond());
				break;*/
			case 5:
				toRet.add(CleaningWorld.AgentAction.aa_moveright);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() + 1, travXY.getSecond());
				break;
			case 6:
				toRet.add(CleaningWorld.AgentAction.aa_movedownleft);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() - 1, travXY.getSecond() + 1);
				break;
			case 7:
				toRet.add(CleaningWorld.AgentAction.aa_movedown);
				travXY = new Pair<Integer,Integer>(travXY.getFirst(), travXY.getSecond() + 1);
				break;
			case 8:
				toRet.add(CleaningWorld.AgentAction.aa_movedownright);
				travXY = new Pair<Integer,Integer>(travXY.getFirst() + 1, travXY.getSecond() + 1);
				break;
			}
		}
		
		return toRet;
	}


	private boolean samePair(Pair<Integer, Integer> travXY, Pair<Integer, Integer> agentDestination) {
		return travXY.getFirst() == agentDestination.getFirst() && travXY.getSecond() == agentDestination.getSecond();
	}

}
