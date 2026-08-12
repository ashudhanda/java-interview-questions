# 48 — HashMap Coding Patterns 🗺️

HashMap internals topic 19 me hain. Ye topic **coding round** ke liye hai — wo patterns jo HashMap se `O(n²)` ko `O(n)` bana dete hain.

## Q1. Two Sum (unsorted array)
**Answer:** Sabse famous question. Brute force `O(n²)`, HashMap se `O(n)`.

```java
static int[] twoSum(int[] arr, int target) {
    Map<Integer, Integer> seen = new HashMap<>();   // value → index

    for (int i = 0; i < arr.length; i++) {
        int complement = target - arr[i];

        if (seen.containsKey(complement)) {
            return new int[]{seen.get(complement), i};
        }
        seen.put(arr[i], i);        // check ke BAAD daalo
    }
    return new int[]{-1, -1};
}
```

⚠️ `put` **check ke baad** karo. Pehle daaloge to `target = 8, arr[i] = 4` wale case me element khud se pair ban jaayega.

👉 Sorted array ho to two pointers better hai (`O(1)` space) — topic 42 dekho.

## Q2. Frequency count
**Answer:** Har jagah kaam aane wala base pattern.

```java
Map<Character, Integer> freq = new HashMap<>();

for (char c : str.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}

// Ya modern tareeka
for (char c : str.toCharArray()) {
    freq.merge(c, 1, Integer::sum);
}
```

👉 `getOrDefault()` aur `merge()` — dono jaanna chahiye. `merge()` zyada clean hai.

**Streams se:**
```java
Map<Character, Long> freq = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
```

## Q3. Anagram check karo
**Answer:** Do tareeke:

```java
// Tareeka 1: sort karke — O(n log n)
static boolean isAnagram(String a, String b) {
    char[] x = a.toCharArray(), y = b.toCharArray();
    Arrays.sort(x); Arrays.sort(y);
    return Arrays.equals(x, y);
}

// Tareeka 2: frequency count — O(n) ✅ better
static boolean isAnagram2(String a, String b) {
    if (a.length() != b.length()) return false;

    int[] count = new int[26];
    for (int i = 0; i < a.length(); i++) {
        count[a.charAt(i) - 'a']++;
        count[b.charAt(i) - 'a']--;      // ek hi loop me dono
    }
    for (int c : count) if (c != 0) return false;
    return true;
}
```

👉 **`int[26]` array "HashMap" ka kaam kar raha hai** — sirf lowercase letters hon to ye HashMap se tez hai (no hashing, no boxing). Ye optimization bolna accha lagta hai.

## Q4. Group Anagrams
**Answer:** Sorted string ko **key** bana do.

```java
static List<List<String>> groupAnagrams(String[] words) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String word : words) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);          // "eat" → "aet"

        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
    }
    return new ArrayList<>(groups.values());
}
```

👉 **`computeIfAbsent()`** — ye method yaad rakho. Iske bina 3 line ka null check likhna padta:
```java
if (!groups.containsKey(key)) groups.put(key, new ArrayList<>());
groups.get(key).add(word);
```

## Q5. Pehla non-repeating character
**Answer:** `LinkedHashMap` — order preserve karta hai.

```java
static char firstNonRepeating(String s) {
    Map<Character, Integer> freq = new LinkedHashMap<>();   // order matters!

    for (char c : s.toCharArray()) freq.merge(c, 1, Integer::sum);

    for (Map.Entry<Character, Integer> e : freq.entrySet()) {
        if (e.getValue() == 1) return e.getKey();
    }
    return '_';
}
```

⚠️ Yahan **`HashMap` galat answer dega** — usme insertion order preserve nahi hota. `LinkedHashMap` hi chahiye. Ye classic trap hai.

## Q6. Subarray sum equals K
**Answer:** **Prefix sum + HashMap** — ye advanced pattern hai, seekhne layak.

```java
static int subarraySum(int[] arr, int k) {
    Map<Integer, Integer> prefixCount = new HashMap<>();
    prefixCount.put(0, 1);              // khaali prefix — zaroori!

    int sum = 0, count = 0;
    for (int num : arr) {
        sum += num;

        // agar (sum - k) pehle dikha tha, to beech ka subarray = k
        count += prefixCount.getOrDefault(sum - k, 0);

        prefixCount.merge(sum, 1, Integer::sum);
    }
    return count;
}
```

