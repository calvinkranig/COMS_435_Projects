import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MedianFinder<T extends Number> {
	PriorityQueue<T> minHeap;
	PriorityQueue<T> maxHeap;

	/**
	 * initialize your data structure here.
	 */
	public MedianFinder() {
		minHeap = new PriorityQueue<>();
		maxHeap = new PriorityQueue<>(new Comparator<T>(){
            public int compare(T o1, T o2){
                return (int) (o2.doubleValue() - o1.doubleValue());
            }
        });
	}

	public void addNum(T num) {
		minHeap.offer(num);
		T temp = minHeap.poll();
		maxHeap.offer(temp);

		if (minHeap.size() < maxHeap.size()) {
			minHeap.offer(maxHeap.poll());
		}
	}

	public double findMedian() {
		if (minHeap.size() > maxHeap.size()) {
			return  minHeap.peek().doubleValue();
		} else {
			return (minHeap.peek().doubleValue() + maxHeap.peek().doubleValue()) / 2.0;
		}
	}
}
