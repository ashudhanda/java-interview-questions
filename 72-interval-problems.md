# 72 — Interval Problems 📅

Meeting rooms, merge intervals, insert interval — ye ek hi family ke questions hain. Ek baar pattern samajh aa gaya, sab solve ho jaate hain.

## Q1. Interval question me pehla step kya hai?
**Answer:** **SORT karo.** Almost har interval question sort karne se shuru hota hai.

```java
Arrays.sort(intervals, (a, b) -> a[0] - b[0]);   // start time se sort
```

⚠️ Sort karne se pehle overlap check karna mushkil hai. Sort ke baad intervals **order me** aate hain — sirf bagal wale se compare karna padta hai.

## Q2. Merge Intervals — THE classic
**Answer:** Overlapping intervals ko jod do.

```java
static int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1-1] - b[0]);  // start se sort
    List<int[]> result = new ArrayList<>();

    int[] current = intervals[0];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] <= current[1]) {
            // Overlap! Merge karo — end ko max lo
            current[1] = Math.max(current[1], intervals[i][1]);
        } else {
            // No overlap — current save karo, naya start
            result.add(current);
            current = intervals[i];
        }
    }
    result.add(current);    // aakhri wala mat bhoolna!
    return result.toArray(new int[0][]);
}
```

`[[1,3],[2,6],[8,10],[15,18]]` → `[[1,6],[8,10],[15,18]]`

**Overlap condition:** `next.start <= current.end`
**Merge:** `current.end = max(current.end, next.end)`

⚠️ **`result.add(current)` loop ke baad** — aakhri interval bhoolna sabse common bug hai.

**Time:** `O(n log n)` (sort) | **Space:** `O(n)`

## Q3. Insert Interval
**Answer:** Sorted list me naya interval daalo, merge karte hue.

```java
static int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> result = new ArrayList<>();
    int i = 0, n = intervals.length;

    // Step 1: jo new se PEHLE khatam hote hain — as-is add
    while (i < n && intervals[i][1] < newInterval[0]) {
        result.add(intervals[i++]);
    }

    // Step 2: jo OVERLAP karte hain — merge
    while (i < n && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
        i++;
    }
    result.add(newInterval);

    // Step 3: jo BAAD me shuru hote hain — as-is add
    while (i < n) {
        result.add(intervals[i++]);
    }
    return result.toArray(new int[0][]);
}
```

👉 **3 phases:** pehle wale → overlap wale → baad wale. Sorted list hai to `O(n)` me ho jaata hai — sort karne ki zaroorat nahi!

## Q4. Meeting Rooms — attend all?
**Answer:** Kya ek person saari meetings attend kar sakta hai?

```java
static boolean canAttendMeetings(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < intervals[i - 1][1]) {
            return false;    // overlap mila — dono attend nahi ho sakti
        }
    }
    return true;
}
```

👉 Bas **overlap check** — sort ke baad sirf adjacent compare.

## Q5. Meeting Rooms II — kitne rooms chahiye?
**Answer:** Classic question — do tareeke:

**Tareeka 1: Sweep line (chronological events)**
```java
static int minMeetingRooms(int[][] intervals) {
    int[] starts = new int[intervals.length];
    int[] ends = new int[intervals.length];
    for (int i = 0; i < intervals.length; i++) {
        starts[i] = intervals[i][0];
        ends[i] = intervals[i][1];
    }
    Arrays.sort(starts);
    Arrays.sort(ends);

    int rooms = 0, maxRooms = 0, s = 0, e = 0;
    while (s < starts.length) {
        if (starts[s] < ends[e]) {
            rooms++;
            s++;
        } else {
            rooms--;
            e++;
        }
        maxRooms = Math.max(maxRooms, rooms);
    }
    return maxRooms;
}
```

**Tareeka 2: Min-Heap**
```java
static int minMeetingRoomsHeap(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> heap = new PriorityQueue<>();   // end times

    for (int[] meeting : intervals) {
        if (!heap.isEmpty() && heap.peek() <= meeting[0]) {
            heap.poll();    // room khaali ho gaya — reuse
        }
        heap.offer(meeting[1]);
    }
    return heap.size();
}
```

