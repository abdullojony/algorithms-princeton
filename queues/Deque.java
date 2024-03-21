import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private static final int N = 4;
    private Item[] array;
    private int size;
    private int front;

    // construct an empty deque
    public Deque() {
        array = (Item[]) new Object[N];
        size = 0;
        front = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }
    
    // resizes the array to specified capacity
    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[front];
            front = (front + 1) % array.length;
        }
        array = newArray;
        front = 0;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (size == array.length) {
            resize(array.length * 2);
        }
        
        if (front == 0) {
            front = array.length - 1;
        } else {
            front--;
        }
        array[front] = item;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (size == array.length) {
            resize(array.length * 2);
        }
        int index = (front + size) % array.length;
        array[index] = item;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deck is empty");
        }

        Item item = array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size--;

        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deck is empty");
        }

        size--;
        int index = (front + size) % array.length;
        Item item = array[index];
        array[index] = null;

        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DeckIterator();
    }

    private class DeckIterator implements Iterator<Item> {
        private int index = front;
        private int count = 0;

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements found");
            }
            
            Item item = array[index];
            index = (index + 1) % array.length;
            count++;
            
            return item;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove method is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deck = new Deque<>();
        for (int i = 1; i < 11; i++) {
            deck.addLast(String.valueOf(i));
        }
        System.out.println(deck.size());
        for (String s : deck) {
            System.out.println(s);
        }
    }
}