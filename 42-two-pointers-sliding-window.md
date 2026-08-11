# 42 — Two Pointers & Sliding Window 🎯

Coding round me `O(n²)` brute force likh dena aur `O(n)` solution likhna — isi se selection decide hota hai. Ye do patterns aadhe array/string problems solve kar dete hain.

---

## Part 1 — Two Pointers

## Q1. Two pointers pattern kya hai?
**Answer:** Ek hi array pe **do index** rakho aur unhe smartly move karo — taaki nested loop ki zaroorat hi na pade.

Do main variants:
- **Opposite ends** — `left = 0`, `right = n-1`, beech me milte hain (sorted array me)
- **Same direction** — dono aage badhte hain, alag speed se (fast/slow)

## Q2. Sorted array me pair with given sum
**Answer:** Brute force `O(n²)` hai. Two pointers se `O(n)`.

```java
static int[] twoSumSorted(int[] arr, int target) {
    int left = 0, right = arr.length - 1;

    while (left < right) {
        int sum = arr[left] + arr[right];

        if (sum == target) return new int[]{left, right};
        else if (sum < target) left++;     // aur bada chahiye
        else right--;                       // aur chhota chahiye
    }
    return new int[]{-1, -1};
}
```

**Logic:** array sorted hai, isliye sum chhota hai to `left` badhao (bada number lo), bada hai to `right` ghatao. Har step pe ek possibility permanently khatam ho jaati hai.

**Time:** `O(n)` | **Space:** `O(1)`

⚠️ Array **sorted nahi** hai to ye kaam nahi karega — tab `HashMap` wala classic Two Sum use karo.

## Q3. Palindrome check
**Answer:**

```java
static boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;

    while (left < right) {
        if (s.charAt(left) != s.charAt(right)) return false;
        left++;
        right--;
    }
    return true;
}
```

**Follow-up** (bahut poocha jaata hai): sirf alphanumeric dekho, case ignore karo:
```java
while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right)))
    return false;
```

## Q4. Sorted array se duplicates remove karo (in-place)
**Answer:** Slow/fast pointer ka classic use.

```java
static int removeDuplicates(int[] arr) {
    if (arr.length == 0) return 0;

    int slow = 0;                            // last unique ki position
    for (int fast = 1; fast < arr.length; fast++) {
        if (arr[fast] != arr[slow]) {
            slow++;
            arr[slow] = arr[fast];
        }
    }
    return slow + 1;                         // unique elements ki count
}
```

**Time:** `O(n)` | **Space:** `O(1)` — koi nayi array nahi banayi.

## Q5. Array me linked-list cycle detect karna (Floyd's algorithm)
**Answer:** Fast pointer 2 kadam, slow 1 kadam. Cycle hui to dono milenge — "tortoise and hare".

```java
static boolean hasCycle(Node head) {
    Node slow = head, fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) return true;       // mil gaye = cycle hai
    }
    return false;
}
```

**Space `O(1)`** — `HashSet` wale solution se yahi better hai.

---

## Part 2 — Sliding Window

## Q6. Sliding window pattern kya hai?
**Answer:** Ek "window" (subarray/substring) ko array pe **slide** karo. Har baar poora dobara calculate karne ki jagah, **jo nikla wo minus, jo aaya wo plus**.

Do types:
- **Fixed size** — window ka size constant (k)
- **Variable size** — condition ke hisaab se window chhoti-badi hoti hai

## Q7. Fixed window: size k ka maximum sum
**Answer:**

```java
static int maxSumSubarray(int[] arr, int k) {
    int windowSum = 0;

    for (int i = 0; i < k; i++) windowSum += arr[i];   // pehla window

    int maxSum = windowSum;
    for (int i = k; i < arr.length; i++) {
        windowSum += arr[i] - arr[i - k];              // naya add, purana minus
        maxSum = Math.max(maxSum, windowSum);
    }
    return maxSum;
}
```

👉 Yahi **core trick** hai: har window ka sum dobara `O(k)` me nikalne ki jagah `O(1)` me update kar liya. `O(n*k)` se `O(n)`.

## Q8. Variable window: longest substring without repeating characters
**Answer:** Ye sabse famous sliding window question hai.

```java
static int longestUnique(String s) {
    Map<Character, Integer> lastSeen = new HashMap<>();
    int maxLen = 0, start = 0;

    for (int end = 0; end < s.length(); end++) {
        char c = s.charAt(end);

        if (lastSeen.containsKey(c) && lastSeen.get(c) >= start) {
            start = lastSeen.get(c) + 1;        // window ko aage khiska do
        }
        lastSeen.put(c, end);
        maxLen = Math.max(maxLen, end - start + 1);
    }
    return maxLen;
}
```

`"abcabcbb"` → `3` (`"abc"`)

⚠️ `lastSeen.get(c) >= start` wali condition **zaroori** hai — warna window ke bahar wale purane character ki wajah se `start` peeche chala jaayega. Ye sabse common bug hai is question me.

## Q9. Variable window: sum >= target wala smallest subarray
**Answer:** Standard expand-shrink template.

```java
static int minSubArrayLen(int target, int[] arr) {
    int start = 0, sum = 0, minLen = Integer.MAX_VALUE;

    for (int end = 0; end < arr.length; end++) {
        sum += arr[end];                        // window badhao

        while (sum >= target) {                 // condition puri? ab chhoti karo
            minLen = Math.min(minLen, end - start + 1);
            sum -= arr[start];
            start++;
        }
    }
    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}
```

👉 **Ye universal template hai:** bahar wala loop window **expand** karta hai, andar wala `while` **shrink** karta hai. Har element zyada se zyada 2 baar touch hota hai → `O(n)`, `O(n²)` nahi.

## Q10. Kaunsa pattern kab pehchanein?
**Answer:** Ye recognition table interview me time bacha deti hai:

| Question me ye dikhe | Pattern |
|---|---|
| **Sorted** array + pair/triplet dhoondho | Two pointers (opposite ends) |
| Palindrome / reverse | Two pointers (opposite ends) |
| In-place duplicates hatao / rearrange | Two pointers (slow-fast) |
| Cycle detection | Fast & slow pointer |
| "Size k ka subarray/substring" | Fixed sliding window |
| "Longest / shortest subarray jisme..." | Variable sliding window |
| "Contiguous" shabd aaye | Sliding window |
| Subsequence (contiguous nahi) | ❌ Sliding window nahi — DP |

⚠️ **Important trap:** sliding window sirf **contiguous** (lagatar) elements pe kaam karta hai. "Subsequence" ka matlab beech ke elements chhod sakte ho — wahan ye pattern fail ho jaayega.

⚠️ **Negative numbers wala trap:** "sum >= target" wala shrink logic sirf tab kaam karta hai jab saare numbers **positive** hon. Negative allowed hai to window shrink karne pe sum badh bhi sakta hai — tab **prefix sum + HashMap** use karo.

---

> 💡 **Interview strategy:** Pehle brute force bolo ("`O(n²)` me nested loop se ho jaayega"), phir bolo *"par ise two pointers se `O(n)` me kar sakte hain"* — aur optimize karke likho. Interviewer ko yahi thought process chahiye hota hai, seedha optimal code nahi.
