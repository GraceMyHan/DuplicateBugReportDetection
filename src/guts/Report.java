package guts;

import java.util.ArrayList;
import java.util.List;

public class Report {
	int reportID;
	List<Integer> duplicates;
	String descriptions;
	
	Report(int reportID, ArrayList<Integer> duplicates, String descriptions){
		this.reportID=reportID;
		this.duplicates=duplicates;
		this.descriptions=descriptions;
	}

	public int getReportID() {
		return reportID;
	}

	public List<Integer> getDuplicates() {
		return duplicates;
	}

	public String getDescriptions() {
		return descriptions;
	}

	
	public void print() {
		System.out.println("report id:"+reportID+", duplicates:"+duplicates.toString());
	}
}
