package Environment;
public abstract class WorldCell
{
	
	protected boolean traversable;
	protected boolean occupied = false;
	
	public WorldCell(boolean traversable) 
	{
		this.traversable = traversable;
	}

	public boolean isOccupied() {
		return !traversable || occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isTraversable() {
		return traversable;
	}
}
