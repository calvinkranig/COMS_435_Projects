public class UniversalHash {
	private static final long G_FACTOR_64 = -7046029254386353131L;

	/**
	 * @param x element
	 * @param i hash function #
	 * @param size range that we reduce to
	 * @return
	 */
	public static int hash(int x, int i, int size) {
		long h = x * G_FACTOR_64;
		int hi = (int) (h >>> 32) * i + (int) (h & 0xffffffffL);
		return (int) ((hi & 0xffffffffL) * size >>> 32);
	}
}
