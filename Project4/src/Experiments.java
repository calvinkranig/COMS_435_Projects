import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author pols_ckranig
 *
 */

public class Experiments {
	

	private static final int STREAMSIZE = 100000;
	private static final int UNIVERSESIZE = 10000;
	private static final float E= (float) 0.1;
	private static final float D = (float) 0.001;
	private static final float Q= (float) E*2;
	private static final float R= (float) E;
	
	
	
	public static void CMSTest(ArrayList<Integer> s, HashMap<Integer,Integer> universe){
		CMS cms = new CMS(E,D,s,Q,R);
		long td = 0;
		ArrayList<Entry<Integer, Integer>> badEstimates = new ArrayList<>();
		HashSet<Integer> actualHH = new HashSet<>();
		double worstExpectedDifference = E*STREAMSIZE;
		PriorityQueue<Pair> maxHeap =  new PriorityQueue<Pair>(new Comparator<Pair>(){
            public int compare(Pair o1, Pair o2){
                return (int) (o2.b - o1.b);
            }
        });
		//total difference check
		for(Entry<Integer, Integer> x : universe.entrySet()){
			//CMS
			int approx = cms.approximateFrequency(x.getKey());
			int actual = x.getValue();
			int difference = approx-actual;
			td+=difference;
			if(difference>=worstExpectedDifference){
				badEstimates.add(x);
			}
			if(actual > Q*STREAMSIZE){
				actualHH.add(x.getKey());
			}
			maxHeap.add(new Pair(x.getKey(),difference));
		}
		System.out.printf("CMSTest with %f epsilon, %f delta, %d Stream Size, %d Universe Size\n"
				+ "Total Difference: %d Average Difference %f\n"
				+ "Worst Expected Difference: %f\n", E, D,STREAMSIZE, UNIVERSESIZE, td, (double)td/(double)universe.size(),worstExpectedDifference);
		System.out.printf("Bad Estimates: %d Expected Bad Estimates: %f\n", badEstimates.size(), D*UNIVERSESIZE);
		System.out.println("Top 10 Worst Estimates");
		for(int j = 0; j < 10; j++){
			Pair cur = maxHeap.poll();
			System.out.printf("Element:%d Actual Count:%d Difference:%d\n", cur.a, universe.get(cur.a), cur.b);
		}
		
		//Test Heavy Hitters
		int badHH = 0;
		for(Integer x : cms.approximateHH()){
			int actual = universe.get(x);
			if(actual < R*STREAMSIZE){
				badHH++;
			}
		}
		System.out.printf("Actual Heavy Hitters: %d Aproximate Heavy Hitters: %d Bad Heavy Hitters: %d\n", actualHH.size(), cms.approximateHH().length, badHH);
	}
	
	public static void CountSketchTest(ArrayList<Integer> s, HashMap<Integer,Integer> universe, Integer size){
		CountSketch cs;
		if(size != null){
			cs = new CountSketch(size,D,s);
		}else{
			cs = new CountSketch(E,D,s);
		}
		double F2 = getF2(universe);
		long td = 0;
		ArrayList<Entry<Integer, Integer>> badEstimates = new ArrayList<>();
		double temp = Math.pow(F2, 0.5);
		double worstExpectedDifference = E*temp;
		PriorityQueue<Pair> maxHeap =  new PriorityQueue<Pair>(new Comparator<Pair>(){
            public int compare(Pair o1, Pair o2){
                return (int) (o2.b - o1.b);
            }
        });
		//total difference check
		for(Entry<Integer, Integer> x : universe.entrySet()){
			//CMS
			int approx = cs.approximateFrequency(x.getKey());
			int actual = x.getValue();
			int difference = Math.abs(approx-actual);
			td+=difference;
			if(difference>=worstExpectedDifference){
				badEstimates.add(x);
			}
			maxHeap.add(new Pair(x.getKey(),difference));
		}
		System.out.printf("CountSketch Test with epsilon: %f , delta: %f, Stream Size:  %d, Universe Size: %d \n"
				+ "Total Difference: %d Average Difference %f\n"
				+ "Worst Expected Difference: %f\n",
				E, D,STREAMSIZE, UNIVERSESIZE, td, (double)td/(double)universe.size(),worstExpectedDifference);
		System.out.printf("Bad Estimates: %d Expected Bad Estimates: %f\n", badEstimates.size(), D*UNIVERSESIZE);
		System.out.println("Top 10 Worst Estimates");
		for(int j = 0; j < 10; j++){
			Pair cur = maxHeap.poll();
			System.out.printf("Element:%d Actual Count:%d Difference:%d\n", cur.a, universe.get(cur.a), cur.b);
		}
	}
	
