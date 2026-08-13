# 61 — Heap & PriorityQueue Patterns ⛰️

Heap wala topic sabse **underrated** hai. Top-K problems, scheduling, merge questions — sab me heap lagta hai. Aur Java me `PriorityQueue` ne aadha kaam asaan kar diya hai.

## Q1. Heap hota kya hai?
**Answer:** Complete binary tree jisme:

- **Min-Heap:** parent **chhota** hai bachon se → root pe sabse chhota
- **Max-Heap:** parent **bada** hai bachon se → root pe sabse bada

⚠️ **Sorted nahi hota!** Sirf root guaranteed minimum/maximum hai. Baaki ka order kuch bhi ho sakta hai.

**Operations:**

| Operation | Time |
|---|---|
| `peek()` — root dekho | `O(1)` |
| `poll()` — root nikalo | `O(log n)` |
| `offer()` — insert | `O(log n)` |
| Build heap from array | `O(n)` |

## Q2. Java me `PriorityQueue` — basics
**Answer:**

```java
// Min-heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);

minHeap.offer(5);
minHeap.offer(1);
minHeap.offer(3);

minHeap.peek();    // 1 (sabse chhota)
minHeap.poll();    // 1 nikal gaya
minHeap.peek();    // 3
```

⚠️ **Do important baatein:**
1. **`PriorityQueue` sorted nahi hota** — sirf `peek()`/`poll()` order deta hai. Print karoge to random order dikhega.
2. **`null` daalne pe `NullPointerException`** — allowed hi nahi.

## Q3. Kth Largest Element
**Answer:** Sabse famous heap question.

```java
static int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll();        // sabse chhota hata do — heap size = k rehta hai
        }
    }
    return minHeap.peek();         // top pe kth largest hai
}
```

**Logic:** Min-heap me **sirf k elements** rakho. Har baar smallest nikal do. End me root = **kth largest**.

👉 **Min-heap kyun?** Kyunki humein k largest chahiye — min-heap me sabse chhota **upar** rehta hai, usse hata do. Max-heap me k largest maintain nahi kar sakte easily.

**Time:** `O(n log k)` | **Space:** `O(k)`

⚠️ Sort se `O(n log n)` hota — heap `O(n log k)` hai, **k chhota ho to better**.

## Q4. Top K Frequent Elements
**Answer:**

```java
static int[] topKFrequent(int[] nums, int k) {
    // Step 1: frequency count
    Map<Integer, Integer> freq = new HashMap<>();
    for (int num : nums) freq.merge(num, 1, Integer::sum);

    // Step 2: min-heap by frequency
    PriorityQueue<Map.Entry<Integer, Integer>> minHeap = 
        new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

    for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
        minHeap.offer(entry);
        if (minHeap.size() > k) minHeap.poll();
    }

    // Step 3: collect
    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) result[i] = minHeap.poll().getKey();
    return result;
}
```

👉 **Pattern yaad rakho:** frequency + min-heap = top-K problems ka 90% solution.

## Q5. Merge K Sorted Lists
**Answer:**

```java
static ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> minHeap = 
        new PriorityQueue<>((a, b) -> a.data - b.data);

    for (ListNode head : lists) {
        if (head != null) minHeap.offer(head);    // har list ka head daalo
    }

    ListNode dummy = new ListNode(0);
    ListNode tail = dummy;

    while (!minHeap.isEmpty()) {
        ListNode smallest = minHeap.poll();        // sabse chhota head
        tail.next = smallest;
        tail = tail.next;

        if (smallest.next != null) {
            minHeap.offer(smallest.next);          // us list ka agla
        }
    }
    return dummy.next;
}
```

👉 **Heap size hamesha `k` se zyada nahi** — har list se sirf ek node andar. Isliye `O(n log k)`.

## Q6. Find Median from Stream
**Answer:** Do heaps — **max-heap** (left half) + **min-heap** (right half).

```java
class MedianFinder {
    PriorityQueue<Integer> left = new PriorityQueue<>((a, b) -> b - a);   // max-heap
    PriorityQueue<Integer> right = new PriorityQueue<>();                   // min-heap

    void addNum(int num) {
        left.offer(num);
        right.offer(left.poll());          // left ka max right me

        if (right.size() > left.size()) {
            left.offer(right.poll());      // balance karo
        }
    }

    double findMedian() {
        if (left.size() > right.size()) return left.peek();
        return (left.peek() + right.peek()) / 2.0;
    }
}
```

