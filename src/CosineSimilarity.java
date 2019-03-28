import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import guts.Report;
import guts.Util;

public class CosineSimilarity {
	/**
	 * Similarity class for finding cosine similarity. Data Structure I used from
	 * Java Collections Framework is HashMap. Purpose: to store data from the files
	 * or the strings in the form of key-value mapping. Andrew Id: pdeoskar
	 * 
	 * @author Priyanka Deoskar
	 *
	 */
	/**
	 * Map object.
	 */
	private Map<String, BigInteger> myMap;
	/**
	 * frequency of the word from the text.
	 */
	private BigInteger frequency = BigInteger.ZERO;
	/**
	 * number of lines.
	 */
	private int numOfLines = 0;
	/**
	 * number of non-distinct words.
	 */
	private BigInteger numOfWords = BigInteger.ZERO;
	/**
	 * dot product of vectors.
	 */
	private BigInteger dotProd = BigInteger.ZERO;
	/**
	 * product of euclidean norm of 2 maps.
	 */
	private double deno;
	/**
	 * cosine similarity or the arc cosine.
	 */
	private double distance;
	/**
	 * euclidean norm.
	 */
	private double sRoot;

	/**
	 * parameterized constructor for string input.
	 * 
	 * @param string input string.
	 */
	public CosineSimilarity() {}	
	public CosineSimilarity(String string) {
		myMap = new HashMap<String, BigInteger>();
		if (string != null) {
			String[] wordsFromText = string.split("\\W");
			for (int i = 0; i < wordsFromText.length; i++) {
				// convert to lowercase.
				wordsFromText[i] = wordsFromText[i].toLowerCase();
				// check for true word.
				if (wordsFromText[i].matches("[a-z]+")) {
					// if true word then check if key is already existing.
					if (myMap.containsKey(wordsFromText[i])) {
						// if true then get the frequency of the key(i.e. word)
						// and increment by 1.
						frequency = myMap.get(wordsFromText[i]);
						frequency = frequency.add(BigInteger.ONE);
						myMap.put(wordsFromText[i], frequency);
						// increment the count of total words.
						numOfWords = numOfWords.add(BigInteger.ONE);
					} else {
						// if the word is being added for first time the set frequency
						// to 1.
						myMap.put(wordsFromText[i], BigInteger.ONE);
						// increment the count of total words.
						numOfWords = numOfWords.add(BigInteger.ONE);
					}
				}
			}
		}
	}

