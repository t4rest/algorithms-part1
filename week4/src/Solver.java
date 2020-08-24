import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Solver {
    private boolean solvable;
    private int number_moves = 0;
    private final BoardSolution board_solution = new BoardSolution();

    private static class SearchNode {
        private Board board;
        private int moves;
        private SearchNode predecessor;
        private int priority_manhattan;
    }

    private static class ManhattanComparator implements Comparator<SearchNode> {
        public int compare(SearchNode s1, SearchNode s2) {
            return Integer.compare(s1.priority_manhattan, s2.priority_manhattan);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        ManhattanComparator manhattanComparator = new ManhattanComparator();

        MinPQ<SearchNode> minPQ = new MinPQ<>(manhattanComparator);
        SearchNode searchNode = new SearchNode();
        searchNode.board = initial;
        searchNode.moves = 0;
        searchNode.predecessor = null;
        searchNode.priority_manhattan = initial.manhattan();
        minPQ.insert(searchNode);

        MinPQ<SearchNode> minPQ_t = new MinPQ<>(manhattanComparator);
        SearchNode searchNode_t = new SearchNode();
        searchNode_t.board = initial.twin();
        searchNode_t.moves = 0;
        searchNode_t.predecessor = null;
        searchNode_t.priority_manhattan = initial.manhattan();
        minPQ_t.insert(searchNode_t);

        int i = 0;
        SearchNode searchNode_s;

        while (true) {
            searchNode = minPQ.delMin();
            searchNode_t = minPQ_t.delMin();

            if (searchNode.board.isGoal() || searchNode_t.board.isGoal()) {
                break;
            }

            for (Board b : searchNode.board.neighbors()) {
                if (searchNode.predecessor != null && searchNode.predecessor.board.equals(b)) {
                    continue;
                }

                searchNode_s = new SearchNode();
                searchNode_s.board = b;
                searchNode_s.moves = searchNode.moves + 1;
                searchNode_s.predecessor = searchNode;
                searchNode_s.priority_manhattan = b.manhattan() + searchNode_s.moves;

                minPQ.insert(searchNode_s);

            }

            for (Board b : searchNode_t.board.neighbors()) {
                if (searchNode.predecessor != null && searchNode_t.predecessor.board.equals(b)) {
                    continue;
                }
                searchNode_s = new SearchNode();
                searchNode_s.board = b;
                searchNode_s.moves = searchNode_t.moves + 1;
                searchNode_s.predecessor = searchNode_t;
                searchNode_s.priority_manhattan = b.manhattan() + searchNode_s.moves;

                minPQ_t.insert(searchNode_s);
            }
        }

        if (searchNode.board.isGoal()) {
            this.solvable = true;
            this.number_moves = searchNode.moves;

            while (searchNode != null) {
                this.board_solution.add(searchNode.board);
                searchNode = searchNode.predecessor;
            }
        } else {
            this.solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.solvable) {
            return -1;
        }

        return this.number_moves;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.solvable) {
            return null;
        }

        return this.board_solution;
    }

    private static class BoardSolution implements Iterable<Board> {
        private Node first = null;

        private static class Node {
            Board board;
            Node next = null;
        }

        public void add(Board b) {
            Node old_first = first;
            first = new Node();
            first.board = b;
            first.next = old_first;
        }

        public Iterator<Board> iterator() {
            return new BoardSolutionIterator();
        }

        private class BoardSolutionIterator implements Iterator<Board> {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Board i = current.board;
                current = current.next;
                return i;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}