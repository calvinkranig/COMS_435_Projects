import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class PageRankHelper {
	
	private final PageRank pr;
	private String[] pages;
	
	public PageRankHelper(String edgeFile, double approximationParameter, double teleportationParameter){
		HashMap<String,Integer> pageTemp = new HashMap<>();
		this.pr = new PageRank(edgeFile, approximationParameter, teleportationParameter, pageTemp);
		this.pages = new String[pageTemp.size()];
		for(Entry<String, Integer> e: pageTemp.entrySet()){
			this.pages[e.getValue()-1] = e.getKey();
		}
	}
	
	protected static int[] getTopK(int k, double[] arr){
		int[] ret = new int[k];
		PriorityQueue<Pair<Integer>> maxHeap = new PriorityQueue<Pair<Integer>>(arr.length,new Comparator<Pair<Integer>>() {
	        @Override
	        public int compare(Pair<Integer> o1, Pair<Integer> o2) {
	            return - Double.compare(o1.val(),o2.val());
	        }
	    });
		for(int i = 0; i < arr.length; i++){
			maxHeap.add(new Pair<Integer>(i,arr[i]));
		}
		for(int j = 0; j < ret.length; j++){
			ret[j] = maxHeap.remove().key();
		}
		return ret;
	}

	public String[] topKPageRank(int k){
		int[] indextemp =  pr.topKPageRank(k);
		String[] ret = new String[k];
		for(int i = 0; i < k ; i++){
			ret[i] = this.pages[indextemp[i]-1];
		}
		return ret;
	}
	
	public int[] topKTrustRankInt(int k, double[] Trust){
		double[] trustranks = pr.TrustRank(Trust);
		return this.getTopK(k, trustranks);
	}
	
	public String[] topKTrustRank(int k, double[] Trust){
		double[] trustranks = pr.TrustRank(Trust);
		int[] topk = this.getTopK(k, trustranks);
		String[] ret = new String[k];
		for(int i = 0; i < k ; i++){
			ret[i] = this.pages[topk[i]];
		}
		return ret;
	}

	public PageRank pr() {
		return pr;
	}

	public String[] pages() {
		return pages;
	}
	
	
}