**Logic:** `sum[0..j] - sum[0..i] = k` matlab `sum[i+1..j] = k`. Isliye har point pe `sum - k` dhoondho.

👉 **Ye sliding window se kyun nahi hoga?** Kyunki **negative numbers** ho sakte hain — window shrink karne pe sum badh bhi sakta hai. Isliye prefix sum chahiye. Ye baat bolna strong point hai.

⚠️ `prefixCount.put(0, 1)` bhoolna sabse common bug hai — tab shuru se start hone wale subarrays count nahi honge.

## Q7. Longest consecutive sequence
**Answer:** `O(n)` me — sort kiye bina.

```java
static int longestConsecutive(int[] arr) {
    Set<Integer> set = new HashSet<>();
    for (int num : arr) set.add(num);

    int longest = 0;
    for (int num : set) {
        if (set.contains(num - 1)) continue;    // sequence ka start nahi hai

        int curr = num, length = 1;
        while (set.contains(curr + 1)) { curr++; length++; }

        longest = Math.max(longest, length);
    }
    return longest;
}
```

👉 **`if (set.contains(num - 1)) continue;`** hi asli trick hai — sirf sequence ke **starting point** se count karo. Iske bina `O(n²)` ho jaata. Iske saath total kaam `O(n)` rehta hai.

## Q8. Duplicates dhoondho
**Answer:**

```java
// Koi duplicate hai ya nahi
static boolean hasDuplicate(int[] arr) {
    Set<Integer> seen = new HashSet<>();
    for (int num : arr) {
        if (!seen.add(num)) return true;    // add() false = already tha
    }
    return false;
}
```

👉 **`set.add()` `boolean` return karta hai** — `contains()` + `add()` do call karne ki zaroorat nahi. Chhota par accha optimization.

## Q9. Do arrays ka intersection
**Answer:**

```java
static int[] intersection(int[] a, int[] b) {
    Set<Integer> setA = Arrays.stream(a).boxed().collect(Collectors.toSet());
    Set<Integer> result = new HashSet<>();

    for (int num : b) {
        if (setA.contains(num)) result.add(num);
    }
    return result.stream().mapToInt(Integer::intValue).toArray();
}
```

👉 **Optimization:** chhoti array ko set banao — memory kam lagegi.

## Q10. HashMap ke wo methods jo game badal dete hain
**Answer:** Ye list yaad kar lo — code aadha ho jaata hai:

```java
map.getOrDefault(key, 0)                      // null check khatam
map.putIfAbsent(key, value)                   // sirf naya hone pe daalo
map.computeIfAbsent(key, k -> new ArrayList<>())  // list of lists ke liye
map.computeIfPresent(key, (k, v) -> v + 1)
map.merge(key, 1, Integer::sum)               // counter ke liye best
map.forEach((k, v) -> print(k + "=" + v))
map.entrySet().removeIf(e -> e.getValue() < 2)
```

⚠️ **`merge()` ka bonus:** agar remapping function `null` return kare, to entry **delete** ho jaati hai.

## Q11. Kab HashMap use karein — pehchan
**Answer:**

| Signal | Approach |
|---|---|
| "kitni baar aaya" / frequency | `HashMap` count |
| "pehle dekha hai kya" | `HashSet` |
| "pair with sum" (unsorted) | `HashMap` complement |
| "subarray with sum k" | Prefix sum + `HashMap` |
| Grouping / categorize | `computeIfAbsent` |
| Order bhi chahiye | `LinkedHashMap` |
| Sorted keys chahiye | `TreeMap` |
| Nested loop dikh raha hai | Sochoo — shayad HashMap se `O(n)` ho jaaye |

---

> 💡 **Interview me:** jab bhi nested loop likhne lago, ek second ruk ke socho — *"kya main pichhli values HashMap me store karke ek pass me kar sakta hoon?"* 70% cases me jawab **haan** hota hai. Yahi `O(n²)` → `O(n)` ka sabse common raasta hai.
