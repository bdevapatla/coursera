import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] q; 
    private int n;  
    private int last;
    
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        n = 0;      
        last = 0;
    }
    
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        int i = 0;
        for (int j = 0; j < q.length; j++) {
            if (q[j] != null) {
                temp[i++] = q[j];
            }
        }
        q = temp;
        last = n;
    }
    
    public boolean isEmpty() {
        return n == 0;
    }
    
    public int size() {
        return n;
    }
    
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Cannot add NULL item to queue");
        }
        if (last == q.length) {
            resize(n*2);
        }      
        
        q[last++] = item;
        n++;
    }   
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("No more elements");
        }
        Item item = null;       
        do {
            int randomIndex = StdRandom.uniform(q.length);
            if (q[randomIndex] != null) { 
                item = q[randomIndex];
                q[randomIndex] = null;
            }       
        } while (item == null);          
        n--;
        if (n == 0) {
            last = 0;
        }
        if (n > 0 && n == q.length/4) {
            resize(q.length/2);
        }
        return item;
    }
    
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("No more elements");
        }
        Item item = null;       
        do {
            int randomIndex = StdRandom.uniform(q.length);
            if (q[randomIndex] != null) {                
                item = q[randomIndex];
            }           
        } while (item == null);
        return item;
    } 
     
    
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {    
        
        private int i;
        private Item[] nodes;
        public RandomizedQueueIterator() {
            i = 0;
            nodes = (Item[]) new Object[n];
            int j = 0;
            for (int k = 0; k < q.length; k++) {
                if (q[k] != null) {                    
                    nodes[j++] = q[k];                  
                }
            }
            StdRandom.shuffle(nodes);
        }
        
        public boolean hasNext() {          
            return i < n;
        }
        
        public Item next() {        
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }            
            Item item = nodes[i];
            i++;
            return item;
        }
        
        public void remove() {
            throw new java.lang.UnsupportedOperationException(); 
        }        
    }
}