// Count vowels and consonants in a string
public class CountVowels {
    public static void main(String[] args) {
        String s = "interview".toLowerCase();
        int vowels = 0, consonants = 0;
        for (char c : s.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                if ("aeiou".indexOf(c) >= 0) vowels++;
                else consonants++;
            }
        }
        System.out.println("Vowels: " + vowels + ", Consonants: " + consonants);
        // Vowels: 4, Consonants: 5
    }
}
