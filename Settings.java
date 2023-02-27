import java.io.Serializable;

public class Settings implements Serializable
{
	private static final long serialVersionUID = -4836286726986083154L;
	String worldFileLocation;
	int heightOfMap;
	int widthOfMap;
	int simulationSteps;
	int dirtInterval;
	int badDirtInterval;
	
	public Settings(int height, int width, int simSteps, int dirtInt, int badDirtInt, String worldLoc)
	{
		heightOfMap = height;
		widthOfMap = width;
		simulationSteps = simSteps;
		dirtInterval = dirtInt;
		badDirtInterval = badDirtInt;
		worldFileLocation = worldLoc;
	}
	
	public int getSimulationSteps() {
		return simulationSteps;
	}

	public void setSimulationSteps(int simulationSteps) {
		this.simulationSteps = simulationSteps;
	}

	public int getDirtInterval() {
		return dirtInterval;
	}

	public void setDirtInterval(int dirtInterval) {
		this.dirtInterval = dirtInterval;
	}

	public int getBadDirtInterval() {
		return badDirtInterval;
	}

	public void setBadDirtInterval(int badDirtInterval) {
		this.badDirtInterval = badDirtInterval;
	}

	public int getHeightOfMap() {
		return heightOfMap;
	}

	public void setHeightOfMap(int heightOfMap) {
		this.heightOfMap = heightOfMap;
	}

	public int getWidthOfMap() {
		return widthOfMap;
	}

	public void setWidthOfMap(int widthOfMap) {
		this.widthOfMap = widthOfMap;
	}

	public String getWorldFileLocation() {
		return worldFileLocation;
	}

	public void setWorldFileLocation(String text) {
		this.worldFileLocation = text;
		
	}

}
