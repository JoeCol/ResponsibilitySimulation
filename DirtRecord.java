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

	ArrayList<Record> allRecords = new ArrayList<Record>(); 
	
	public void addRecord(int time, int dirtLevel, int badDirtLevel)
	{
		allRecords.add(new Record(time, dirtLevel, badDirtLevel));
	}
	
	public void saveToFile(String directory)
	{
		try {
			String filename = "DirtLevels_";
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void clear()
	{
		allRecords.clear();
	}
}
