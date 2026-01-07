package src;

public interface Filter {
	public void add(String s);
	
	public boolean appears(String s);
	
	public int filterSize();
	
	public int dataSize();
	
	public int numHashes();
	
	public boolean getBit(int j);
	
	public String name();
}
