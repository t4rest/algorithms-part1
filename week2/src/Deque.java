import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }


        if (this.isEmpty()) {
            this.first = new Node<>();
            this.first.item = item;
            this.first.next = null;
            this.first.prev = null;
            this.last = this.first;

            return;
        }

        Node<Item> prevFirst = this.first;
        this.first = new Node<>();
        this.first.item = item;
        this.first.next = prevFirst;
        prevFirst.prev = this.first;

        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> prevLast = this.last;
        this.last = new Node<>();
        this.last.item = item;
        this.last.next = null;

        if (isEmpty()) {
            this.first = this.last;
            this.last.prev = null;
        } else {
            prevLast.next = this.last;
            last.prev = prevLast;
        }

        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        this.size--;

        Item item = this.first.item;
        this.first = this.first.next;
        if (isEmpty()) {
            this.last = null;
            this.first = null;
        } else {
            this.first.prev = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = this.last.item;
        this.last = last.prev;

        this.size--;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> cursor = first;

        public boolean hasNext() {
            return this.cursor != null;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = this.cursor.item;
            this.cursor = cursor.next;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(Integer.parseInt(StdIn.readString()));
        StdOut.println(deque.isEmpty());
        deque.addLast(Integer.parseInt(StdIn.readString()));
        StdOut.println(deque.removeFirst());
        deque.addFirst(Integer.parseInt(StdIn.readString()));
        deque.addLast(Integer.parseInt(StdIn.readString()));
        StdOut.println(deque.removeLast());
        StdOut.println(deque.size());
    }

}