import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class KdTree {
    private double minDistance = Double.POSITIVE_INFINITY;
    private TreeNode root = null;
    private int n = 0;


    private static class TreeNode {
        private final Point2D point;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(Point2D p) {
            point = p;
            left = right = null;
        }
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return this.n;
    }

    public void insert(Point2D p) {
        this.root = insert(p, this.root, true);
    }

    private TreeNode insert(Point2D p, TreeNode x, boolean t) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (x == null) {
            this.n++;
            return new TreeNode(p);
        }// can i use boolean in this way?
        if (x.point.equals(p)) {
            return x;
        }

        if (t) {
            boolean cmp = p.x() <= x.point.x();
            if (cmp) {
                x.left = insert(p, x.left, false);    // in most cases it will just simply return x.left ,
            } else {                // unless it creates new node ,think about it;
                x.right = insert(p, x.right, false);
            }
        } else {
            boolean cmp = p.y() <= x.point.y();
            if (cmp) {
                x.left = insert(p, x.left, true);
            } else {
                x.right = insert(p, x.right, true);
            }
        }
        return x; //think carefully, why here we return x;
    }


    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.root == null) {
            return false;
        }

        boolean k;
        k = contains(p, this.root, true);
        return k;

    }

    private boolean contains(Point2D p, TreeNode x, boolean t) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (x == null) {
            return false;
        }

        if (x.point.equals(p)) {
            return true;
        }

        boolean k;
        boolean cmp;
        if (t) {
            cmp = p.x() <= x.point.x();
        } else {
            cmp = p.y() <= x.point.y();
        }

        if (cmp) {
            k = contains(p, x.left, !t);
        } else {
            k = contains(p, x.right, !t);
        }

        return k;
    }

    public void draw() {
        draw(root);
    }

    private void draw(TreeNode x) {
        if (x == null) {
            return;
        }

        draw(x.left);
        x.point.draw();
        draw(x.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        return new Cluster(rect);
    }

    private class Cluster implements Iterable<Point2D> {

        private Node first;

        private class Node {
            Point2D point;
            Node next = null;
        }

        public Cluster(RectHV rect) {
            first = null;
            cluster(rect, root, true);

        }

        private void cluster(RectHV rect, TreeNode x, boolean t) {
            if (x == null) {
                return;
            }

            if (rect.contains(x.point)) add(x.point);

            if (t) {
                if (rect.xmax() <= x.point.x())
                    cluster(rect, x.left, false);
                else if (rect.xmin() > x.point.x())
                    cluster(rect, x.right, false);
                else {
                    cluster(rect, x.left, false);
                    cluster(rect, x.right, false);
                }
            } else {
                if (rect.ymax() <= x.point.y())
                    cluster(rect, x.left, true);
                else if (rect.ymin() > x.point.y())
                    cluster(rect, x.right, true);
                else {
                    cluster(rect, x.left, true);
                    cluster(rect, x.right, true);
                }
            }
        }

        private void add(Point2D p) {
            Node old_first = first;
            first = new Node();
            first.point = p;
            first.next = old_first;
        }

        public Iterator<Point2D> iterator() {
            return new ClusterIterator();
        }

        private class ClusterIterator implements Iterator<Point2D> {
            Node current;

            public ClusterIterator() {
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

        if (this.root == null) {
            return null;
        }

        Point2D nearest_point = this.root.point;
        nearest_point = nearest(p, this.root, true, nearest_point);
        this.minDistance = Double.POSITIVE_INFINITY;
        //nearest_point = null;
        return nearest_point;
    }

    private Point2D nearest(Point2D p, TreeNode x, boolean t, Point2D n) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (x == null) {
            return n;
        }

        double current_distance = p.distanceSquaredTo(x.point);
        if (current_distance < this.minDistance) {
            n = x.point;
            this.minDistance = current_distance;
        }

        if (t) {
            if (p.x() <= x.point.x()) {
                n = this.nearest(p, x.left, false, n);
                if (this.minDistance > (x.point.x() - p.x())) {
                    n = this.nearest(p, x.right, false, n);
                }
            } else {
                n = this.nearest(p, x.right, false, n);
                if (this.minDistance >= (p.x() - x.point.x())) {
                    n = this.nearest(p, x.left, false, n);
                }
            }
        } else {
            if (p.y() <= x.point.y()) {
                n = this.nearest(p, x.left, true, n);
                if (this.minDistance > (x.point.y() - p.y())) {
                    n = this.nearest(p, x.right, true, n);
                }
            } else {
                n = this.nearest(p, x.right, true, n);
                if (this.minDistance > (p.y() - x.point.y())) {
                    n = this.nearest(p, x.left, true, n);
                }
            }
        }
        return n;
    }


    public static void main(String[] args) {
        KdTree kdtree = new KdTree();

        Point2D p = new Point2D(0.372, 0.497);
        kdtree.insert(p);
        p = new Point2D(0.564, 0.413);
        kdtree.insert(p);
        kdtree.draw();

        p = new Point2D(0.226, 0.577);
        kdtree.insert(p);
        p = new Point2D(0.144, 0.179);
        kdtree.insert(p);
        p = new Point2D(0.083, 0.51);
        kdtree.insert(p);
        p = new Point2D(0.32, 0.708);
        kdtree.insert(p);
        kdtree.draw();

        p = new Point2D(0.417, 0.362);
        kdtree.insert(p);
        p = new Point2D(0.862, 0.825);
        kdtree.insert(p);
        p = new Point2D(0.785, 0.725);
        kdtree.insert(p);
        kdtree.draw();

        StdOut.print(kdtree.size());
        StdOut.print(kdtree.contains(p));
        StdOut.print(kdtree.isEmpty());

        p = new Point2D(0.499, 0.208);
        kdtree.insert(p);
        kdtree.draw();

        Point2D k = new Point2D(0.53, 0.919);
        p = kdtree.nearest(k);

        StdOut.print(p);
    }
}
