import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int N = 4;
    private Item[] array;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[N];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // resizes array to specified capacity
    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (size == array.length) {
            resize(array.length * 2);
        }
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int index = StdRandom.uniformInt(size);
        Item item = array[index];
        array[index] = array[size - 1];

        array[--size] = null;

        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int index = StdRandom.uniformInt(size);
        return array[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Item[] shuffledItems;
        private int count;

        QueueIterator() {
            shuffledItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                shuffledItems[i] = array[i];
            }
            StdRandom.shuffle(shuffledItems);
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements found");
            }

            return shuffledItems[count++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove method is not supported");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        System.out.println(q.isEmpty());
        for (int i = 1; i < 11; i++) {
            q.enqueue(i);
        }
        System.out.println(q.size());
        for (int i = 1; i < 11; i++) {
            System.out.print(q.sample() + " ");
        }
        System.out.println();
        for (int n : q) {
            System.out.print(n + " ");
        }
        System.out.println();
        for (int i = 1; i < 11; i++) {
            System.out.print(q.dequeue() + " ");
        }
        System.out.println();
        System.out.println(q.size());
    }

}