
class Pair<T>{
		private final T key;
		private final double val;
		public Pair(T key, double val) {
			this.key = key;
			this.val = val;
		}
		public T key() {
			return key;
		}

		public double val() {
			return val;
		}
	}