	private static double getF2(HashMap<Integer, Integer> universe) {
		double total = 0;
		for(Entry<Integer, Integer> x : universe.entrySet()){
			total+=Math.pow(x.getValue(), 2);
		}
		return total;
	}

	private static void generateStream(ArrayList<Integer> s,HashMap<Integer,Integer> universe) {
		Random ran = new Random();
		int i = 0;
		//Make sure every element is at lease in once
		for(; i<UNIVERSESIZE;i++){
			s.add(i);
			universe.put(i, 1);
		}
		//add rest of elements randomly
		for(; i < STREAMSIZE; i++) {
			int val = ran.nextInt(UNIVERSESIZE);
			s.add(val);
			Integer prev = universe.get(val);
			if(prev == null){
				universe.put(val, 1);
			}else{
				universe.put(val, prev+1);
			}
		}
	}
	
	private static int[] generateStreamWeighted(ArrayList<Integer> s,HashMap<Integer,Integer> universe){
		int i = 0;
		Random ran = new Random();
		//Make sure every element is at lease in once
		for(; i<UNIVERSESIZE;i++){
			s.add(i);
			universe.put(i, 1);
		}
		
		int hh =  (int) (Q*STREAMSIZE)+1;
		int hh1 = ran.nextInt(UNIVERSESIZE);
		int hh2 = ran.nextInt(UNIVERSESIZE);
		for(int j = 0; j < hh; j++){
			s.add(hh1);
			s.add(hh2);
			i+=2;
		}
		universe.put(hh1, hh+1);
		universe.put(hh2, hh+1);
		//add rest of elements randomly
		for(; i < STREAMSIZE; i++) {
			int val = ran.nextInt(UNIVERSESIZE);
			s.add(val);
			Integer prev = universe.get(val);
			if(prev == null){
				universe.put(val, 1);
			}else{
				universe.put(val, prev+1);
			}
		}
		int[] ret = {hh1,hh2};
		
		return ret;
	}
	

	private static void Test1(){
		ArrayList<Integer> s = new ArrayList<>(STREAMSIZE);
		HashMap<Integer,Integer> universe = new HashMap<>(UNIVERSESIZE);
		generateStream(s,universe);
		CMSTest(s,universe);
		System.out.println("");
		CountSketchTest(s,universe,null);
	}
	
	private static void Test2(){
		ArrayList<Integer> s = new ArrayList<>(STREAMSIZE);
		HashMap<Integer,Integer> universe = new HashMap<>(UNIVERSESIZE);
		int[] hh = generateStreamWeighted(s,universe);
		CMSTest(s,universe);
		System.out.println();
	}
	
	private static void Test3(){
		ArrayList<Integer> s = new ArrayList<>(STREAMSIZE);
		HashMap<Integer,Integer> universe = new HashMap<>(UNIVERSESIZE);
		generateStream(s,universe);
		CMSTest(s,universe);
		System.out.println();
		CountSketchTest(s,universe,(int) Math.ceil(2 / E));
	}
	
	private static void Test4(){
		ArrayList<Integer> s = new ArrayList<>(STREAMSIZE);
		HashMap<Integer,Integer> universe = new HashMap<>(UNIVERSESIZE);
		generateStream(s,universe);
		double F2 = getF2(universe);
		double aprox = AMS.secondFreqMoment(s, E, D);
		System.out.printf("Expected Error:%f Actual:%f\n", E*F2, Math.abs(F2-aprox));
		System.out.printf("Actual F2:%f Aproximate F2:%f\n", F2, aprox);
	}
	public static void main(String args[]){
		Test4();
	}

}