👉 Heap me **end times** rakho. Nayi meeting ke start se pehle koi end ho gaya → wo room free. Heap ka final size = rooms needed.

## Q6. Non-overlapping Intervals — minimum removals
**Answer:**

```java
static int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);   // END se sort — greedy!

    int count = 0;
    int end = intervals[0][1];

    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < end) {
            count++;                                  // overlap — remove
        } else {
            end = intervals[i][1];                    // no overlap — update end
        }
    }
    return count;
}
```

👉 **Greedy:** jo interval **sabse jaldi khatam** ho use rakho — baaki ke liye zyada jagah bachti hai. Ye Activity Selection (topic 59) ka hi roop hai.

⚠️ **Start se sort karoge to galat** — end se sort karna zaroori hai yahan.

## Q7. Interval List Intersections
**Answer:** Do sorted lists ke common intervals:

```java
static int[][] intervalIntersection(int[][] A, int[][] B) {
    List<int[]> result = new ArrayList<>();
    int i = 0, j = 0;

    while (i < A.length && j < B.length) {
        int start = Math.max(A[i][0], B[j][0]);   // late start
        int end = Math.min(A[i][1], B[j][1]);      // early end

        if (start <= end) {
            result.add(new int[]{start, end});     // intersection mila
        }

        if (A[i][1] < B[j][1]) i++;                // chhota end wala aage
        else j++;
    }
    return result.toArray(new int[0][]);
}
```

👉 **Intersection formula:** `start = max(starts)`, `end = min(ends)`. Agar `start <= end` → valid intersection.

## Q8. Employee Free Time
**Answer:** Sab employees ke common free slots:

```java
// Sab intervals ko flatten karo, sort karo, merge karo
// Merged intervals ke BEECH ke gaps = free time
static List<int[]> employeeFreeTime(List<List<int[]>> schedule) {
    List<int[]> all = new ArrayList<>();
    for (List<int[]> emp : schedule) all.addAll(emp);
    all.sort((a, b) -> a[0] - b[0]);

    List<int[]> result = new ArrayList<>();
    int end = all.get(0)[1];

    for (int i = 1; i < all.size(); i++) {
        if (all.get(i)[0] > end) {
            result.add(new int[]{end, all.get(i)[0]});  // gap mila = free time
        }
        end = Math.max(end, all.get(i)[1]);
    }
    return result;
}
```

## Q9. Interval questions ka decision tree
**Answer:**

```
Overlap wala question hai?
│
├── Merge / Insert           → Sort by START, merge adjacent
├── Attend all?              → Sort by START, check adjacent overlap
├── Min rooms                → Sweep line / Min-heap of ends
├── Min removals             → Sort by END (greedy)
├── Intersection of 2 lists  → Two pointers
└── Common free time         → Flatten + sort + find gaps
```

## Q10. Common galtiyan
**Answer:**

```java
// ❌ Sort bhool gaye
// intervals already sorted NAHI hote — hamesha pehle sort karo

// ❌ Overlap condition galat
intervals[i][0] < current[1]     // < se boundary miss hoti hai
intervals[i][0] <= current[1]    // ✅ <= (touching bhi overlap hai)

// ❌ Merge me end galat update
current[1] = intervals[i][1]              // galat — chhota ho sakta hai
current[1] = Math.max(current[1], intervals[i][1])  // ✅

// ❌ Last interval add karna bhool gaye (merge wale me)
```

---

> 💡 **Interview tip:** interval question sunte hi pehla sawaal poochho — *"intervals sorted hain ya mujhe sort karna hai?"* Phir bolo — *"main start time se sort karke linear scan karunga, overlap pe merge. Time O(n log n) sort ki wajah se."* Aur meeting rooms II me **min-heap of end times** — ye trick pata ho to interviewer impress hota hai kyunki ye non-obvious optimization hai.
