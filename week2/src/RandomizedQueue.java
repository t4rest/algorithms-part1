import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.rq = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (this.size == this.rq.length) {
            this.resize(this.rq.length * 2);
        }

        this.rq[this.size] = item;

        this.size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(this.size);

        Item item = this.rq[index];
        this.rq[index] = this.rq[this.size - 1];
        this.rq[this.size - 1] = null;

        this.size--;

        if (this.size > 0 && this.size <= this.rq.length / 4) {
            this.resize(this.rq.length / 2);
        }


        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        return this.rq[StdRandom.uniform(this.size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int index = size - 1;
        private final Item[] tq;

        public RandomizedQueueIterator() {
            this.tq = (Item[]) new Object[size];

            for (int i = 0; i < size; i++) {
                this.tq[i] = rq[i];
            }

            StdRandom.shuffle(this.tq);
        }

        public boolean hasNext() {
            return this.index >= 0;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = this.tq[this.index];
            this.index--;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            temp[i] = this.rq[i];
        }

        this.rq = temp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(StdIn.readString());
        randomizedQueue.enqueue(StdIn.readString());
        StdOut.println(randomizedQueue.isEmpty());
        StdOut.println(randomizedQueue.dequeue());
        StdOut.println(randomizedQueue.size());
        StdOut.println(randomizedQueue.sample());

        for (String s : randomizedQueue) {
            StdOut.println(s);
        }
    }

}