# 23 — String Coding Questions 💻

Classic string problems jo har interview me aate hain — with clean solutions.

## Q1. Reverse a string.
```java
String s = "hello";
String reversed = new StringBuilder(s).reverse().toString();
// "olleh"
```
Manual (interview me bina built-in ke poochhein to):
```java
char[] chars = s.toCharArray();
for (int i = 0, j = chars.length - 1; i < j; i++, j--) {
    char temp = chars[i]; chars[i] = chars[j]; chars[j] = temp;
}
```

## Q2. Check palindrome.
```java
boolean isPalindrome(String s) {
    int i = 0, j = s.length() - 1;
    while (i < j) {
        if (s.charAt(i) != s.charAt(j)) return false;
        i++; j--;
    }
    return true;
}
```
Two-pointer technique — O(n) time, O(1) space.

## Q3. Count vowels and consonants.
```java
String s = "programming".toLowerCase();
int vowels = 0, consonants = 0;
for (char c : s.toCharArray()) {
    if ("aeiou".indexOf(c) >= 0) vowels++;
    else if (Character.isLetter(c)) consonants++;
}
```

## Q4. Check anagrams.
```java
boolean isAnagram(String a, String b) {
    char[] x = a.toCharArray(), y = b.toCharArray();
    Arrays.sort(x); Arrays.sort(y);
    return Arrays.equals(x, y);
}
```
**Better (O(n)):** 26-size frequency array se count compare karo — interviews me ye follow-up aata hai.

## Q5. First non-repeated character.
```java
Map<Character, Integer> count = new LinkedHashMap<>();
for (char c : s.toCharArray()) count.merge(c, 1, Integer::sum);

char result = count.entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .findFirst().get().getKey();
```
`LinkedHashMap` insertion order preserve karta hai — isliye "first" wala logic kaam karta hai. ⭐

## Q6. Reverse each word in a sentence.
```java
String sentence = "java is fun";
String result = Arrays.stream(sentence.split(" "))
        .map(w -> new StringBuilder(w).reverse().toString())
        .collect(Collectors.joining(" "));
// "avaj si nuf"
```

## Q7. Check if a string contains only digits.
```java
boolean onlyDigits = s.chars().allMatch(Character::isDigit);
// ya regex: s.matches("\\d+")
```

## Q8. Remove duplicate characters (order preserved).
```java
String result = s.chars()
        .distinct()
        .collect(StringBuilder::new,
                 StringBuilder::appendCodePoint,
                 StringBuilder::append)
        .toString();
```
Loop version: `LinkedHashSet<Character>` me add karke join karo.

## Q9. Find duplicate characters with counts.
```java
Map<Character, Long> dups = s.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
```

## Q10. String immutability trick question.
```java
String s = "hello";
s.concat(" world");
System.out.println(s); // ???
```
**Output:** `hello` — concat ne **naya** object banaya jo assign hi nahi hua. String immutable hai; result `s = s.concat(...)` karna padega. Ye question 90% candidates confuse karta hai! 🎯