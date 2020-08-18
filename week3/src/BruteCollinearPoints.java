import java.util.Arrays;

public class BruteCollinearPoints {
    private int count = 0;
    private LineSegment[] lineSegments;

    private Node first = null;
    private Node last = null;


    private static class Node {
        LineSegment lineSegment;
        Node next = null;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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


        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                double i2j = points[i].slopeTo(points[j]);

                for (int k = j + 1; k < len; k++) {
                    double i2k = points[i].slopeTo(points[k]);
                    if (i2j != i2k) {
                        continue;
                    }

                    for (int l = k + 1; l < len; l++) {
                        double i2l = points[i].slopeTo(points[l]);

                        if (i2k == i2l) {
                            Point[] a = {points[i], points[j], points[k], points[l]};
                            Arrays.sort(a);

                            if (this.first == null) {
                                this.first = this.last = new Node();
                            } else {
                                Node old_last = this.last;
                                this.last = new Node();
                                old_last.next = this.last;
                            }

                            this.last.lineSegment = new LineSegment(a[0], a[a.length - 1]);
                            this.count++;
                        }
                    }
                }
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
}