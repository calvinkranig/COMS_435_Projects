import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class PageRank {
	
	private String edgeFile;
	private int numEdges;
	private double approximationParameter;
	private double teleportationParameter;
	private double[] ranks;
	private ArrayList<Integer>[] edges;
	
	public PageRank(String edgeFile, double approximationParameter, double teleportationParameter) {
		this.edgeFile = edgeFile;
		this.approximationParameter = approximationParameter;
		this.teleportationParameter = teleportationParameter;
		this.edges = ProcessEdgeFile.getEdges(this.edgeFile,null);
		this.computeRanks(edges);
		this.numEdges = countEdges(this.edges);
	}
	
	public PageRank(String edgeFile, double approximationParameter, double teleportationParameter, HashMap<String, Integer> pages) {
		this.edgeFile = edgeFile;
		this.approximationParameter = approximationParameter;
		this.teleportationParameter = teleportationParameter;
		this.edges = ProcessEdgeFile.getEdges(this.edgeFile,pages);
		this.computeRanks(edges);
		this.numEdges = countEdges(this.edges);
	}
	
	private int countEdges(ArrayList<Integer>[] e) {
		int total = 0;
		for(ArrayList<Integer> el: e){
			total += el.size();
		}
		return total;
	}
	
	protected ArrayList<Integer>[] edges(){
		return this.edges;
	}

	private void computeRanks(ArrayList<Integer>[] edges){
		double[] curranks = initRank(edges.length,1.0/edges.length);
		boolean converged = false;
		int iterations = 0;
		while(!converged){
			double[] nextranks = nextRank(curranks, edges);
			converged = Norm(curranks, nextranks) < this.approximationParameter;
			curranks = nextranks;
			iterations++;
		}
		//System.out.println("Iteration Count: " + iterations);
		this.ranks = curranks;
	}
	
	private double[] computeRanks(ArrayList<Integer>[] edges, int[] trusted){
		double[] curranks = initRank(edges.length,1.0/edges.length);
		boolean converged = false;
		while(!converged){
			double[] nextranks = nextRank(curranks, edges, trusted);
			converged = Norm(curranks, nextranks) < this.approximationParameter;
			curranks = nextranks;
		}
		return curranks;
	}
	
	private double[] nextRank(double[] curranks, ArrayList<Integer>[] edges) {
		double[] nextrank = initRank(curranks.length, (1.0-this.teleportationParameter)/curranks.length);
		for(int i = 0; i < nextrank.length; i++){
			if(edges[i].size() == 0){
				for(int j = 0; j < nextrank.length; j++){
					nextrank[j] = nextrank[j] + this.teleportationParameter*curranks[i]/curranks.length;
				}
			}
			else{
				for(Integer edge: edges[i]){
					nextrank[edge] = nextrank[edge] + this.teleportationParameter*curranks[i]/edges[i].size();
				}
			}
		}
		return nextrank;
	}
	
	private double[] nextRank(double[] curranks, ArrayList<Integer>[] edges, int[] trusted) {
		double[] nextrank = initRank(curranks.length, (1.0-this.teleportationParameter)/curranks.length);
		for(int i = 0; i < nextrank.length; i++){
			if(edges[i].size() == 0){
				for(int j : trusted){
					nextrank[j] = nextrank[j] + this.teleportationParameter*curranks[i]/trusted.length;
				}
			}
			else{
				for(Integer edge: edges[i]){
					nextrank[edge] = nextrank[edge] + this.teleportationParameter*curranks[i]/edges[i].size();
				}
			}
		}
		return nextrank;
	}
	
	private double Norm(double[] cur, double[] next){
		double total = 0;
		for(int i = 0; i < cur.length; i++){
			total += Math.abs(next[i]-cur[i]);
		}
		return total;
	}
	
	private double[] initRank(int size, double val) {
		double[] ret = new double[size];
		for(int i= 0; i < ret.length; i++){
			ret[i] = val;
		}
		return ret;
	}
	
	private int[] getTopK(int k, double[] arr){
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
			ret[j] = maxHeap.remove().key()+1;
		}
		return ret;
	}

	public double pageRankOf(int vertex){
		if(vertex-1>this.ranks.length||vertex-1<0){
			return -1;
		}
		else{
			return this.ranks[vertex-1];
		}
	}
	
	public double[] TrustRank(double[] Trust){
		if(Trust.length != this.ranks.length){
			System.out.println("Must pass Trust array of size v");
			return null;
		}
		int[] trusted = getTopK((int) Trust.length/4,Trust);
		return this.computeRanks(edges, trusted);
	}
	
	public int numEdges(){
		return this.numEdges;
	}
	
	public int[] topKPageRank(int k){
		return getTopK(k, this.ranks);
	}
	
	public double[] ranks(){
		return this.ranks;
	}
}
