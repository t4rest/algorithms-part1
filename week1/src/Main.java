public class Main {
    public static void main(String[] arg) {
       System.out.println("hi there");


    }
}

// 0  1  2  3    11 12 13 14
// 4  5  6  7    21 22 23 23
// 8  9  10 11   31 32 33 34
// 12 13 14 15   41 42 43 44
//
// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
//
// 16 // top virtual site (N*N)
// 17 // bottom virtual site (N*N+1)
//
// twoD2oneD = (row - 1) * n + col - 1

