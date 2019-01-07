 import edu.princeton.cs.algs4.StdOut;
 import edu.princeton.cs.algs4.StdIn;

public class UnitTestRandomizedQueue {
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        
        for (int i = 0; i < 5; i++) {
            rq.enqueue(i);
        }
        
        StdOut.println("Size ="+rq.size());
        
        for (int i = 0; i < 5; i++) {
            StdOut.println("Dequeue = "+rq.dequeue());
        }
    }
}