# 🟡 Strings & Arrays — 15 Questions

### Q1. Why are Strings immutable in Java?
**Answer:** Security (safe as map keys, class names, file paths), **string pool** memory sharing, thread-safety, and cached hashcode. Any "modification" creates a new String object.

---

### Q2. What is the String pool?
**Answer:** A special memory area where string **literals** are stored and reused.

```java
String a = "hi";              // pool
String b = "hi";              // same pool object
String c = new String("hi"); // new heap object
System.out.println(a == b);   // true
System.out.println(a == c);   // false
```

---

### Q3. String vs StringBuilder vs StringBuffer?
**Answer:**
| | String | StringBuilder | StringBuffer |
|--|--------|---------------|--------------|
| Mutable | ❌ | ✅ | ✅ |
| Thread-safe | ✅ (immutable) | ❌ | ✅ (synchronized) |
| Speed | Slow for concat | **Fastest** | Slower |

> Use **StringBuilder** for loops with concatenation.

---

### Q4. Reverse a string (without built-in reverse).
```java
static String reverse(String s) {
    char[] arr = s.toCharArray();
    int left = 0, right = arr.length - 1;
    while (left < right) {
        char tmp = arr[left];
        arr[left++] = arr[right];
        arr[right--] = tmp;
    }
    return new String(arr);
}
```

---

### Q5. Check if a string is a palindrome.
```java
static boolean isPalindrome(String s) {
    int i = 0, j = s.length() - 1;
    while (i < j) {
        if (s.charAt(i++) != s.charAt(j--)) return false;
    }
    return true;
}
```

---

### Q6. Count vowels and consonants in a string.
```java
static void count(String s) {
    int vowels = 0, consonants = 0;
    for (char c : s.toLowerCase().toCharArray()) {
        if (c >= 'a' && c <= 'z') {
            if ("aeiou".indexOf(c) >= 0) vowels++;
            else consonants++;
        }
    }
    System.out.println(vowels + " vowels, " + consonants + " consonants");
}
```

---

### Q7. Find the first non-repeated character.
```java
static char firstNonRepeated(String s) {
    Map<Character, Integer> freq = new LinkedHashMap<>();
    for (char c : s.toCharArray()) freq.merge(c, 1, Integer::sum);
    for (var e : freq.entrySet())
        if (e.getValue() == 1) return e.getKey();
    return '\0';
}
```

---

### Q8. Check if two strings are anagrams.
```java
static boolean isAnagram(String a, String b) {
    if (a.length() != b.length()) return false;
    int[] count = new int[26];
    for (int i = 0; i < a.length(); i++) {
        count[a.charAt(i) - 'a']++;
        count[b.charAt(i) - 'a']--;
    }
    for (int c : count) if (c != 0) return false;
    return true;
}
```

---

### Q9. Find the largest and second largest element in an array.
```java
static void twoLargest(int[] arr) {
    int max = Integer.MIN_VALUE, second = Integer.MIN_VALUE;
    for (int n : arr) {
        if (n > max) { second = max; max = n; }
        else if (n > second && n != max) second = n;
    }
    System.out.println("Largest: " + max + ", Second: " + second);
}
```

---

### Q10. Remove duplicates from an array.
```java
static int[] removeDuplicates(int[] arr) {
    return Arrays.stream(arr).distinct().toArray();
}
// Or without streams: use a LinkedHashSet to preserve order
```

---

### Q11. Two Sum — find indices of two numbers adding to a target.
```java
static int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> seen = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int need = target - nums[i];
        if (seen.containsKey(need)) return new int[]{seen.get(need), i};
        seen.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```
> O(n) time using HashMap — the classic interview favorite.

---

### Q12. Rotate an array to the right by k steps.
```java
static void rotate(int[] arr, int k) {
    k %= arr.length;
    reverse(arr, 0, arr.length - 1);
    reverse(arr, 0, k - 1);
    reverse(arr, k, arr.length - 1);
}
static void reverse(int[] a, int i, int j) {
    while (i < j) { int t = a[i]; a[i++] = a[j]; a[j--] = t; }
}
```

---

### Q13. Find the missing number from 1 to n.
```java
static int missingNumber(int[] arr, int n) {
    long expected = (long) n * (n + 1) / 2;
    long actual = 0;
    for (int x : arr) actual += x;
    return (int) (expected - actual);
}
```

---

### Q14. Merge two sorted arrays into one sorted array.
```java
static int[] merge(int[] a, int[] b) {
    int[] res = new int[a.length + b.length];
    int i = 0, j = 0, k = 0;
    while (i < a.length && j < b.length)
        res[k++] = (a[i] <= b[j]) ? a[i++] : b[j++];
    while (i < a.length) res[k++] = a[i++];
    while (j < b.length) res[k++] = b[j++];
    return res;
}
```

---

### Q15. Print frequency of each character in a string.
```java
static void charFrequency(String s) {
    Map<Character, Integer> freq = new TreeMap<>();
    for (char c : s.toCharArray()) freq.merge(c, 1, Integer::sum);
    freq.forEach((ch, count) -> System.out.println(ch + " → " + count));
}
```

---

⬅️ [Prev: OOPs Concepts](02-oops-concepts.md) | ➡️ [Next: Collections Framework](04-collections-framework.md)
