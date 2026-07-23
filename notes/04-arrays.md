# Chapter 4 — Arrays 🚂

> **Goal:** Store 100 values without creating 100 variables — and never fear `ArrayIndexOutOfBoundsException` again.

---

## 1. Why arrays?

Storing marks of 60 students as `int m1, m2, m3... m60;` = madness 🤯. An **array** is ONE variable that holds many values of the **same type**.

**Analogy 🚂:** An array is a **train** — one name, many coaches, and every coach has a fixed number. Coach numbering starts from **0**!

```java
int[] marks = {90, 85, 78, 92, 88};

 index:    0     1     2     3     4
        ┌────┬────┬────┬────┬────┐
marks → │ 90 │ 85 │ 78 │ 92 │ 88 │
        └────┴────┴────┴────┴────┘
```

## 2. Creating arrays (two ways)

```java
// Way 1: you know the values
int[] marks = {90, 85, 78, 92, 88};

// Way 2: you know only the size (values default to 0)
int[] scores = new int[5];    // [0, 0, 0, 0, 0]
scores[0] = 99;               // fill coach 0
```

> 💡 Remember Chapter 3: `new int[5]` → the array object lives on the **heap**; `scores` on the stack just holds its address. That's why arrays need `new` — it's dynamic memory allocation!

### Rules
- **Fixed size** — once created, a train can't add coaches (that's what ArrayList is for, later!)
- **Same type only** — an `int[]` can't hold a String
- **Index goes 0 to length-1** — for size 5: valid indexes are 0,1,2,3,4

## 3. Using arrays

```java
System.out.println(marks[0]);       // 90  (first element)
System.out.println(marks.length);   // 5   (no brackets! length is a property)
marks[2] = 80;                       // update coach 2

// Visit every coach — the classic loop:
for (int i = 0; i < marks.length; i++) {
    System.out.println(marks[i]);
}

// Shortcut when you don't need the index (for-each):
for (int m : marks) {
    System.out.println(m);
}
```

## 4. 💥 The Famous Error

```java
int[] a = new int[5];
a[5] = 10;   // 💥 ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
```

Size 5 = last index **4**. Trying coach 5 on a 5-coach train (0-4) → you fall off the train. 🚅 This error means: *check your loop condition* — it's almost always `<= length` written instead of `< length`.

## 5. 2D Arrays (array of arrays)

**Analogy 🏨:** A hotel — floors and rooms. `hotel[floor][room]`

```java
int[][] matrix = {
    {1, 2, 3},    // floor 0
    {4, 5, 6}     // floor 1
};
System.out.println(matrix[1][2]);   // 6 (floor 1, room 2)

for (int i = 0; i < matrix.length; i++) {          // each floor
    for (int j = 0; j < matrix[i].length; j++) {   // each room on that floor
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}
```

## 6. ❌ Common Beginner Mistakes

1. `marks.length()` → error — arrays use `.length` (no brackets); Strings use `.length()` (with brackets). Yes, it's annoying. 😅
2. Loop with `i <= arr.length` → crashes on the last iteration
3. `int[] a = new int[5]; System.out.println(a);` prints something like `[I@1b6d` — that's the **address**, not values! Use a loop or `Arrays.toString(a)`.
4. Expecting `b = a` to copy the array → it copies the reference (see Chapter 3!)

---

## 🏋️ Practice

**Q1. Find the sum and average of array elements.**

<details><summary>✅ Solution + dry run</summary>

```java
int[] marks = {90, 85, 78, 92, 88};
int sum = 0;
for (int m : marks) {
    sum += m;          // 0→90→175→253→345→433
}
double avg = sum / (double) marks.length;   // 433/5.0 = 86.6
System.out.println("Sum=" + sum + " Avg=" + avg);
```
**Why `(double)`?** Without it, 433/5 = 86 (int division trap from Chapter 2!).
</details>

**Q2. Find the largest element.**

<details><summary>✅ Solution + logic</summary>

```java
int[] a = {34, 78, 12, 95, 56};
int max = a[0];                  // assume first is largest
for (int i = 1; i < a.length; i++) {
    if (a[i] > max) max = a[i]; // found bigger? update!
}
System.out.println(max);         // 95
```
**Logic:** like checking heights in a line — remember the tallest seen so far, compare with each next person. Start `max` from `a[0]`, never from 0 (array could be all negative!).
</details>

**Q3. Reverse an array in place.**

<details><summary>✅ Solution + dry run</summary>

```java
int[] a = {1, 2, 3, 4, 5};
int left = 0, right = a.length - 1;
while (left < right) {
    int temp = a[left];   // swap ends, move inward
    a[left] = a[right];
    a[right] = temp;
    left++; right--;
}
// [5,2,3,4,1] → [5,4,3,2,1] → done (left meets right at index 2)
```
**Logic:** two friends walking from both ends of the train, swapping items, until they meet in the middle. Same swap trick as Chapter 2 Q1!
</details>

**Q4. Predict the output:**
```java
int[] a = new int[3];
System.out.println(a[0] + a.length);
```

<details><summary>✅ Answer</summary>

`3` — `new int[3]` fills with defaults, so `a[0]` is `0`, and `0 + 3 = 3`.
</details>

---

⬅️ [Prev: Memory — Stack vs Heap](03-memory-stack-vs-heap.md) | 🏠 [Index](../README.md)
