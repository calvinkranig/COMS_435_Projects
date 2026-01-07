import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Test {
	
	private static final String edgefile = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project3/WikiSportsGraph.txt";
	private static final String edgefileinteger = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project3/7.txt";
	private static final String spamfile = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project3/spam.txt";
	private static final String txtFolder = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project3/IR/IR";
	private static final String txtFolder2 = "C:/Users/pols_ckranig/Desktop/COMS_435/Projects/Project3/IR/test2";
	
	
	private static void test1(){
		double[] approxP = {0.05, 0.01};
		double[] teleP = {0.85, 0.25};
		for(int i = 0; i < teleP.length; i++){
			for(int j = 0; j < approxP.length; j++){
				System.out.printf("Approximation Parameter: %f Teleportation Paramter: %f \n",approxP[j],teleP[i]);
				PageRankHelper prh = new PageRankHelper(edgefileinteger, approxP[j] , teleP[i]);
				System.out.println(prh.pr().pageRankOf(prh.pr().edges().length));
			}
		}
	}
	
	private static void test2(){
		PageRankHelper prh = new PageRankHelper(edgefileinteger, 0.05,0.85);
		double totalDiff = 0;
		int diffpositions = 0;
		int pagenum = prh.pages().length;
		double[] trustRanks = prh.pr().TrustRank(prh.pr().ranks());
		String[] trustorderString = prh.topKTrustRank(pagenum, prh.pr().ranks());
		String[] regorderString = prh.topKPageRank(pagenum);
		
	
		
		
		int[] regOrder = prh.pr().topKPageRank(prh.pr().ranks().length);
		int[] trustOrder = prh.topKTrustRankInt(prh.pr().ranks().length, prh.pr().ranks());				
		System.out.println("Trust Ranks Order, Regular Ranks Order");
		for(int i = 0; i < trustorderString.length; i++){
			totalDiff += Math.abs(trustRanks[i]-prh.pr().ranks()[i]);
			diffpositions = trustorderString[i].equals(regorderString[i]) ?  diffpositions: diffpositions+1;
			if(i < 10){
				System.out.printf("%s : %f, %s : %f\n", trustorderString[i], trustRanks[trustOrder[i]], regorderString[i],prh.pr().ranks()[regOrder[i]]);
			}
		}
		for(int i = 0; i <trustOrder.length; i++){
			totalDiff += Math.abs(trustRanks[i]-prh.pr().ranks()[i]);
			diffpositions =trustRanks[i] == regOrder[i] ?  diffpositions: diffpositions+1;
			if(i < 10){
				System.out.printf("%s : %f, %s : %f\n", trustOrder[i], trustRanks[trustOrder[i]], regOrder[i],prh.pr().ranks()[regOrder[i]]);
			}
		}
		System.out.println("Total Difference: " + totalDiff);
		System.out.println("Average Difference: "+ totalDiff/(double)trustOrder.length);
		System.out.printf("Different at %d positions out of %d\n", diffpositions, pagenum);
	}

	private static void test3(){
		int[] spampagecount = {1,2,3,4,5,6,7,8,9,10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 125};
		PageRankHelper prh = new PageRankHelper(edgefileinteger, 0.0002,0.85);
		int[] ranks = prh.pr().topKPageRank(prh.pr().edges().length);
		int lowest = ranks[ranks.length-1];
		System.out.println("Original Rankings");
		for(int i = 0; i <ranks.length && i < 10;i++){
			System.out.println(ranks[i]);
		}
		System.out.println("\nLowest Ranked Vertex: "+lowest+"\n");
		for(int count: spampagecount){
			SpamFarm sf = new SpamFarm(edgefileinteger, lowest, count);
			sf.CreateSpam(spamfile);
			PageRankHelper spamprh = new PageRankHelper(spamfile, 0.0002,0.85);
			int[] spamranks = spamprh.pr().topKPageRank(spamprh.pr().edges().length);			 
			double[] trustRanks = spamprh.pr().TrustRank(spamprh.pr().ranks());
			int[] trustOrder = spamprh.getTopK(trustRanks.length, trustRanks);
			for(int i = 0; i <ranks.length;i++){
				if(spamranks[i] == lowest){
					for(int j =0; j < trustOrder.length; j++){
						if(trustOrder[j] == lowest){
							System.out.printf("Spam Pages: %d\nNew Rank: %d \nIncrease in Rank: %d\nTrust Rank Position: %d\nPageRank: %f\nTrustRank: %f\n\n",
									count, i,ranks.length-1-i, j, spamprh.pr().ranks()[lowest],trustRanks[lowest]);
							break;
						}
					}
					
				}
			}
		}
		System.out.println("Spam Rankings");
		
	}
	
	private static void test4(){
		String[] querys = {"ball", 
				"pittsburgh pirates", 
				"new york state", 
				"the cubs finished fifth", 
				"the last cy young winner for the cubs"};
		QueryProcessor qp = new QueryProcessor(txtFolder);
		for(String q : querys){
			qp.topKDocsTest(q, 10);
		}
	}
	public static void main(String[] args) {
		test1();
	}

}
