# 18 — Streams API Coding 🌊

Interview-style problems solved with Java 8 Streams. Har problem ka loop-wala solution bhi socho, phir stream compare karo.

## Q1. Filter even numbers from a list.
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
List<Integer> evens = nums.stream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList());
// [2, 4, 6]
```

## Q2. Convert list of strings to uppercase.
```java
List<String> upper = names.stream()
        .map(String::toUpperCase)
        .collect(Collectors.toList());
```

## Q3. Find the maximum element.
```java
int max = nums.stream()
        .max(Integer::compareTo)
        .orElseThrow(); // empty list pe Optional — orElse handle karo
```

## Q4. Count frequency of each word.
```java
List<String> words = List.of("a", "b", "a", "c", "b", "a");
Map<String, Long> freq = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
// {a=3, b=2, c=1}
```
⭐ **Grouping wale questions interviews me bahut aate hain!**

## Q5. Sort employees by salary (descending).
```java
List<Employee> sorted = employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .collect(Collectors.toList());
```

## Q6. Remove duplicates while keeping order.
```java
List<Integer> unique = nums.stream()
        .distinct()
        .collect(Collectors.toList());
```
`distinct()` first occurrence rakhta hai — LinkedHashSet wala logic internally.

## Q7. Flatten a list of lists.
```java
List<List<String>> nested = List.of(List.of("a", "b"), List.of("c"));
List<String> flat = nested.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
// [a, b, c]
```
`flatMap` = map + flatten. Ye samajh gaya to streams 80% clear.

## Q8. Sum of squares of odd numbers (chain practice).
```java
int sum = nums.stream()
        .filter(n -> n % 2 != 0)
        .map(n -> n * n)
        .reduce(0, Integer::sum);
```

## Q9. Partition into even and odd.
```java
Map<Boolean, List<Integer>> parts = nums.stream()
        .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// {false=[1,3,5], true=[2,4,6]}
```
`partitioningBy` vs `groupingBy`: partition = sirf 2 buckets (true/false).

## Q10. Find first element starting with "A".
```java
Optional<String> first = names.stream()
        .filter(n -> n.startsWith("A"))
        .findFirst();
```
**Trick:** `findFirst()` vs `findAny()` — parallel stream me `findAny()` faster ho sakta hai; sequential me same result.