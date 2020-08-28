import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PointSET {

    private final SET<Point2D> PS;

    public PointSET() {
        this.PS = new SET<>();
    }

    public boolean isEmpty() {
        return this.PS.isEmpty();
    }

    public int size() {
        return this.PS.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        this.PS.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return this.PS.contains(p);
    }

    public void draw() {
        for (Point2D p : this.PS) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return new Clustering(rect);
    }

    private class Clustering implements Iterable<Point2D> {

        Node first = null;

        private class Node {
            Point2D point;
            Node next = null;
        }

        public Clustering(RectHV rect) {
            for (Point2D p : PS)
                if (rect.contains(p))
                    add(p);
        }

        public void add(Point2D p) {
            Node old_first = first;
            first = new Node();
            first.point = p;
            first.next = old_first;
        }

        public Iterator<Point2D> iterator() {
            return new ClusteringIterator();
        }

        private class ClusteringIterator implements Iterator<Point2D> {

            Node current;

            public ClusteringIterator() {
                current = first;
            }

            public boolean hasNext() {
                return current != null;
            }

            public Point2D next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                Point2D p = current.point;
                current = current.next;
                return p;
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.PS.isEmpty()) {
            return null;
        }

        Point2D nearest_point = null;
        double distance;
        double min_d = Double.POSITIVE_INFINITY;

        for (Point2D point : this.PS) {
            distance = p.distanceTo(point);
            if (distance < min_d) {
                nearest_point = point;
                min_d = distance;
            }
        }

        return nearest_point;
    }

    public static void main(String[] args) {

    }
}


