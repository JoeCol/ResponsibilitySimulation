public class WorldCell
{
	public enum DirtLevel {dl_dirt,dl_badDirt,dl_clear};
	private DirtLevel dirtLevel = DirtLevel.dl_clear;
	private boolean traversable;
	private boolean occupied = false;
	private char zoneID;

	public boolean hasDirt() 
	{
		return dirtLevel != DirtLevel.dl_clear;
	}
	
	public boolean hasBadDirt()
	{
		return dirtLevel == DirtLevel.dl_badDirt;
	}
	
	public void setDirty(boolean bad)
	{
		dirtLevel = bad ? DirtLevel.dl_badDirt : DirtLevel.dl_dirt;
	}
	
	public void clean()
	{
		dirtLevel = DirtLevel.dl_clear;
	}

	public boolean isOccupied() {
		return !traversable || occupied;
	}

	public WorldCell() 
	{
		super();
		traversable = false;
		zoneID = '0';
	}
	
	public WorldCell(char zone)
	{
		zoneID = zone;
		traversable = zone != '0';
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isTraversable() {
		return traversable;
	}

	public char getZoneID() {
		return zoneID;
	}
}
