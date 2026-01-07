import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class CMS extends Sketch {

	private int[] hh;
	private float q,r;

	CMS(float epsilon, float delta, ArrayList<Integer> s, float q, float r) {
		super((int) Math.ceil(2 / epsilon), delta, s);
		this.q = q;
		this.r= r;
		initSketchArray(s);
	}

	@Override
	public int approximateFrequency(int x) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < this.k; i++) {
			int hash = UniversalHash.hash(x, i+1, L);
			//Pair p = this.hashes[i];
			//int hash = Math.abs((p.a*x+p.b)%L);
			int val = this.sketchArray[i][hash];
			min =  val < min ? val : min;
		}
		return min;
	}

	//Retain list during init sketch array and check to see if new element belongs in list, and if prev. element should be removed
	public int[] approximateHH() {
		return hh;
	}

	// Maybe Parallelize?
	@Override
	protected void initSketchArray(ArrayList<Integer> s) {
		//LinkedList<Pair> heavyhitters = new LinkedList<>();
		HashSet<Integer> heavyhitters = new HashSet<>();
		int count = 0;
		for (Integer x : s) {
			count+=1;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < k; i++) {
				int hash = UniversalHash.hash(x, i+1, L);
				//Pair p = this.hashes[i];
				//int hash = Math.abs((p.a*x+p.b)%L);
				this.sketchArray[i][hash]+=1;
				min = this.sketchArray[i][hash] < min ? this.sketchArray[i][hash] : min;
			}
			//real check would be every time 
			if(min > q*s.size()){
				heavyhitters.add(x);
			}
			//updateHeavyHitters(heavyhitters, count, x, min);
		}
		hh = new int[heavyhitters.size()];
		hh = heavyhitters.stream().mapToInt(i->i).toArray();
		//hh = heavyhitters.toArray[];
	}

	private void updateHeavyHitters(List<Pair> heavyhitters, int count, int x, int min) {
		Iterator<Pair> i = heavyhitters.iterator();
		boolean flag = false;
		while(i.hasNext()){
			Pair cur = i.next();
			if(cur.b < count*q){
				i.remove();
			}else if(cur.a == x){
				flag = true;
				cur.b = min;
			}
		}
		if(flag == false && min > count*q){
			heavyhitters.add(new Pair(x,min));
		}
	}

}
