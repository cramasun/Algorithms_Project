/******************************************************************************
 *  Compilation:  javac UnitTestDeque.java
 *  Execution:    java UnitTestDeque < input.txt
 *  Dependencies: StdIn.java StdOut.java
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class UnitTestDeque {
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        
        while (!StdIn.isEmpty()) {
            int item = StdIn.readInt();
            deque.addFirst(item);
        }
        
        System.out.println("Deque Size = "+deque.size());
        
        for (int item : deque) {
            StdOut.print(item + " ");
        }
        
        StdOut.println("\n");
    }
}