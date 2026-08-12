# 45 — Sorting Algorithms from Scratch 🔄

"Merge sort likh ke dikhao" — ye ab bhi poocha jaata hai. Aur "quick sort ka worst case kab aata hai?" pe zyadatar log atak jaate hain.

## Q1. Bubble Sort
**Answer:** Bagal wale elements compare karo aur swap karo. Sabse simple, sabse slow.

```java
static void bubbleSort(int[] arr) {
    int n = arr.length;

    for (int i = 0; i < n - 1; i++) {
        boolean swapped = false;

        for (int j = 0; j < n - 1 - i; j++) {      // -i : end already sorted
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }
        if (!swapped) break;                        // already sorted → jaldi nikal jao
    }
}
```

**Time:** best `O(n)` (swapped flag ki wajah se), avg/worst `O(n²)` | **Space:** `O(1)` | **Stable:** ✅

👉 `swapped` flag zaroor likhna — iske bina best case bhi `O(n²)` reh jaata hai. Interviewer yahi dekhta hai.

## Q2. Selection Sort
**Answer:** Har round me **sabse chhota** dhoondho aur aage rakh do.

```java
static void selectionSort(int[] arr) {
    for (int i = 0; i < arr.length - 1; i++) {
        int minIdx = i;

        for (int j = i + 1; j < arr.length; j++) {
            if (arr[j] < arr[minIdx]) minIdx = j;
        }
        int temp = arr[i];
        arr[i] = arr[minIdx];
        arr[minIdx] = temp;
    }
}
```

**Time:** hamesha `O(n²)` (sorted ho tab bhi!) | **Space:** `O(1)` | **Stable:** ❌

👉 **Khaas baat:** swaps sirf `O(n)` hote hain — baaki sab me zyada. Isliye jab **writing mehngi** ho (flash memory), tab ye kaam ka hai.

## Q3. Insertion Sort
**Answer:** Taash ke patte sort karne jaisa — har naya element apni sahi jagah pe ghusaa do.

```java
static void insertionSort(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
        int key = arr[i];
        int j = i - 1;

        while (j >= 0 && arr[j] > key) {       // bade elements ko aage khisakao
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}
```

**Time:** best `O(n)`, worst `O(n²)` | **Space:** `O(1)` | **Stable:** ✅

👉 **Chhoti arrays (< 10-15) pe ye quick sort se bhi tez hai** — isliye Java ka `Arrays.sort()` andar se chhote hisson pe insertion sort hi use karta hai.

## Q4. Merge Sort
**Answer:** Divide and conquer — aadha-aadha karo, sort karo, phir merge karo.

```java
static void mergeSort(int[] arr, int left, int right) {
    if (left >= right) return;                  // base case: 1 element

    int mid = left + (right - left) / 2;
    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}

static void merge(int[] arr, int left, int mid, int right) {
    int[] temp = new int[right - left + 1];
    int i = left, j = mid + 1, k = 0;

    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) temp[k++] = arr[i++];    // <= se stability aati hai
        else temp[k++] = arr[j++];
    }
    while (i <= mid) temp[k++] = arr[i++];
    while (j <= right) temp[k++] = arr[j++];

    System.arraycopy(temp, 0, arr, left, temp.length);
}
```

**Time:** hamesha `O(n log n)` | **Space:** `O(n)` | **Stable:** ✅

⚠️ `arr[i] <= arr[j]` me `=` **stability ke liye** hai. `<` likhoge to equal elements ka order badal jaayega. Ye detail interviewer notice karta hai.

## Q5. Quick Sort
**Answer:** Ek pivot chuno, chhote left me bade right me, phir dono side recursion.

```java
static void quickSort(int[] arr, int low, int high) {
    if (low >= high) return;

    int pivotIdx = partition(arr, low, high);
    quickSort(arr, low, pivotIdx - 1);
    quickSort(arr, pivotIdx + 1, high);
}

static int partition(int[] arr, int low, int high) {
    int pivot = arr[high];                      // last element pivot
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}
```

**Time:** avg `O(n log n)`, **worst `O(n²)`** | **Space:** `O(log n)` recursion stack | **Stable:** ❌

## Q6. Quick sort ka worst case kab aata hai?
**Answer:** Jab pivot hamesha **sabse chhota ya sabse bada** nikle — tab partition `1` aur `n-1` me batta hai, balanced nahi.

