# 44 — Binary Search & Variants 🔎

Binary search sabko "pata" hota hai, par interview me **infinite loop** ya **off-by-one** bug me phas jaate hain. Aur asli test variants me hota hai — first occurrence, rotated array waghera.

## Q1. Binary search ka basic code likho
**Answer:**

```java
static int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;

    while (left <= right) {                    // <= zaroori hai!
        int mid = left + (right - left) / 2;    // overflow-safe

        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

**Time:** `O(log n)` | **Space:** `O(1)`

⚠️ **Pre-condition:** array **sorted** hona chahiye. Ye bolna mat bhoolna — interviewer specifically dekhta hai.

## Q2. `mid = (left + right) / 2` kyun galat hai?
**Answer:** **Integer overflow.** Agar `left` aur `right` dono bade hon, to `left + right` `int` ki limit (~2.1 billion) cross kar jaayega aur **negative** ban jaayega → `ArrayIndexOutOfBoundsException`.

```java
int left = 2_000_000_000, right = 2_000_000_000;
(left + right) / 2        // ❌ negative ho gaya!
left + (right - left) / 2  // ✅ safe — kabhi overflow nahi
```

👉 Ye bug **Java ki khud ki library me 9 saal** tak tha (`Arrays.binarySearch`), 2006 me fix hua. Ye kahani interview me sunana strong impression banata hai.

## Q3. `while (left <= right)` vs `while (left < right)` — kya farak?
**Answer:** Ye sabse common bug source hai.

- **`left <= right`** — jab `right = mid - 1` / `left = mid + 1` use kar rahe ho. Single element wala case bhi check hota hai.
- **`left < right`** — jab `right = mid` use kar rahe ho (boundary search). Loop khatam hone pe `left == right` hi answer hota hai.

⚠️ **Infinite loop ka trap:** `left < right` ke saath `right = mid` likha aur `left = mid` bhi likh diya → `mid` badalta hi nahi → hamesha loop. `left = mid + 1` hona hi chahiye.

## Q4. First occurrence (lower bound) kaise dhoondhein?
**Answer:** Duplicates hone pe **sabse pehla** index chahiye.

```java
static int firstOccurrence(int[] arr, int target) {
    int left = 0, right = arr.length - 1, result = -1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            result = mid;          // mil gaya, par...
            right = mid - 1;       // ...left me aur dhoondho
        } else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return result;
}
```

**Key insight:** match milne pe **turant return mat karo** — answer store karke usi direction me search continue karo.

## Q5. Last occurrence (upper bound)?
**Answer:** Bas ek line badalti hai — `right = mid - 1` ki jagah `left = mid + 1`.

```java
if (arr[mid] == target) {
    result = mid;
    left = mid + 1;        // right me aur dhoondho
}
```

👉 **Bonus:** `lastOccurrence - firstOccurrence + 1` = element ki **total count** in `O(log n)`. Ye follow-up question aata hai.

## Q6. Rotated sorted array me search kaise karein?
**Answer:** Classic question — `[4,5,6,7,0,1,2]` me `0` dhoondho.

```java
static int searchRotated(int[] arr, int target) {
    int left = 0, right = arr.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;

        if (arr[left] <= arr[mid]) {           // left half sorted hai
            if (target >= arr[left] && target < arr[mid]) right = mid - 1;
            else left = mid + 1;
        } else {                                // right half sorted hai
            if (target > arr[mid] && target <= arr[right]) left = mid + 1;
            else right = mid - 1;
        }
    }
    return -1;
}
```

**Logic:** rotated array me **kam se kam ek half hamesha properly sorted** hoti hai. Pehle wo pehchano, phir dekho target uske range me hai ya nahi.

## Q7. Rotated array ka minimum element?
**Answer:**

```java
static int findMin(int[] arr) {
    int left = 0, right = arr.length - 1;

    while (left < right) {                     // yahan < use hua
        int mid = left + (right - left) / 2;

        if (arr[mid] > arr[right]) left = mid + 1;   // min right me hai
        else right = mid;                            // min mid ya left me
    }
    return arr[left];
}
```

⚠️ Yahan `right = mid` hai (`mid - 1` nahi) — kyunki `mid` khud answer ho sakta hai. Isliye condition bhi `left < right` hai.

## Q8. Square root binary search se kaise nikalein?
**Answer:** "Answer pe binary search" — ye pattern samajhna zaroori hai.

```java
static int sqrt(int n) {
    if (n < 2) return n;
    int left = 1, right = n / 2, result = 0;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        long square = (long) mid * mid;         // long! overflow se bacho

        if (square == n) return mid;
        else if (square < n) { result = mid; left = mid + 1; }
        else right = mid - 1;
    }
    return result;   // floor value
}
```

👉 Yahan array hi nahi hai! Binary search **kisi bhi monotonic answer space** pe lag sakta hai — yahi advanced insight hai.

## Q9. Binary search kab use kar sakte hain?
**Answer:** Do conditions:
1. Search space **sorted** ya **monotonic** ho
2. `mid` dekh ke **aadha space discard** kar sako

Pehchanne ke signals:
- Array **sorted** likha ho
- `O(log n)` maanga jaaye
- "minimum/maximum value jisme condition satisfy ho"
- "kth smallest/largest"

## Q10. Java ki built-in `binarySearch` kya deti hai?
**Answer:**

```java
Arrays.binarySearch(arr, target);          // arrays ke liye
Collections.binarySearch(list, target);    // List ke liye
```

⚠️ **Do important baatein:**
1. **Na mile to negative** return karta hai: `-(insertionPoint) - 1`. Isliye `>= 0` check karo, `!= -1` nahi.
   ```java
   int i = Arrays.binarySearch(arr, 5);
   if (i >= 0) found(); else insertAt(-i - 1);
   ```
2. **Duplicates me kaunsa index milega, guarantee nahi hai.** First/last occurrence chahiye to khud likhna padega.

## Q11. `TreeMap` se binary search jaisa kaam?
**Answer:** Agar data structure choose kar sakte ho to manually likhne ki zaroorat nahi:

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.floorKey(25);      // 25 se chhota ya barabar, sabse bada
map.ceilingKey(25);    // 25 se bada ya barabar, sabse chhota
map.headMap(25);       // 25 se chhote saare
```

Andar se Red-Black tree hai — `O(log n)` guaranteed. (Details topic 38 me.)

---

> 💡 **Interview tip:** binary search likhne se pehle **teen cheezein** bolo — (1) array sorted hai, (2) `mid` overflow-safe formula se nikaal raha hoon, (3) loop condition `<=` hai kyunki `right = mid - 1` use kar raha hoon. Ye teen line bolne se interviewer ko lagta hai tumne ye pattern samajh ke yaad kiya hai, ratta nahi maara.
