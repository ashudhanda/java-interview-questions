// Check if a number is an Armstrong number (e.g. 153 = 1^3 + 5^3 + 3^3)
public class ArmstrongNumber {
    public static boolean isArmstrong(int n) {
        int original = n, sum = 0;
        int digits = String.valueOf(n).length();
        while (n > 0) {
            int d = n % 10;
            sum += (int) Math.pow(d, digits);
            n /= 10;
        }
        return sum == original;
    }

    public static void main(String[] args) {
        System.out.println(isArmstrong(153)); // true
        System.out.println(isArmstrong(123)); // false
    }
}
