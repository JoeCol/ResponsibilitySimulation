package CleaningEnvironment;
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
			return "," + dirtLevel + "," + badDirtLevel;
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
	
	public void saveToFile(String directory,int simulationTime)
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
			fw.write("remainingTime,dirtLevel,badDirtLevel" + System.lineSeparator());
			Record r = allRecords.get(0);
			int recordNumber = -1;
			int actualTime = 0;
			for (int i = simulationTime; i > 0; i--)
			{
				actualTime = simulationTime - i;
				if (recordNumber == -1)
				{
					if (i > r.getTime())
					{
						fw.write(actualTime + ",0,0" + System.lineSeparator());
					}
					else
					{
						recordNumber = 0;
						while (r.getTime() == allRecords.get(recordNumber).getTime())
						{
							recordNumber++;
						}
						recordNumber--;
						r = allRecords.get(recordNumber);
						fw.write(actualTime + r.toString() + System.lineSeparator());
					}
				}
				else
				{
					if (recordNumber != allRecords.size() - 1 && allRecords.get(recordNumber+1).time == i)
					{
						recordNumber++;
						r = allRecords.get(recordNumber);
						while (recordNumber != allRecords.size() - 1 && r.getTime() == allRecords.get(recordNumber).getTime())
						{
							recordNumber++;
						}
						if (recordNumber != allRecords.size() - 1)
						{
							recordNumber--;//Last instance of
						}
						else
						{
							//System.out.println("Last Record");
						}
						r = allRecords.get(recordNumber);
					}
					fw.write(actualTime + r.toString() + System.lineSeparator());
				}
			}
			fw.flush();
			fw.close();

			fw = new FileWriter(directory + timeName + fileNo + ".csv");
			fw.write("badDirt,timeAlive" + System.lineSeparator());
			for (TimeRecord tr : timeRecords)
			{
				fw.write(tr.toString() + System.lineSeparator());
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
