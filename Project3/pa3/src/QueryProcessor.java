import java.util.ArrayList;

public class QueryProcessor {
	
	private PositionalIndex pi;
	
	
	public QueryProcessor(String folder) {
		this.pi = new PositionalIndex(folder);
	}


	public void topKDocsTest(String query, int k){
		ArrayList<String> docs =  pi.topKDocs(query, k);
		System.out.println("Query : " + query);
		for(String doc : docs){
			double tps = pi.TPScore(query, doc);
			double vss = pi.VSScore(query, doc);
			double rel = pi.Relevance(query, doc);
			System.out.printf("Document: %s TPScore: %f VSScore: %f Relevance: %f \n", doc, tps, vss, rel);
		}
	}
	
	public ArrayList<String> topKDocs(String query, int k){
		return pi.topKDocs(query, k);
	}
}
