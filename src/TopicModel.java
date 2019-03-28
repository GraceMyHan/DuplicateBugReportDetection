import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.Object.*;

import guts.Report;
import guts.Util;

public class TopicModel {

	// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
	//  Note that the first parameter is passed as the sum over topics, while
	//  the second is 
	int numTopics = 200;
	ParallelTopicModel model = new ParallelTopicModel(numTopics,1.0,0.01);
	InstanceList instances;
	ArrayList<Pipe> pipeList;
	List<Report> reports;

	public void train(List<Report> reports){
		this.reports = reports;
		// Begin by importing documents from text to feature sequences
		pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add( new CharSequenceLowercase() );
		pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		pipeList.add( new TokenSequenceRemoveStopwords(new File("src/guts/en.txt"), "UTF-8", false, false, false) );
		pipeList.add( new TokenSequence2FeatureSequence() );

		instances = new InstanceList (new SerialPipes(pipeList));

		for(Report report: reports) {
			if(report.getDuplicates().size()>0){
					System.out.print("new instance id:"+report.getReportID()+"?");
					instances.addThruPipe(new Instance(report.getDescriptions(),1,(Integer)report.getReportID(),null));
					System.out.print("new instance id:"+instances.get(instances.size()-1).getName());
					System.out.println();
			}
		}
	
		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		//  statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only, 
		//  for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(1000);
		
		try {
			model.estimate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double cosineSimilarity(double[] A, double[] B) {
		if (A == null || B == null || A.length == 0 || B.length == 0 || A.length != B.length) {
			return -2;
		}

		double sumProduct = 0;
		double sumASq = 0;
		double sumBSq = 0;
		for (int i = 0; i < A.length; i++) {
			sumProduct += A[i]*B[i];
			sumASq += A[i] * A[i];
			sumBSq += B[i] * B[i];
		}
		if (sumASq == 0 && sumBSq == 0) {
			return 2.0;
		}
		return sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));

	}

	public double predict(int top_K){
		HashMap<Integer, double[]> map=new HashMap<>();
		
		HashMap<Integer, List<Integer>> realDup=Util.readTrain();
		
		for(int i=0;i<instances.size();i++) {
			double[] topicDistribution = model.getTopicProbabilities(i);

			map.put((Integer)instances.get(i).getName(),topicDistribution);
		}
		
		HashMap<Integer,ArrayList<Integer>> candidateDup=new HashMap<>();

		for(Integer report_id: realDup.keySet()) {
			if(candidateDup.containsKey(report_id))
				continue;
			else {
				candidateDup.put(report_id, new ArrayList<>());
			
				for(Integer report2_id: map.keySet()) {
					if(report_id!=report2_id) {
						//System.out.println("report:"+report_id+", predict duplicate:"+report2_id);
						double similarity=cosineSimilarity(map.get(report_id),map.get(report2_id));
						//System.out.println(similarity);
						if(similarity>0.00001&&candidateDup.get(report_id).size()<top_K) {
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

	public static void main(String [] args){
		
		List<Report> reports=Util.readReports("trainingData/mozilla_core.csv");
		
		TopicModel tm=new TopicModel();
		tm.train(reports);
		System.out.print(tm.predict(3));
		
		
	}

}