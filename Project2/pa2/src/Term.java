
/**
 * @author pols_ckranig
 *
 */
public class Term {
	private final int index;
	private int binaryIndex;
	private final String term;
	private int maxFrequency;
	
	/**
	 * @param index
	 * @param term
	 * @param maxFrequency
	 */
	public Term(int index, String term, int maxFrequency) {
		this.index = index;
		this.term = term;
		this.maxFrequency = maxFrequency;
		binaryIndex = -1;
	}

	/**
	 * @return the binaryIndex
	 */
	public int getBinaryIndex() {
		return binaryIndex;
	}

	/**
	 * @param binaryIndex the binaryIndex to set
	 */
	public void setBinaryIndex(int binaryIndex) {
		this.binaryIndex = binaryIndex;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @return the maxFrequency
	 */
	public int getMaxFrequency() {
		return maxFrequency;
	}

	/**
	 * @param maxFrequency the maxFrequency to set
	 */
	public void setMaxFrequency(int maxFrequency) {
		this.maxFrequency = maxFrequency;
	}
	
	
}
