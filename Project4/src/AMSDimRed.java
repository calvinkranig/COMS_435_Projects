import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class AMSDimRed {

	public static ArrayList<Vector> reduceDim(ArrayList<Vector> inputVectors, float epsilon, float delta) {
		int k = (int) ceil((1 / pow(epsilon, 2)) * log(1 / delta));

		ArrayList<Vector> res = new ArrayList<>();
		Random rand = new Random();

		for (Vector v : inputVectors) {
			float[] reducedV = new float[k];

			for (int j = 0; j < k; ++j) {
				int count = 0;
				int hash = rand.nextInt(Integer.MAX_VALUE);
				for (int c = 0; c < v.getDim(); ++c) {
					int coord = Float.floatToRawIntBits(v.getIthCoor(c));
					// todo.. change to random Gaussian function instead?
					int val = UniversalHash.hash(coord, hash, Integer.MAX_VALUE)>(int)Integer.MAX_VALUE/2?1:-1;
					count += val;
				}
				reducedV[j] = (float) (pow(count, 2) / sqrt(k));
			}

			Vector reduced = new Vector(reducedV);
			res.add(reduced);
		}

		return res;
	}
}
