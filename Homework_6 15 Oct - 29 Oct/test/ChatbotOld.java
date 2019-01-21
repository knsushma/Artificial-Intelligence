
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Chatbot{
    
    //private static String filename = "/Users/sushmakn/wisconsin/projects/eclipse-workspace/IntroToAi/src/com/wisconsin/ai/hw6/WARC201709_wid.txt";
    private static String filename = "./WARC201709_wid.txt";    

    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
            sc.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    
    private static TreeMap<Integer, Integer> getUnigramWordTypeCount(ArrayList<Integer> corpusArray) {
    	 
    	TreeMap<Integer, Integer> wordTypeCount = new TreeMap<Integer, Integer>();
             
         for (int i=0; i<corpusArray.size(); i++) {
         	int wordTypeIndex = corpusArray.get(i);
         		//wordTypeCountProbability.merge(wordTypeIndex, 1, Integer::sum);
         		if (wordTypeCount.containsKey(wordTypeIndex)) {
         			wordTypeCount.put(wordTypeIndex, wordTypeCount.get(wordTypeIndex) + 1);
             	} else {
             		wordTypeCount.put(wordTypeIndex, 1);
             	}
         }
         
         return wordTypeCount;	
    }
    
    private static TreeMap<Integer, Integer> getBigramWordTypeCount(ArrayList<Integer> corpusArray, int history) {
   	 
    	TreeMap<Integer, Integer> wordTypeCount = new TreeMap<Integer, Integer>();
             
         for (int i=1; i<corpusArray.size(); i++) {
        	 	if (corpusArray.get(i-1) == history) {
	         		int wordTypeIndex = corpusArray.get(i);
	         		//wordTypeCountProbability.merge(wordTypeIndex, 1, Integer::sum);
	         		if (wordTypeCount.containsKey(wordTypeIndex)) {
	         			wordTypeCount.put(wordTypeIndex, wordTypeCount.get(wordTypeIndex) + 1);
	             	} else {
	             		wordTypeCount.put(wordTypeIndex, 1);
	             	}
        	 	}
         }
         
         return wordTypeCount;	
    }
    
    private static TreeMap<Integer, Integer> getTrigramWordTypeCount(ArrayList<Integer> corpusArray, int history1, int history2) {
      	 
    	TreeMap<Integer, Integer> wordTypeCount = new TreeMap<Integer, Integer>();
             
         for (int i=2; i<corpusArray.size(); i++) {
        	 	if (corpusArray.get(i-2) == history1 && corpusArray.get(i-1) == history2 ) {
	         		int wordTypeIndex = corpusArray.get(i);
	         		//wordTypeCountProbability.merge(wordTypeIndex, 1, Integer::sum);
	         		if (wordTypeCount.containsKey(wordTypeIndex)) {
	         			wordTypeCount.put(wordTypeIndex, wordTypeCount.get(wordTypeIndex) + 1);
	             	} else {
	             		wordTypeCount.put(wordTypeIndex, 1);
	             	}
        	 	}
         }
         
         return wordTypeCount;	
    }
    
    private static TreeMap<Integer, Double> getWordTypeCountProbability(ArrayList<Integer> corpusArray) {
   	 
    	HashMap<Integer, Integer> wordTypeCount = new HashMap<Integer, Integer>();
    	TreeMap<Integer, Double> wordTypeCountProbability = new TreeMap<Integer, Double>();	
             
         for (int i=0; i<corpusArray.size(); i++) {
         	int wordTypeIndex = corpusArray.get(i);
         		//wordTypeCountProbability.merge(wordTypeIndex, 1, Integer::sum);
         		if (wordTypeCount.containsKey(wordTypeIndex)) {
         			wordTypeCount.put(wordTypeIndex, wordTypeCount.get(wordTypeIndex) + 1);
             	} else {
             		wordTypeCount.put(wordTypeIndex, 1);
             	}
         }
         
         int corpusLength = corpusArray.size();
         for ( Map.Entry<Integer, Double> wc : wordTypeCountProbability.entrySet()) {
         	Double probability = (double) (wc.getValue()/corpusLength);
         	if ( probability != 0) {
         		wordTypeCountProbability.put(wc.getKey(), probability);
         	} 
         }
         
         return wordTypeCountProbability;	
    }
    
    private static int getUnigramWord(ArrayList<Integer> corpusArray, double randomIndex) {
    	 
    	int corpusLength = corpusArray.size();
        double cumulativeProb = 0.0000000;
         
         TreeMap<Integer, Integer> wordTypeCount = new TreeMap<Integer, Integer>();                     
         wordTypeCount = getUnigramWordTypeCount(corpusArray);     
         
         for ( Map.Entry<Integer, Integer> entry : wordTypeCount.entrySet()) {
        	 double probability = (double) entry.getValue()/corpusLength;
         	if ( (cumulativeProb + probability) >= randomIndex) {
         		return entry.getKey();
         	}
         	cumulativeProb = cumulativeProb + probability;
         }
         return 0;
    }
    
    private static int getBigramWord(ArrayList<Integer> corpusArray, int h1, double randomIndex) {
   	 
         double cumulativeProb = 0;
         
         TreeMap<Integer, Integer> wordTypeCountWithH1 = new TreeMap<Integer, Integer>();                     
         wordTypeCountWithH1 = getBigramWordTypeCount(corpusArray, h1);     
         
         int sumOfHistories = 0;
         for (int count : wordTypeCountWithH1.values()) {
         	sumOfHistories += count;
         }
         
         for ( Map.Entry<Integer, Integer> entry : wordTypeCountWithH1.entrySet()) {
         	double probability = (double) entry.getValue()/sumOfHistories;           	
         	if ((cumulativeProb + probability) >= randomIndex) {
         		return entry.getKey();
         	}
         	cumulativeProb = cumulativeProb + probability;
         }
         return 0;
    }
    
    private static int getTrigramWord(ArrayList<Integer> corpusArray, int h1, int h2, double randomIndex) {
      	 
         double cumulativeProb = 0;
         
         TreeMap<Integer, Integer> wordTypeCountWithH1H2 = new TreeMap<Integer, Integer>();                     
         wordTypeCountWithH1H2 = getTrigramWordTypeCount(corpusArray, h1, h2);     
         
         int sumOfHistories = 0;
         for (int count : wordTypeCountWithH1H2.values()) {
         	sumOfHistories += count;
         }
         
         for ( Map.Entry<Integer, Integer> entry : wordTypeCountWithH1H2.entrySet()) {
         	double probability = (double) entry.getValue()/sumOfHistories;           	
         	if ((cumulativeProb + probability) >= randomIndex) {
         		return entry.getKey();
         	}
         	cumulativeProb = cumulativeProb + probability;
         }
         return -1;
    }
    
    
    public static void main(String[] args){
        ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
        
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count = 0;
            //TODO count occurence of w

            for (int i=0; i<corpus.size(); i++) {
            	if(corpus.get(i) == w) {
            		count++;
            	}
            }
            
            System.out.println(count);
            System.out.println(String.format("%.7f",count/(double)corpus.size()));
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            //TODO generate
            
            double r = (double) n1/n2;
            int corpusLength = corpus.size();
            double cumulativeProb = 0;
            
            TreeMap<Integer, Integer> wordTypeCount = new TreeMap<Integer, Integer>();                     
            wordTypeCount = getUnigramWordTypeCount(corpus);     
            
            for ( Map.Entry<Integer, Integer> entry : wordTypeCount.entrySet()) {
            	double probability = (double) entry.getValue()/corpusLength;           	
            	if ( probability != 0 && (cumulativeProb + probability) >= r) {
            		System.out.println(entry.getKey());
            		System.out.println(String.format("%.7f",cumulativeProb));
            		System.out.println(String.format("%.7f",cumulativeProb+probability));
            		break;
            	}
            	cumulativeProb = cumulativeProb + probability;            	
            }
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            //TODO
            for (int i=0; i<corpus.size()-1; i++) {
            	if ( corpus.get(i) == h && corpus.get(i+1) == w) {
            		count = count + 1;
            		words_after_h.add(corpus.get(i));
            	} else if ( corpus.get(i) == h ) {
            		words_after_h.add(corpus.get(i+1));
            	}
            }            
            //output 
            System.out.println(count);
            System.out.println(words_after_h.size());
            System.out.println(String.format("%.7f",count/(double)words_after_h.size()));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            //TODO
            
            double r = (double) n1/n2;
            double cumulativeProb = 0;
            
            TreeMap<Integer, Integer> wordTypeCountWithH1 = new TreeMap<Integer, Integer>();                     
            wordTypeCountWithH1 = getBigramWordTypeCount(corpus, h);     
            int sumOfHistories = 0;
            for (int count : wordTypeCountWithH1.values()) {
            	sumOfHistories += count;
            }
            
            for ( Map.Entry<Integer, Integer> entry : wordTypeCountWithH1.entrySet()) {
            	double probability = (double) entry.getValue()/sumOfHistories;           	
            	if ( probability != 0 && (cumulativeProb + probability) >= r) {
            		System.out.println(entry.getKey());
            		System.out.println(String.format("%.7f",cumulativeProb));
            		System.out.println(String.format("%.7f",cumulativeProb+probability));
            		break;
            	}
            	cumulativeProb = cumulativeProb + probability;            	
            }
            
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            //TODO

            for (int i=0; i<corpus.size()-2; i++) {
            	if ( corpus.get(i) == h1 && corpus.get(i+1) == h2 && corpus.get(i+2) == w) {
            		count = count + 1;
            		words_after_h1h2.add(corpus.get(i+2));
            	} else if ( corpus.get(i) == h1 && corpus.get(i+1) == h2 ) {
            		words_after_h1h2.add(corpus.get(i+2));
            	}
            }
            //output 
            System.out.println(count);
            System.out.println(words_after_h1h2.size());
            if(words_after_h1h2.size() == 0)
                System.out.println("undefined");
            else
                System.out.println(String.format("%.7f",count/(double)words_after_h1h2.size()));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            //TODO
            
            double r = (double) n1/n2;
            double cumulativeProb = 0;
            
            TreeMap<Integer, Integer> wordTypeCountWithH1H2 = new TreeMap<Integer, Integer>();                     
            wordTypeCountWithH1H2 = getTrigramWordTypeCount(corpus, h1, h2);     
	        if ( wordTypeCountWithH1H2.size() == 0) {
	        	System.out.println("undefined");
	        } else {
            	int sumOfHistories = 0;
	            for (int f : wordTypeCountWithH1H2.values()) {
	            	sumOfHistories += f;
	            }	            
	            for ( Map.Entry<Integer, Integer> entry : wordTypeCountWithH1H2.entrySet()) {
	            	double probability = (double) entry.getValue()/sumOfHistories;           	
	            	if ( probability != 0 && (cumulativeProb + probability) >= r) {
	            		System.out.println(entry.getKey());
	            		System.out.println(String.format("%.7f",cumulativeProb));
	            		System.out.println(String.format("%.7f",cumulativeProb+probability));
	            		break;
	            	}
	            	cumulativeProb = cumulativeProb + probability;            	
	            }
	        }
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r            	
            	double r = rng.nextDouble();
                h1 = getUnigramWord(corpus, r);
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }

                // TODO Generate second word using r 
                r = rng.nextDouble();
                h2 = getBigramWord(corpus, h1, r);
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                double r = rng.nextDouble();
                // TODO Generate second word using r
                h2 = getBigramWord(corpus, h1, r);
                
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = 0;
                // TODO Generate new word using h1,h2
                w = getTrigramWord(corpus, h1, h2, r);
                if ( w == -1) {
                	w = getBigramWord(corpus, h2, r);
                }
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}

