package src;

import java.util.ArrayList;
import java.util.BitSet;

public class DynamicFilter implements Filter{
	private int bitsPerElement;
	private static final int INITSETSIZE = 1000;
	private static final String NAME = "DynamicFilter";
	private int lastSetSize;
	private ArrayList<Filter> filters;
	private int datasize;
	private int numHashes;
	private int totalfiltersize;
	
	public DynamicFilter(int bitsPerElement) {
		this.bitsPerElement = bitsPerElement;
		filters = new ArrayList<Filter>();
		this.lastSetSize = INITSETSIZE;
		this.addNewFilter();
		this.datasize = 0;
		this.totalfiltersize = this.filters.get(0).filterSize();
		this.numHashes = this.filters.get(0).numHashes();
	}
	
	private void addNewFilter(){
		this.filters.add(new BloomFilterRan(this.lastSetSize, this.bitsPerElement));
	}
	
	private Filter getLastFilter(){
		return this.filters.get(this.filters.size()-1);
	}
	
	private void resizeFilter(){
		//Add a new filter
		this.addNewFilter();
		//update size of new filter so filter size will double next time
		this.lastSetSize *=2;
		this.totalfiltersize += this.getLastFilter().filterSize();
		this.numHashes += this.getLastFilter().numHashes();
	}

	@Override
	public void add(String s) {
		if(this.getLastFilter().dataSize()>=this.lastSetSize){
			//Add new filter of size 2l
			resizeFilter();
		}
		this.getLastFilter().add(s);
		this.datasize++;
	}

	@Override
	public boolean appears(String s) {
		for(int i = 0; i < this.filters.size(); i++){
			if(this.filters.get(i).appears(s)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int filterSize() {
		return this.totalfiltersize;
	}

	@Override
	public int dataSize() {
		return this.datasize;
	}

	@Override
	public int numHashes() {
		return this.numHashes;
	}

	@Override
	public boolean getBit(int j) {
		if(j>this.filterSize()-1){
			return false;
		}
		int temp = j;
		int index = 0;
		while(this.filters.get(index).filterSize()-1<temp){
			temp -= this.filters.get(index).filterSize();
			index++;
		}
		return this.filters.get(index).getBit(temp);
	}

	@Override
	public String name() {
		return NAME;
	}
	


}