**Logic:**
- `left` = chhota aadha (max-heap, top pe sabse bada)
- `right` = bada aadha (min-heap, top pe sabse chhota)
- Median = dono ke tops ka average (ya left ka top)

**Time:** add `O(log n)`, median `O(1)`

## Q7. K Closest Points to Origin
**Answer:** Max-heap of size k.

```java
static int[][] kClosest(int[][] points, int k) {
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> distance(b) - distance(a)    // max-heap by distance
    );

    for (int[] p : points) {
        maxHeap.offer(p);
        if (maxHeap.size() > k) maxHeap.poll();  // sabse door wala hatao
    }

    int[][] result = new int[k][];
    for (int i = 0; i < k; i++) result[i] = maxHeap.poll();
    return result;
}

static int distance(int[] p) {
    return p[0] * p[0] + p[1] * p[1];    // sqrt ki zaroorat nahi — comparison ke liye
}
```

👉 **K closest → max-heap** (door wala hatao). **K farthest → min-heap** (paas wala hatao). Ulta sochna hai.

## Q8. Task Scheduler
**Answer:** Same task `n` gap pe — minimum time?

```java
static int leastInterval(char[] tasks, int n) {
    int[] freq = new int[26];
    for (char t : tasks) freq[t - 'A']++;

    int maxFreq = 0;
    for (int f : freq) maxFreq = Math.max(maxFreq, f);

    int maxCount = 0;
    for (int f : freq) if (f == maxFreq) maxCount++;

    // Formula: (maxFreq - 1) slots × (n + 1) + maxCount
    int result = (maxFreq - 1) * (n + 1) + maxCount;
    return Math.max(result, tasks.length);    // kam se kam tasks.length to chahiye
}
```

👉 Ye **greedy + math** hai — heap nahi, direct formula. Par heap se bhi hota hai (simulate karo) — `O(n log 26)`.

## Q9. Heap sort karo
**Answer:** `PriorityQueue` se ek line me:

```java
static void heapSort(int[] arr) {
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    for (int num : arr) heap.offer(num);
    for (int i = 0; i < arr.length; i++) arr[i] = heap.poll();
}
```

**Time:** `O(n log n)` | **Space:** `O(n)`

⚠️ Ye **in-place nahi** hai — `O(n)` extra space laga. Asli heap sort array me hi hota hai (build-heap + extract). Interview me poochhe to manually implement karna aana chahiye.

## Q10. Heap kab use karein — pehchan
**Answer:**

| Signal | Approach |
|---|---|
| "Kth largest/smallest" | Heap (size k) |
| "Top K frequent/closest" | Heap |
| "Merge K sorted" | Heap |
| "Median from stream" | Do heaps |
| "Minimum/maximum at every step" | Heap |
| "Scheduling with constraints" | Heap + greedy |
| Continuous stream se query | Heap (sorted nahi rakhna) |

## Q11. Common galtiyan
**Answer:**

```java
// ❌ PriorityQueue ko ArrayList ki tarah iterate kiya
for (int val : heap) System.out.println(val);   // random order!

// ✅ Poll se nikalo — sorted milega
while (!heap.isEmpty()) System.out.println(heap.poll());

// ❌ Integer overflow in comparator
(a, b) -> a - b        // Integer.MIN_VALUE - 1 = overflow!

// ✅ Safe comparator
Integer.compare(a, b)
(a, b) -> Integer.compare(b, a)    // max-heap
```

⚠️ `a - b` overflow kar sakta hai — `Integer.compare()` ya `Comparator.reverseOrder()` use karo.

---

> 💡 **Interview tip:** "K largest/smallest" sunte hi **heap** bolna — 95% sahi hoga. Aur heap ka size `k` rakhna hi trick hai — poora array heap me mat daalo. Interviewer poochhega *"kyun?"* — tab bolo *"kyunki mujhe sirf top k chahiye, baaki bekaar hain. Heap ka size `k` rakha to time `O(n log k)` ho gaya, poora daalne se `O(n log n)` ho jaata."* Ye optimization dikhana important hai.
