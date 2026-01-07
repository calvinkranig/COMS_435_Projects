import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class CountSketch extends Sketch {
	
	

	CountSketch(float epsilon, float delta, ArrayList<Integer> s) {
		super((int) Math.ceil(3 / Math.pow(epsilon, 2)), delta, s);
		initSketchArray(s);
	}
	
	CountSketch(int L, float delta, ArrayList<Integer> s){
		super(L,delta,s);
		initSketchArray(s);
	}

	

	@Override
	public int approximateFrequency(int x) {
		
		MedianFinder<Integer> mf = new MedianFinder<>();
		for (int i = 0; i < k; i++) {
			//int hash = UniversalHash.hash(x, i+1, L);
			Pair p = this.hashes[i];
			int hash = Math.abs((x*p.a+p.b)%L);
			int ghash = UniversalHash.hash(x, hash, Integer.MAX_VALUE)>(int)Integer.MAX_VALUE/2?1:-1;
			int val = this.sketchArray[i][hash]*ghash;
			mf.addNum(val);
		}
		
		return (int) mf.findMedian();
	}

	@Override
	protected void initSketchArray(ArrayList<Integer> s) {
		for (Integer x : s) {
			for (int i = 0; i < this.k; i++) {
				//int hash = UniversalHash.hash(x, i+1, L);
				Pair p = this.hashes[i];
				int hash = Math.abs((x*p.a+p.b)%L);
				int ghash = UniversalHash.hash(x, hash, Integer.MAX_VALUE)>(int)Integer.MAX_VALUE/2?1:-1;
				this.sketchArray[i][hash] += ghash;
			}
		}
	}

}
