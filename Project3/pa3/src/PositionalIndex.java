import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.PriorityQueue;

public class PositionalIndex {
	private String folder;
	private HashMap<String,ArrayList<PostingsListItem>> postings;
	private HashMap<String,Double> docTermVectorLength;
	private int numDocs;
	
	
	public PositionalIndex(String folder) {
		this.folder = folder;
		this.docTermVectorLength = new HashMap<>();
		this.postings = new HashMap<>();
		this.numDocs = new ProcessDoc().processDocsParallel(this.folder, postings);
		this.initDocTermVectorLength();
	}
	
	private void initDocTermVectorLength(){
		this.docTermVectorLength = new HashMap<String, Double>();
		for(Entry<String,ArrayList<PostingsListItem>> posting :this.postings.entrySet()){
			for(PostingsListItem pli : posting.getValue()){
				Double cur = this.docTermVectorLength.get(pli.DocName());
				Double termWeight = Math.pow(this.weight(pli.positions().size(), numDocs, posting.getValue().size()), 2.0);
				if(cur == null){
					this.docTermVectorLength.put(pli.DocName(), termWeight);
				}else{
					Double newWeight = termWeight + cur;
					this.docTermVectorLength.put(pli.DocName(), newWeight);
				}
			}
		}
		for(Entry<String, Double> e : this.docTermVectorLength.entrySet()){
			this.docTermVectorLength.put(e.getKey(), Math.sqrt(e.getValue()));
		}
	}
	
	public int termFrequency(String term, String doc){
		PostingsListItem docList = this.getPLI(term, doc);
		return docList!=null ? docList.positions().size(): 0;
	}
	
	public int docFrequency(String term){
		ArrayList<PostingsListItem> postinglist = this.postings.get(term);
		return postinglist == null ? 0 : postinglist.size();
	}
	
	public String postingsList(String t){
		StringBuilder s = new StringBuilder();
		ArrayList<PostingsListItem> postinglist = this.postings.get(t);
		if(postinglist == null){
			return "";
		}
		s.append("[");
		for(PostingsListItem d: postinglist){
			s.append(d.toString()+",");
		}
		int i = s.lastIndexOf(",");
		s.setCharAt(i, ']');
		return s.toString();
	}
	
	public double weight(String t, String d){
		return weight(this.termFrequency(t, d), this.numDocs, this.docFrequency(t));
	}
	
	private double weight(int docTermFrequency, int numDocs, int docFrequency){
		double ret = Math.sqrt((double)docTermFrequency)*Math.log10((double)numDocs/(double)docFrequency);
		return ret;
	}
	

	public double TPScore(String query, String doc){
		String[] words = query.toLowerCase().split("\\s+");
		if(words.length<=1){
			return 0;
		}
		int total = 0;
		for(int i = 0; i < words.length-1; i++){
			total += this.calcITPSScore(doc, words[i],words[i+1]);
		}
		
		return (double) words.length/(double)total;
	}
	
	public double VSScore(String query, String doc){
		String[] terms = query.toLowerCase().split("\\s+");
		Double v2Total = this.docTermVectorLength.get(doc);
		if(v2Total == null){return 0;}
		double total = 0;
		HashMap<String, Integer> queryVector = new HashMap<>();
		//calculate dot product
		for(String term : terms){
			Integer count = queryVector.get(term);
			if(count == null){
				queryVector.put(term, 1);
			}else{
				queryVector.put(term, count+1);
			}
		}
		double queryVectorLength = 0;
		for(Entry<String, Integer> e : queryVector.entrySet()){
			total += e.getValue()*this.weight(e.getKey(), doc);
			queryVectorLength += Math.pow(e.getValue(), 2);
		}
		double v1Total = Math.sqrt(queryVectorLength);
		return total/(v1Total*v2Total);
	}
	
	public double Relevance(String query, String doc){
		return 0.6*TPScore(query, doc)+ 0.4*VSScore(query, doc);
	}
	
	protected ArrayList<String> topKDocs(String query, int k){
		ArrayList<String> arr = new ArrayList<>(k);
		PriorityQueue<Pair<String>> maxHeap = new PriorityQueue<Pair<String>>(k,new Comparator<Pair<String>>() {
	        @Override
	        public int compare(Pair<String> o1, Pair<String> o2) {
	            return - Double.compare(o1.val(),o2.val());
	        }
	    });
		for(String doc: this.docTermVectorLength.keySet()){
			maxHeap.add(new Pair<String>(doc, this.Relevance(query, doc)));
		}
		for(int i = 0; i < k; i++){
			arr.add(maxHeap.remove().key());
		}
		return arr;
	}
	
	private int calcITPSScore(String doc, String t1, String t2){
		PostingsListItem p1 = this.getPLI(t1, doc);
		PostingsListItem p2 = this.getPLI(t2, doc);
		if(p1 == null || p2 == null){
			return 17;
		}
		ArrayList<Integer> l1 = p1.positions();
		ArrayList<Integer> l2 = p2.positions();
		int min = 17;
		int j = 0;
		for(int i = 0; i < l2.size(); i++){
			while(j<l1.size() && l1.get(j)<l2.get(i)){
				int temp = l2.get(i)-l1.get(j);
				min = temp<min?temp:min;
				j++;
			}
		}
		return min;
	}
	
	private PostingsListItem getPLI(String term, String doc){
		ArrayList<PostingsListItem> postinglist = this.postings.get(term);
		if(postinglist == null){return null;}
		int index = Collections.binarySearch(postinglist, new PostingsListItem(doc),  PostingsListItem.Comparator());
		return index >= 0 ? postinglist.get(index) : null;
	}
	
}
