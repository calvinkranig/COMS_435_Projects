import java.util.Collections;
import java.util.PriorityQueue;

/**
 * @author Program Creak
 * <p>
 * Code found at https://www.programcreek.com/2015/01/leetcode-find-median-from-data-stream-java/
 */
public class MFI {

	PriorityQueue<Integer> minHeap;
	PriorityQueue<Integer> maxHeap;

	/**
	 * initialize your data structure here.
	 */
	public MFI() {
		minHeap = new PriorityQueue<>();
		maxHeap = new PriorityQueue<>(Collections.reverseOrder());
	}

	public void addNum(Integer num) {
		minHeap.offer(num);
		Integer temp = minHeap.poll();
		maxHeap.offer(temp);

		if (minHeap.size() < maxHeap.size()) {
			minHeap.offer(maxHeap.poll());
		}
	}

	public double findMedian() {
		if (minHeap.size() > maxHeap.size()) {
			return (double) minHeap.peek();
		} else {
			return ((double) minHeap.peek() + (double) maxHeap.peek()) / 2.0;
		}
	}

}
