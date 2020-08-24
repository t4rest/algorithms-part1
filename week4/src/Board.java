import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

    private int[] flatBoard;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.flatBoard = new int[this.n * this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.flatBoard[this.twoD2oneD(i, j)] = tiles[i][j];
            }
        }
    }

    private Board() {
    }

    // return index of one-dimensional array
    private int twoD2oneD(int row, int col) {
        return row * this.n + col;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.n).append("\n");

        for (int i = 0; i < this.n * this.n; i++) {
            s.append(String.format("%2d ", this.flatBoard[i]));

            if ((i + 1) % this.n == 0) {
                s.append("\n");
            }
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;

        for (int i = 0; i < this.n * this.n; i++) {
            if ((this.flatBoard[i] != 0) && (this.flatBoard[i] != i + 1)) {
                count++;
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;

        for (int i = 0; i < this.n * this.n; i++) {
            if (this.flatBoard[i] != 0 && (this.flatBoard[i] != i + 1)) {
                sum += Math.abs((this.flatBoard[i] - 1) / this.n - i / this.n) +
                        Math.abs((this.flatBoard[i] - 1) % this.n - i % this.n);
            }
        }

        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (that.n != this.n) {
            return false;
        }
        for (int i = 0; i < this.n * this.n; i++) {
            if (this.flatBoard[i] != that.flatBoard[i]) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board> {
        private Node first = null;

        private class Node {
            Board board;
            Node next = null;
        }

        public BoardIterable() {
            int p;
            for (int i = 0; i < n * n; i++) {
                if (flatBoard[i] == 0) {
                    p = i - 1;
                    if (p >= 0 && p / n == i / n) add(i, p);
                    p = i + 1;
                    if (p < n * n && p / n == i / n) add(i, p);
                    p = i - n;
                    if (p >= 0) add(i, p);
                    p = i + n;
                    if (p < n * n) add(i, p);
                }
            }

        }

        private void add(int i, int p) {
            Board board = new Board();
            board.n = n;
            board.flatBoard = new int[n * n];

            if (n * n >= 0) {
                System.arraycopy(flatBoard, 0, board.flatBoard, 0, n * n);
            }

            int swap;
            swap = board.flatBoard[i];
            board.flatBoard[i] = board.flatBoard[p];
            board.flatBoard[p] = swap;

            Node old_first = first;
            first = new Node();
            first.board = board;
            first.next = old_first;
        }

        public Iterator<Board> iterator() {
            return new NeighborIterator();
        }

        class NeighborIterator implements Iterator<Board> {
            private Node current;

            public NeighborIterator() {
                this.current = first;
            }

            public boolean hasNext() {
                return this.current != null;
            }

            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Board nextBoard = this.current.board;
                this.current = this.current.next;

                return nextBoard;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board();
        twin.n = this.n;
        twin.flatBoard = new int[n * n];

        if (n * n >= 0) {
            System.arraycopy(this.flatBoard, 0, twin.flatBoard, 0, n * n);
        }

        int swap;
        if (twin.flatBoard[0] != 0 && twin.flatBoard[1] != 0) {
            swap = twin.flatBoard[0];
            twin.flatBoard[0] = twin.flatBoard[1];
            twin.flatBoard[1] = swap;
        } else {
            swap = twin.flatBoard[2];
            twin.flatBoard[2] = twin.flatBoard[3];
            twin.flatBoard[3] = swap;
        }

        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}