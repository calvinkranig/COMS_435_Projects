import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @author pols_ckranig
 *
 */
public class DocumentProcessor {
	
	private static final int THREADSPERPROCESS = 3;
	
	public DocumentProcessor(){
		
	}
	
	public static Hashtable<String,Integer>[] processDocs(String[] paths,Hashtable<String,Term> terms){
		int termindex = 0;
		Hashtable<String,Integer>[] multisets = (Hashtable<String,Integer>[]) new Hashtable[paths.length];
		for(int i = 0; i < paths.length;i++){
			multisets[i] = processDoc(paths[i]);
		}
		for(Hashtable<String,Integer> s: multisets){
			termindex= getTerms(s,terms,termindex);
		}
		return multisets;
	}
	
	private static Hashtable<String,Integer> processDoc(String filepath){
		Hashtable<String,Integer> multiset = new Hashtable<>();
		File doc = new File(filepath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(doc));
			String line;
			while((line = br.readLine())!=null){
				String pline = processLine(line);
				String[] words = pline.split("\\s+");
				for(String word: words){
					if(word.length()>2 && !word.equals("the")){
						if(!multiset.containsKey(word)){
							multiset.put(word, 1);
						}else{
							multiset.put(word, multiset.get(word)+1);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return multiset;
	}
	
	private static String processLine(String line){
		String ret = line.toLowerCase().replaceAll("[.,:;']", " ");
		return ret;
	}
	
	public Hashtable<String,Integer>[] processDocsParallel(String[] paths,Hashtable<String,Term> terms){
		Hashtable<String,Integer>[] multisets = (Hashtable<String,Integer>[]) new Hashtable[paths.length];
		int processors = Runtime.getRuntime().availableProcessors();
		int NUMTHREADS = processors*THREADSPERPROCESS;
		
		List<Callable<Hashtable<String,Integer>[]>> tasks = new ArrayList<>();
	    int portion = paths.length/NUMTHREADS;
	    int remainder = paths.length%NUMTHREADS;
	    //Split up work
	    for(int left = 0, end = portion; left<paths.length; left = end, end +=portion) {
	    	if(remainder>0) {
	    		end++;
	    		remainder--;
	    	}
	    	String[] subarray = Arrays.copyOfRange(paths, left,end);
	    	tasks.add(new Worker1(subarray));    
	    }
	    
	    ExecutorService exec = Executors.newFixedThreadPool(NUMTHREADS);
	    try {
			List<Future<Hashtable<String,Integer>[]>> results = exec.invokeAll(tasks);
			
			//assemble results
			int index = 0;
			int termindex = 0;
			for(Future<Hashtable<String,Integer>[]> f: results) {
				Hashtable<String,Integer>[] multiset = f.get();
				for(int i = 0; i < multiset.length; i++){
					termindex = getTerms(multiset[i],terms,termindex);
					multisets[index] = multiset[i];
					index++;
				}
		    	
		    }

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   exec.shutdownNow();
	   return multisets;
	}
	
	private static int getTerms(Hashtable<String,Integer> multiset, Hashtable<String,Term> terms, int index){
		for(Entry<String, Integer> term : multiset.entrySet()){
			String word = term.getKey();
			if(!terms.containsKey(word)){
				terms.put(word, new Term(index, word,term.getValue()));
				index+=1;
			}else{
				//Check to see if max frequency needs to be changed
				Term t = terms.get(word);
				if(t.getMaxFrequency()< term.getValue()){
					t.setMaxFrequency(term.getValue());
				}
			}
		}
		return index;
	}

	
	class Worker1 implements Callable<Hashtable<String,Integer>[]>{
		private String[] subarray;
		public Worker1(String[] subarray) {
			this.subarray = subarray;
		}
		@Override
		public Hashtable<String,Integer>[] call(){
			Hashtable<String,Integer>[] multisets = (Hashtable<String,Integer>[]) new Hashtable[this.subarray.length];
			for(int i = 0; i < this.subarray.length;i++){
				multisets[i] = this.processDoc(this.subarray[i]);
			}
			return multisets;
		}
		
		private Hashtable<String,Integer> processDoc(String filepath){
			Hashtable<String,Integer> multiset = new Hashtable<>();
			File doc = new File(filepath);
			try {
				BufferedReader br = new BufferedReader(new FileReader(doc));
				String line;
				while((line = br.readLine())!=null){
					String pline = processLine(line);
					String[] words = pline.split("\\s+");
					for(String word: words){
						if(word.length()>2 && !word.equals("the")){
							if(!multiset.containsKey(word)){
								multiset.put(word, 1);
							}else{
								multiset.put(word, multiset.get(word)+1);
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return multiset;
		}
		
		private String processLine(String line){
			String ret = line.toLowerCase().replaceAll("[.,:;']", " ");
			return ret;
		}
		  
	  }
}