	/**
	 * parameterized constructor for file input.
	 * 
	 * @param file input file.
	 */
	public CosineSimilarity(File file) {
		myMap = new HashMap<String, BigInteger>();
		Scanner scanner = null;
		try {
			if (file != null) {
				scanner = new Scanner(file, "latin1");
				while (scanner.hasNextLine()) {
					// if there is next line then increment number of lines.
					if (scanner.hasNextLine()) {
						numOfLines++;
					}
					String line = scanner.nextLine();
					// split the string at whitespace.
					String[] wordsFromText = line.split("\\W");
					for (int i = 0; i < wordsFromText.length; i++) {
						// convert to lowercase.
						wordsFromText[i] = wordsFromText[i].toLowerCase();
						// check for true words.
						if (wordsFromText[i].matches("[a-z]+")) {
							// check if word already exists.
							if (myMap.containsKey(wordsFromText[i])) {
								// if true then get the frequency of the key(i.e. word) and increment by 1.
								frequency = myMap.get(wordsFromText[i]);
								frequency = frequency.add(BigInteger.ONE);
								myMap.put(wordsFromText[i], frequency);
								// increment the number of total words.
								numOfWords = numOfWords.add(BigInteger.ONE);
							} else {
								// if the word is being added for first time the set frequency
								// to 1.
								myMap.put(wordsFromText[i], BigInteger.ONE);
								// increment the count of total words.
								numOfWords = numOfWords.add(BigInteger.ONE);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Cannot find the file");
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * calculates the total number of lines.
	 * 
	 * @return number of lines.
	 */
	public int numOfLines() {
		return numOfLines;
	}

	/**
	 * calculates the total number of words.
	 * 
	 * @return number of words.
	 */
	public BigInteger numOfWords() {
		return numOfWords;
	}

	/**
	 * calculates the total number of distinct words.
	 * 
	 * @return number of distinct words.
	 */
	public int numOfWordsNoDups() {
		return myMap.size();
	}

	/**
	 * calculates the euclidean norm.
	 * 
	 * @return sroot which is euclidean norm value.
	 */
	public double euclideanNorm() {
		BigInteger freqVal = BigInteger.ZERO;
		BigInteger sqr = BigInteger.ZERO;
		double sqrVal = 0;
		if (myMap == null) {
			return 0.0;
		}
		for (String word : myMap.keySet()) {
			freqVal = myMap.get(word);
			sqr = sqr.add(freqVal.multiply(freqVal));
		}
		sqrVal = sqr.doubleValue();
		sRoot = Math.sqrt(sqrVal);
		return sRoot;
	}

	/**
	 * reason for non-quadratic time complexity: Since HashMap's 'get' method
	 * provides O(1) complexity so we need to use only one 'for loop' without any
	 * need for nesting. So the complexity is O(n) instead of O(n2).
	 * 
	 * @param map map instance passed from the test classes.
	 * @return dot product value of the 2 map instances (2 vectors).
	 */
	public double dotProduct(Map<String, BigInteger> map) {
		if (map == null || map.size() == 0) {
			return 0.0;
		}
		for (String word : myMap.keySet()) {
			if (map.containsKey(word)) {
				dotProd = dotProd.add(myMap.get(word).multiply(map.get(word)));
			}
		}
		return dotProd.doubleValue();
	}

	/**
	 * calculates the cosine similarity between 2 individual strings or 2 files.
	 * 
	 * @param map instance passed from the test classes.
	 * @return dot product value of the 2 map instances (2 vectors).
	 */
	public double distance(Map<String, BigInteger> map) {
		if (map == null || map.size() == 0) {
			return Math.PI / 2;
		}
		if (map.equals(myMap)) {
			return 0.0;
		}
		BigInteger freqVal = BigInteger.ZERO;
		BigInteger sqr = BigInteger.ZERO;
		double sqrVal;
		double eNorm1 = 0;
		double eNorm2 = 0;

		eNorm1 = euclideanNorm();
		for (String word : map.keySet()) {
			freqVal = map.get(word);
			sqr = sqr.add((freqVal.multiply(freqVal)));
		}
		sqrVal = sqr.doubleValue();
		eNorm2 = Math.sqrt(sqrVal);
		deno = eNorm1 * eNorm2;
		distance = Math.acos(dotProduct(map) / deno);
		return distance;
	}

	/**
	 * for getting the reference to the Map instance.
	 * 
	 * @return a reference to the Map instance in my class.
	 */
	public Map<String, BigInteger> getMap() {
		return new HashMap<String, BigInteger>(myMap);
	}

	public double predict(int top_K, List<Report> reports) {

		HashMap<Integer,HashMap<Integer,Double>> distances=new HashMap();
		for(Report report: reports){
			CosineSimilarity cs = new CosineSimilarity(report.getDescriptions());
			HashMap<Integer,Double> reportDistances;
			if(!distances.containsKey(report.getReportID()))
				reportDistances=new HashMap<>();
			else
				reportDistances=distances.get(report.getReportID());
			for(Report report2: reports) {
				if(distances.containsKey(report.getReportID())&&distances.get(report.getReportID()).containsKey(report2.getReportID())) {
					continue;
				}
				if(report.getReportID()!=report2.getReportID()) {
					CosineSimilarity cs2 = new CosineSimilarity(report2.getDescriptions());
					Double dis_between=cs.distance(cs2.getMap());
					reportDistances.put(report2.getReportID(), dis_between);
					
					if(!distances.containsKey(report2.getReportID())) {
						HashMap<Integer,Double> reportDistances2=new HashMap<>();
						distances.put(report2.getReportID(), reportDistances2);
					}
					distances.get(report2.getReportID()).put(report2.getReportID(), dis_between);
				}
			}
			distances.put(report.getReportID(), reportDistances);
		}
		
		HashMap<Integer, List<Integer>> realDup=Util.readTrain();
		
		HashMap<Integer,ArrayList<Integer>> candidateDup=new HashMap<>();

		for(Integer report_id: realDup.keySet()) {
			if(candidateDup.containsKey(report_id))
				continue;
			else {
				candidateDup.put(report_id, new ArrayList<>());
			
				for(Integer report2_id: distances.keySet()) {
					if(distances.keySet().contains(report_id)&&report_id!=report2_id) {		
						//System.out.println(similarity);
						if(distances.get(report_id).get(report2_id)<1.0&&candidateDup.get(report_id).size()<top_K) {
							candidateDup.get(report_id).add(report2_id);
							//System.out.println("report:"+report_id+", predict duplicate:"+report2_id);
						}
					}
				}
			}
		}
		
		double TP=0;
		double FN=0;
		
		for(Integer report_id: realDup.keySet()) {				
			if(candidateDup.containsKey(report_id)) {
				ArrayList<Integer> candidates=candidateDup.get(report_id);
				if(candidates.size()==0) {
					FN++;
				}else {
					TP+=1.0;
					for(Integer dup_id:	candidates) {
						if(realDup.get(report_id).contains(dup_id)) {
							TP+=1.0;
						}
					}
				}
			}
		}
		System.out.println("TP is:"+TP+", FN is:"+FN);
		return TP/(TP+FN);
	}
	
	public static void main(String[] args) {
		List<Report> reports = Util.readReports("trainingData/mozilla_core.csv");
		CosineSimilarity cs=new CosineSimilarity();
		System.out.print(cs.predict(12, reports));
		

	}

}
