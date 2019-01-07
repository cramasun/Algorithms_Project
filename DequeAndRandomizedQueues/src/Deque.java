import java.util.Iterator;
import java.util.NoSuchElementException;
    
public class Deque<Item> implements Iterable<Item> {
    private int size;           // Size of the Deque
    private Node<Item> first, last;  // front node and back node of the Deque  
    
    // Helper inner class to represent a Deque node
    private static class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;
    }
    
    // construct an empty deque
    public Deque() { 
        size = 0;
        first = null;
        last = null;
    }                          
    
    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return size;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null Argument passed");
        }
        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (isEmpty()) {
            last = first;
        }
        else {
            oldFirst.prev = first;
        }
        size++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null Argument passed");
        }
        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }
        size++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Deque");
        }
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) { 
            last = null; 
        }
        else {
            first.prev = null;
        }
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty Dequeue");
        }
        Item item = last.item;
        last = last.prev;
        size--;
        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        return item;
    }
        
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(first);
    }
    
    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        
        public DequeIterator(Node<Item> first) {
            current = first;
        }
        
        public boolean hasNext() {
            return current != null;
        }
        public void remove() { 
            throw new UnsupportedOperationException("Remove not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No More Items");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}