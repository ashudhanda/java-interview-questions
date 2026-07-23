# 🔴 Advanced Coding Problems — 13 Questions

### Q1. Fibonacci with memoization (avoid exponential recursion).
```java
static long fib(int n, long[] memo) {
    if (n <= 1) return n;
    if (memo[n] != 0) return memo[n];
    return memo[n] = fib(n - 1, memo) + fib(n - 2, memo);
}
// fib(50, new long[51]) — O(n) instead of O(2^n)
```

---

### Q2. Print all primes up to n (Sieve of Eratosthenes).
```java
static void sieve(int n) {
    boolean[] composite = new boolean[n + 1];
    for (int i = 2; (long) i * i <= n; i++)
        if (!composite[i])
            for (int j = i * i; j <= n; j += i) composite[j] = true;
    for (int i = 2; i <= n; i++)
        if (!composite[i]) System.out.print(i + " ");
}
```

---

### Q3. Factorial using recursion (with BigInteger for large n).
```java
static BigInteger factorial(int n) {
    return n <= 1 ? BigInteger.ONE
                  : BigInteger.valueOf(n).multiply(factorial(n - 1));
}
```

---

### Q4. Binary search (iterative).
```java
static int binarySearch(int[] arr, int target) {
    int lo = 0, hi = arr.length - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;  // avoids int overflow!
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) lo = mid + 1;
        else hi = mid - 1;
    }
    return -1;
}
```

---

### Q5. QuickSort implementation.
```java
static void quickSort(int[] a, int lo, int hi) {
    if (lo >= hi) return;
    int pivot = a[hi], i = lo - 1;
    for (int j = lo; j < hi; j++)
        if (a[j] < pivot) { int t = a[++i]; a[i] = a[j]; a[j] = t; }
    int t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
    quickSort(a, lo, i);
    quickSort(a, i + 2, hi);
}
```

---

### Q6. Longest substring without repeating characters (sliding window).
```java
static int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> lastSeen = new HashMap<>();
    int max = 0, start = 0;
    for (int end = 0; end < s.length(); end++) {
        char c = s.charAt(end);
        if (lastSeen.containsKey(c) && lastSeen.get(c) >= start)
            start = lastSeen.get(c) + 1;
        lastSeen.put(c, end);
        max = Math.max(max, end - start + 1);
    }
    return max;
}
```

---

### Q7. Check balanced parentheses using a stack.
```java
static boolean isBalanced(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : s.toCharArray()) {
        switch (c) {
            case '(' -> stack.push(')');
            case '[' -> stack.push(']');
            case '{' -> stack.push('}');
            default -> {
                if (stack.isEmpty() || stack.pop() != c) return false;
            }
        }
    }
    return stack.isEmpty();
}
```

---

### Q8. Reverse a linked list (iterative).
```java
static Node reverse(Node head) {
    Node prev = null;
    while (head != null) {
        Node next = head.next;
        head.next = prev;
        prev = head;
        head = next;
    }
    return prev;
}
```

---

### Q9. Detect a cycle in a linked list (Floyd's algorithm).
```java
static boolean hasCycle(Node head) {
    Node slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;        // 1 step
        fast = fast.next.next;   // 2 steps
        if (slow == fast) return true;
    }
    return false;
}
```
> Tortoise 🐢 and hare 🐇 — if there's a loop, fast eventually laps slow.

---

### Q10. Implement a stack using two queues.
```java
class StackViaQueue {
    private Queue<Integer> q = new LinkedList<>();
    public void push(int x) {
        q.add(x);
        for (int i = 1; i < q.size(); i++) q.add(q.remove()); // rotate
    }
    public int pop() { return q.remove(); }
    public int top() { return q.peek(); }
}
```

---

### Q11. Design an LRU Cache.
```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    LRUCache(int capacity) {
        super(capacity, 0.75f, true);   // true = access order
        this.capacity = capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

---

### Q12. Thread-safe Singleton (double-checked locking).
```java
class Singleton {
    private static volatile Singleton instance;
    private Singleton() { }
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) instance = new Singleton();
            }
        }
        return instance;
    }
}
// Even simpler: use an enum — enum Singleton { INSTANCE; }
```

---

### Q13. How do you create an immutable class?
**Answer:**
1. Declare the class `final` (no subclassing).
2. All fields `private final`.
3. No setters; initialize via constructor.
4. **Defensive copies** for mutable fields (lists, dates) in constructor AND getters.

```java
final class Person {
    private final String name;
    private final List<String> hobbies;
    Person(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = List.copyOf(hobbies); // defensive copy
    }
    public List<String> getHobbies() { return hobbies; } // already unmodifiable
}
// Or just use a record!
```

---

⬅️ [Prev: Java 8+](07-java8-and-beyond.md) | 🏠 [Back to Index](README.md)
