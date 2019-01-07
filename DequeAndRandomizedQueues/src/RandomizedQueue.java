import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int n;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        n = 0;
    }
    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the randomized queue
    public int size() {
        return n;
    }
    // Resize the queue array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item passed to Enqueue");
        }
        if (n == queue.length) {
            resize(2*queue.length);
        }
        queue[n++] = item;
    }
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Queue");
        }
        
        int remIdx = StdRandom.uniform(n);
        Item item = queue[remIdx];
        queue[remIdx] = queue[--n];
        queue[n] = null;
        if (n > 0 && n == queue.length/4) {
            resize(queue.length/2);
        }
        return item;
    }
   // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Queue");
        }
        
        int sampleIdx = StdRandom.uniform(n);
        Item item = queue[sampleIdx];
        return item;   
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    // Iterator to iterate randomly over items in the queue
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private final Item[] iterQ;
        
        public RandomizedQueueIterator() {
           iterQ = (Item[]) new Object[n];
           current = 0;
           /* Copy the element from queue to iterator queue */
           for (int i = 0; i < n; i++) {
               iterQ[i] = queue[i];
           }
           /* Shuffle the iterator queue array to have elements in random */
           StdRandom.shuffle(iterQ);
        }
        public boolean hasNext() { 
            return current < n;            
        }
        public void remove() { 
            throw new UnsupportedOperationException("Remove method not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No More Items");
            }
            Item item = iterQ[current++];
            return item;
        }
    }
}