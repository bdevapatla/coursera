import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last; 
    private int size;
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Insertion of NULL element is not supported.");        
        }
        
        Node oldFirst =  first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;      
        if (size == 0) {
            last = first;
        }
        else {
            oldFirst.previous = first;
        }
        size++;
    }
    
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Insertion of NULL element is not supported.");        
        }
        else if (size == 0) {
            addFirst(item);
        }
        else {
            Node oldLast =  last;       
            last = new Node();
            last.item = item;       
            last.previous = oldLast;        
            oldLast.next = last;        
            size++;
        }
    }
    
    public Item removeFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Empty dequeue");
        }        
        Item item = first.item;        
        size--;
        if (size == 0) {
            last = null;
            first = null;
        }
        else {
            first = first.next;
            if (first != null) {
                first.previous = null;            
            }
        }
        
        return item;
    }
    
    public Item removeLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Empty dequeue");
        }
        else if (size == 1) {
            return removeFirst();
        }
        else {
            size--;
            Item item = last.item;
            last = last.previous;
            last.next = null;
            return item;
        }
    }
    
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }   
    
    private class DequeIterator implements Iterator<Item> {
        private Node current;
        
        public DequeIterator() {
            current = first;
        }
        
        public boolean hasNext() {      
            return current != null;
        }

        public Item next() {            
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("Iterator has no more elements");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
        
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Remove method is not supported");
        }        
    }
}
