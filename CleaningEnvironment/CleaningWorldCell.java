package CleaningEnvironment;

import Environment.WorldCell;

public class CleaningWorldCell extends WorldCell
{
    public enum DirtLevel {dl_dirt,dl_badDirt,dl_clear};
	private DirtLevel dirtLevel = DirtLevel.dl_clear;
    private int appeared;
	private int cleaned;
	private int cleaningLength;
	private int remainingCleaningLeft;
    private char zoneID;
	private DirtRecord dirtRecord = new DirtRecord();

    public boolean hasDirt() 
	{
		return dirtLevel != DirtLevel.dl_clear;
	}
	
	public boolean hasBadDirt()
	{
		return dirtLevel == DirtLevel.dl_badDirt;
	}
	
	public void setDirty(boolean bad, int time)
	{
		dirtLevel = bad ? DirtLevel.dl_badDirt : DirtLevel.dl_dirt;
		appeared = time;
		remainingCleaningLeft = cleaningLength;
	}
	
	public boolean clean(int time)
	{
		remainingCleaningLeft--;
		if (remainingCleaningLeft == 0)
		{
			dirtRecord.addRecord(appeared,time,dirtLevel==DirtLevel.dl_badDirt);
			dirtLevel = DirtLevel.dl_clear;
			cleaned = time;
			return true;
		}
		return false;
	}

	public int timeAlive()
	{
		return appeared - cleaned;
	}

	public char getZoneID() {
		return zoneID;
	}

    public CleaningWorldCell(int timeToClean) 
	{
		super(false);
		zoneID = '0';
		cleaningLength = timeToClean;
	}
	
	public CleaningWorldCell(char zone, int timeToClean)
	{
		super(zone != '0');
		zoneID = zone;
		cleaningLength = timeToClean;
	}

    public DirtRecord getDirtRecord() 
	{
        return dirtRecord;
    }
}