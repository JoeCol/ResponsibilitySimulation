package CleaningEnvironment;
import java.util.ArrayList;

/****
 * 
 * @author Joe Collenette
 *
 * Records the level of dirt over time and saves to a csv file
 */
public class DirtRecord {
	public class Record {
		int timeAppeared;
		int timeCleaned;
		boolean isBadDirt;
		
		public Record(int start, int end, boolean isBadDirt) {
			super();
			this.timeAppeared = start;
			this.timeCleaned = end;
			this.isBadDirt = isBadDirt;
		}
		
		public int getStart() {
			return timeAppeared;
		}

		public int getEnd() {
			return timeCleaned;
		}
		
		public boolean isBadDirt() {
			return isBadDirt;
		}
		
		public String toString()
		{
			return timeAppeared + "," + timeCleaned + "," + isBadDirt;
		}
		
	}

	ArrayList<Record> records = new ArrayList<Record>(); 
	
	public void clear()
	{
		records.clear();
	}

	public void addRecord(int start, int end, boolean badDirt) 
	{
		records.add(new Record(start, end, badDirt));
	}
}
