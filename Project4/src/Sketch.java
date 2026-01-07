import java.util.ArrayList;
import java.util.Random;


/**
 * @author calvinkranig
 * 
 * Abstract class that combines some functions for CMS and CountSketch
 * 
 *
 */
public abstract class Sketch {

	final protected int[][] sketchArray;
	//k hash functions and L columns
	final protected int k, L;
	Pair[] hashes;
	
	Sketch(int approxL, float delta, ArrayList<Integer> s){
		//should do with log base 2
		k = (int)Math.ceil(Math.log(1.0/delta)/Math.log(2));
		L = Prime.nextPrime(approxL);
		sketchArray = new int[k][L];
		initHash();
	}
	
	protected abstract void initSketchArray(ArrayList<Integer> s);

	public abstract int approximateFrequency(int x);
	
	protected void initHash() {
		hashes = new Pair[this.k];
		Random ran = new Random();
		for(int i =0; i < k; i++){
			int a = ran.nextInt(L);
			int b= ran.nextInt(L);
			hashes[i] = new Pair(a,b);
		}
	}

}
