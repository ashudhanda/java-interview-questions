# 74 — Streams & Collectors Advanced 🌊

Topic 18 me Stream coding tha, 21 me lambdas. Ab **Collectors** ka deep dive — `groupingBy`, `partitioningBy`, `collectingAndThen` — yahan senior-level questions aate hain.

## Q1. `Collectors.groupingBy()` — sabse powerful
**Answer:** Data ko **groups** me baanto — SQL ka GROUP BY jaisa.

```java
List<Employee> employees = List.of(
    new Employee("Ashu", "IT", 70000),
    new Employee("Rahul", "IT", 60000),
    new Employee("Priya", "HR", 90000)
);

// Department ke hisaab se group
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept));
// {IT=[Ashu, Rahul], HR=[Priya]}

// Group + count
Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));
// {IT=2, HR=1}

// Group + average salary
Map<String, Double> avgSalary = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
        Collectors.averagingDouble(Employee::getSalary)));
// {IT=65000.0, HR=90000.0}
```

👉 **`groupingBy(key, downstream)`** — doosra parameter batata hai har group pe kya karna hai. Yahi se complexity aati hai.

## Q2. `partitioningBy()` vs `groupingBy()`
**Answer:**

```java
// partitioningBy — sirf 2 groups (true/false)
Map<Boolean, List<Employee>> highEarners = employees.stream()
    .collect(Collectors.partitioningBy(e -> e.getSalary() > 65000));
// {false=[Rahul], true=[Ashu, Priya]}
```

| | `partitioningBy` | `groupingBy` |
|---|---|---|
| Groups | Hamesha **2** (true/false) | Kitne bhi |
| Keys | Sirf `Boolean` | Koi bhi type |
| Speed | Thoda tez | Normal |
| Kab use | Pass/fail, active/inactive | Department, city, category |

## Q3. Downstream collectors — nested grouping
**Answer:**

```java
// Dept → salary sorted list
Map<String, List<Employee>> byDeptSorted = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
        Collectors.collectingAndThen(
            Collectors.toList(),
            list -> list.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .toList()
        )));

// Dept → sabse zyada salary wala employee
Map<String, Optional<Employee>> topEarner = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
        Collectors.maxBy(Comparator.comparing(Employee::getSalary))));

// Dept → sirf naam (objects nahi)
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept,
        Collectors.mapping(Employee::getName, Collectors.toList())));
// {IT=[Ashu, Rahul], HR=[Priya]}
```

👉 **`Collectors.mapping()`** — group me objects ki jagah sirf ek field collect karo. Bahut useful.

## Q4. `collectingAndThen()` kya karta hai?
**Answer:** Collect karne ke **baad** ek final transformation:

```java
// Unmodifiable list chahiye
List<String> names = employees.stream()
    .map(Employee::getName)
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    ));

// Max nikalo aur Optional unwrap karo
double maxSalary = employees.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.maxBy(Comparator.comparing(Employee::getSalary)),
        opt -> opt.map(Employee::getSalary).orElse(0.0)
    ));
```

## Q5. Frequency count — teen tareeke
**Answer:**

```java
List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");

// Tareeka 1: groupingBy + counting
Map<String, Long> freq1 = words.stream()
    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

// Tareeka 2: toMap
Map<String, Integer> freq2 = words.stream()
    .collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));

// Tareeka 3: simple loop (kabhi kabhi yahi best hai)
Map<String, Integer> freq3 = new HashMap<>();
for (String w : words) freq3.merge(w, 1, Integer::sum);
```

👉 Interview me **teesra tareeka bhi batao** — dikhata hai tum sirf streams ke fan nahi ho, practical ho.

## Q6. `toMap()` ka duplicate key trap
**Answer:**

```java
// ❌ Duplicate key = IllegalStateException!
Map<String, String> map = employees.stream()
    .collect(Collectors.toMap(Employee::getName, Employee::getDept));
    // Agar do "Ashu" hain → CRASH

// ✅ Merge function do
Map<String, String> safe = employees.stream()
    .collect(Collectors.toMap(
        Employee::getName,
        Employee::getDept,
        (existing, replacement) -> existing    // duplicate pe purana rakho
    ));
```

⚠️ Ye production me bahut aata hai — database se duplicate naam aa jaaye to poora API crash. Hamesha merge function do.

## Q7. `flatMap()` — nested lists flatten karo
**Answer:**

```java
List<List<String>> nested = List.of(
    List.of("a", "b"),
    List.of("c", "d"),
    List.of("e")
);

List<String> flat = nested.stream()
    .flatMap(List::stream)
    .toList();
// [a, b, c, d, e]

// Real example — sab employees ke skills
List<String> allSkills = employees.stream()
    .flatMap(e -> e.getSkills().stream())
    .distinct()
    .toList();
```

👉 **`map` vs `flatMap`:** `map` = 1 input → 1 output. `flatMap` = 1 input → **stream of outputs**, sab milke ek flat stream.

## Q8. `reduce()` — sabse flexible
**Answer:**

```java
// Sum
int total = numbers.stream().reduce(0, Integer::sum);

// Max
Optional<Integer> max = numbers.stream().reduce(Integer::max);

// String concat
String joined = words.stream().reduce("", (a, b) -> a + "," + b);

// Custom object
Employee richest = employees.stream()
    .reduce((e1, e2) -> e1.getSalary() > e2.getSalary() ? e1 : e2)
    .orElseThrow();
```

👉 **`reduce` teen forms:**
1. `reduce(identity, accumulator)` — identity se shuru
2. `reduce(accumulator)` — Optional return
3. `reduce(identity, accumulator, combiner)` — parallel streams ke liye

## Q9. Useful short operations — cheat sheet
**Answer:**

```java
// Joining strings
String csv = names.stream().collect(Collectors.joining(", "));

// Summary statistics — ek hi pass me sab!
DoubleSummaryStatistics stats = employees.stream()
    .collect(Collectors.summarizingDouble(Employee::getSalary));
// stats.getSum(), getAverage(), getMax(), getMin(), getCount()

// First match
Optional<Employee> first = employees.stream()
    .filter(e -> e.getSalary() > 65000)
    .findFirst();

// Any/all/none
boolean anyHigh = employees.stream().anyMatch(e -> e.getSalary() > 80000);
boolean allAdults = employees.stream().allMatch(e -> e.getAge() >= 18);
boolean noInterns = employees.stream().noneMatch(e -> e.getDept().equals("Intern"));
```

## Q10. Stream performance — kab mat use karo
**Answer:**

```java
// ❌ Simple loop ke liye stream — overkill
list.stream().forEach(System.out::println);
// ✅ Bas ye karo
list.forEach(System.out::println);

// ❌ Counter ke liye stream
AtomicInteger count = new AtomicInteger();
list.stream().forEach(x -> count.incrementAndGet());
// ✅
int count = list.size();

// ❌ Parallel stream chhote data pe
smallList.parallelStream()...   // overhead > benefit
```

👉 **Rule:** 10,000+ elements + expensive operations → parallel socho. Warna sequential stream ya simple loop.

---

> 💡 **Interview tip:** `groupingBy` ka downstream wala form (`groupingBy(dept, counting())`) jaanna hi kaafi hai senior-level dikhne ke liye. Aur ek line bolna: *"Streams lazy hain — intermediate operations tab tak nahi chalte jab tak terminal operation na aaye"* — ye dikhata hai tum internals samajhte ho, sirf syntax nahi.
