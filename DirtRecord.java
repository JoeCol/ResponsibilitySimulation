import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/****
 * 
 * @author Joe Collenette
 *
 * Records the level of dirt over time and saves to a csv file
 */
public class DirtRecord {
	public class Record {
		int time;
		int dirtLevel;
		int badDirtLevel;
		
		public Record(int time, int dirtLevel, int badDirtLevel) {
			super();
			this.time = time;
			this.dirtLevel = dirtLevel;
			this.badDirtLevel = badDirtLevel;
		}
		
		public int getTime() {
			return time;
		}
		
		public int getDirtLevel() {
			return dirtLevel;
		}
		
		public int getBadDirtLevel() {
			return badDirtLevel;
		}
		
		public String toString()
		{
			return time + "," + dirtLevel + "," + badDirtLevel;
		}
		
	}

	public class TimeRecord {
		int timeAlive;
		boolean badDirt;
		
		public TimeRecord(boolean badDirt, int timeAlive) {
			super();
			this.timeAlive = timeAlive;
			this.badDirt = badDirt;
		}

		public String toString()
		{
			if (badDirt)
			{
				return "true," + timeAlive;
			}
			else
			{
				return "false," + timeAlive; 
			}
		}
		
	}

	ArrayList<Record> allRecords = new ArrayList<Record>(); 
	ArrayList<TimeRecord> timeRecords = new ArrayList<TimeRecord>(); 

	public void addRecord(int time, int dirtLevel, int badDirtLevel)
	{
		allRecords.add(new Record(time, dirtLevel, badDirtLevel));
	}
	
	public void saveToFile(String directory)
	{
		try {
			String filename = "DirtLevels_";
			String timeName = "AliveTime_";
			if (!Files.exists(Paths.get(directory)))
			{
				Files.createDirectories(Paths.get(directory));
			}
			int fileNo = 1;
			while (Files.exists(Paths.get(directory + filename + fileNo + ".csv")))
			{
				fileNo++;
			}
			
			FileWriter fw = new FileWriter(directory + filename + fileNo + ".csv");
			fw.write("time,dirtLevel,badDirtLevel" + System.lineSeparator());
			for (Record r : allRecords)
			{
				fw.write(r.toString() + System.lineSeparator());
			}
			fw.flush();
			fw.close();

			fw = new FileWriter(directory + timeName + fileNo + ".csv");
			fw.write("badDirt,timeAlive" + System.lineSeparator());
			for (TimeRecord r : timeRecords)
			{
				fw.write(r.toString() + System.lineSeparator());
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void clear()
	{
		allRecords.clear();
	}

	public void addTimeRecord(boolean badDirt, int timeAlive) 
	{
		timeRecords.add(new TimeRecord(badDirt, timeAlive));
	}
}
