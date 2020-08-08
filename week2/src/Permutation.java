import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int n = 2; //Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String ss = StdIn.readString();
            rq.enqueue(ss);
        }

        for (int i = 0; i < n; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
