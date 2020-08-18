import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int count = 0;
    private LineSegment[] lineSegments;

    private Node first = null;
    private Node last = null;


    private static class Node {
        LineSegment lineSegment;
        Node next = null;
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        int len = points.length;

        //  Throw an IllegalArgumentException if the argument to the constructor is null,
        //  if any point in the array is null,
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        //  or if the argument to the constructor contains a repeated point.
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        for (int i = 0; i < len - 3; i++) {
            Arrays.sort(points);

            // Sort the points according to the slopes they makes with p.
            // Check if any 3 (or more) adjacent points in the sorted order
            // have equal slopes with respect to p. If so, these points,
            // together with p, are collinear.

            Arrays.sort(points, points[i].slopeOrder());

            for (int p = 0, firstP = 1, lastP = 2; lastP < len; lastP++) {
                // find last collinear to p point


                boolean isEqualSlope = Double.compare(points[p].slopeTo(points[firstP]), points[p].slopeTo(points[lastP])) == 0);


                while (lastP < len && isEqualSlope) {
                    lastP++;
                }
                // if found at least 3 elements, make segment if it's unique
                if (lastP - firstP >= 3 && points[p].compareTo(points[firstP]) < 0) {

                    if (this.first == null) {
                        this.first = this.last = new Node();
                    } else {
                        Node old_last = this.last;
                        this.last = new Node();
                        old_last.next = this.last;
                    }


                    this.last.lineSegment = new LineSegment(points[p], points[lastP - 1]);
                    this.count++;

                }
                // Try to find next
                firstP = lastP;
            }
        }

        this.lineSegments = new LineSegment[count];
        Node currentPointer = first;
        int i = 0;
        while (currentPointer != null) {
            this.lineSegments[i] = currentPointer.lineSegment;
            currentPointer = currentPointer.next;
            i++;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.count;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.lineSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}