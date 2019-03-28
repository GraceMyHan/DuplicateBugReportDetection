package guts;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Util {
	
	public static ArrayList<Report> readReports(String fileName){
		ArrayList<Report> reportList=new ArrayList<>();
		String line = "";
		String cvsSplitBy = ",";
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			line = br.readLine();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] report = line.split(cvsSplitBy);
				String[] dups = report[3].split(",");
				ArrayList<Integer> duplicates=new ArrayList<>();
				for(String str: dups) {
					if(str.equals("")) 
						break;
					else
						duplicates.add(Integer.parseInt(str.split("\\.")[0]));
				}
				if(reportList.size()<20000)
					reportList.add(new Report(Integer.parseInt(report[0]),duplicates,report[5]));
				else
					return reportList;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return reportList;
	}
	
	public static HashMap<Integer, List<Integer>>  readTrain(){
		HashMap<Integer, List<Integer>> reportMap=new HashMap<>();
		String line = "";
		String cvsSplitBy = ",";
		
		try (BufferedReader br = new BufferedReader(new FileReader("trainingData/train.csv"))) {
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] report = line.split(cvsSplitBy);
				String[] dups = report[1].split(";");
				ArrayList<Integer> duplicates=new ArrayList<>();
				if(dups.length>0)
					for(String str: dups) {
						if(str==null||str.equals("")||str.equals("NULL")) 
							break;
						else
							duplicates.add(Integer.parseInt(str));
					}
				if((!dups[0].equals("NULL"))&&!dups[0].equals(""))
					reportMap.put(Integer.parseInt(report[0]),duplicates);
				if(reportMap.size()>20000)
					return reportMap;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader("testData/test.csv"))) {
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] report = line.split(cvsSplitBy);
				String[] dups = report[1].split(";");
				ArrayList<Integer> duplicates=new ArrayList<>();
				for(String str: dups) {
					if(str==null||str.equals("")||str.equals("NULL")) 
						break;
					else
						duplicates.add(Integer.parseInt(str.split("\\.")[0]));
				}
				if((!dups[0].equals("NULL"))&&!dups[0].equals(""))
					reportMap.put(Integer.parseInt(report[0]),duplicates);
				if(reportMap.size()>20000)
					return reportMap;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return reportMap;
	}
}
