// Fibonacci series up to n terms
public class Fibonacci {
    public static void main(String[] args) {
        int n = 10, a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(a + " ");
            int next = a + b;
            a = b;
            b = next;
        }
        // 0 1 1 2 3 5 8 13 21 34
    }
}