👉 **Ironic baat:** already **sorted array** pe (last element pivot lene par) quick sort ka **worst case** aata hai! Ye favourite trick question hai.

**Bachne ke tareeke:**
- **Random pivot** chuno
- **Median-of-three** — first, mid, last ka median lo
- Java ka **Dual-Pivot QuickSort** do pivots use karta hai

## Q7. Merge sort vs Quick sort — kaunsa better?
**Answer:**

| | Merge Sort | Quick Sort |
|---|---|---|
| Worst case | `O(n log n)` ✅ | `O(n²)` ❌ |
| Space | `O(n)` ❌ | `O(log n)` ✅ |
| Stable | ✅ | ❌ |
| Practice me speed | Dheema | **Tez** (better cache locality) |
| Linked list pe | ✅ Best | ❌ Kharab |

👉 **Jawab:** worst case guarantee ya stability chahiye → **merge sort**. Average speed aur kam memory chahiye → **quick sort**.

## Q8. Java andar se kya use karta hai?
**Answer:** Ye topic 34 me bhi tha, par yahan detail me:

- **`Arrays.sort(int[])`** (primitives) → **Dual-Pivot QuickSort**. Stability ki zaroorat nahi, memory bhi nahi lagti.
- **`Arrays.sort(Object[])`** / **`Collections.sort()`** → **TimSort** (merge sort + insertion sort). Stable hai — objects me ye zaroori hai.
- **`Arrays.parallelSort()`** → bade arrays pe multiple cores use karta hai

**TimSort ki khoobi:** real-world data me aksar hisse already sorted hote hain. TimSort un "runs" ko detect karke merge kar deta hai — isliye practically bahut tez hai.

## Q9. Counting Sort — `O(n)` sorting?
**Answer:** Haan, par sirf **limited range** ke integers pe. Comparison-based nahi hai isliye `O(n log n)` ki limit lagti hi nahi.

```java
static void countingSort(int[] arr, int maxVal) {
    int[] count = new int[maxVal + 1];

    for (int num : arr) count[num]++;           // frequency count

    int idx = 0;
    for (int i = 0; i <= maxVal; i++) {
        while (count[i]-- > 0) arr[idx++] = i;  // wapas bharo
    }
}
```

**Time:** `O(n + k)` | **Space:** `O(k)` — jahan `k` = range

⚠️ Range badi ho (jaise 1 se 10 lakh) to `k` ki wajah se ye bekaar ho jaata hai. "Marks 0-100 sort karo" jaise cases me perfect.

## Q10. Complexity comparison table
**Answer:** Ye table yaad kar lo — seedha poocha jaata hai:

| Algorithm | Best | Average | Worst | Space | Stable |
|---|---|---|---|---|---|
| Bubble | `O(n)` | `O(n²)` | `O(n²)` | `O(1)` | ✅ |
| Selection | `O(n²)` | `O(n²)` | `O(n²)` | `O(1)` | ❌ |
| Insertion | `O(n)` | `O(n²)` | `O(n²)` | `O(1)` | ✅ |
| Merge | `O(n log n)` | `O(n log n)` | `O(n log n)` | `O(n)` | ✅ |
| Quick | `O(n log n)` | `O(n log n)` | `O(n²)` | `O(log n)` | ❌ |
| Heap | `O(n log n)` | `O(n log n)` | `O(n log n)` | `O(1)` | ❌ |
| Counting | `O(n+k)` | `O(n+k)` | `O(n+k)` | `O(k)` | ✅ |

## Q11. Comparison sort `O(n log n)` se tez kyun nahi ho sakti?
**Answer:** Mathematical proof hai. `n` elements ke `n!` possible arrangements hote hain. Har comparison answer ko aadha karta hai. `n!` ko 1 tak laane ke liye kam se kam `log₂(n!)` comparisons chahiye — jo `O(n log n)` hota hai.

👉 Isliye counting/radix sort tez hain — wo compare karte hi nahi, values ko directly index ki tarah use karte hain.

---

> 💡 **Interview me:** real code me hamesha `Arrays.sort()` use karo. Ye algorithms **samajhne** ke liye hain — interviewer dekhta hai ki tumhe trade-offs pata hain ya nahi. "Quick sort average me tez hai par sorted input pe worst case deta hai" — aisa jawab hi alag dikhata hai.
