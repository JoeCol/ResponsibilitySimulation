package SREnvironment;

import Environment.WorldCell;

public class SRWorldCell extends WorldCell
{
    private char zoneID;
	private boolean isOnFire;
	private int timePutOut = 0;
	private boolean isEscape = false;
	
	public boolean isOnFire() {
		return isOnFire;
	}

	public void setOnFire() 
	{
		this.isOnFire = true;
	}

	public void putOutFire(int time)
	{
		isOnFire = false;
		timePutOut = time;
	}

	public boolean wasOnFire()
	{
		return timePutOut != 0;
	}

	public int timePutOut()
	{
		return timePutOut;
	}

	public char getZoneID() {
		return zoneID;
	}

    public SRWorldCell() 
	{
		super(false);
		zoneID = '0';
	}
	
	public SRWorldCell(char zone)
	{
		super(zone != '0');
		zoneID = zone;
	}

	public boolean isEscape() 
	{
		return isEscape;
	}
}