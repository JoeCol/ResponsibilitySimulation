package SREnvironment;

import Environment.WorldCell;

public class SRWorldCell extends WorldCell
{
    private char zoneID;
	private boolean isOnFire;
	public boolean isOnFire() {
		return isOnFire;
	}

	public void setOnFire(boolean isOnFire) {
		this.isOnFire = isOnFire;
	}

	private boolean hasHuman;

	public boolean isHasHuman() {
		return hasHuman;
	}

	public void setHasHuman(boolean hasHuman) {
		this.hasHuman = hasHuman;
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
}