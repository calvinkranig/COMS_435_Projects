import java.util.ArrayList;
import java.util.Random;

public class AMS {
	
	public static double secondFreqMoment(ArrayList<Integer> s, float epsilon, float delta){
		int k = (int) Math.ceil(15/(epsilon*epsilon));
		int copies = (int) Math.ceil(Math.log(1/delta));
		Random r = new Random();
		MedianFinder<Double> mf = new MedianFinder<>();
		
		for(int i = 0; i <copies; i++){
			//Algorithm B
			double average = 0;
			for(int j =  0; j < k; j++){
				//Algorithm A
				int count = 0;
				int hash = r.nextInt(Integer.MAX_VALUE);
				for(Integer x:s){
					int val = UniversalHash.hash(x, hash, Integer.MAX_VALUE)>(int)Integer.MAX_VALUE/2?1:-1;
					count+= val;
				}
				average+=count*count;
			}
			mf.addNum(average/k);
		}
		return mf.findMedian();
	}
}
