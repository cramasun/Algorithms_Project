import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int n = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            n++;
            if (n <= k) {
                rq.enqueue(item);
            }
            else {
                int randIdx = StdRandom.uniform(n);
                if (randIdx < k) {
                    rq.dequeue();
                    rq.enqueue(item);
                }
            }
        }
        
        for (String item : rq) {
            StdOut.println(item);
        }
    }
}