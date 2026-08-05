# 22 — Date & Time API (java.time) 📅

Purani `Date`/`Calendar` ki problems aur nayi `java.time` API (Java 8+) — interviews me ab yehi poochha jata hai.

## Q1. Why was the old `java.util.Date` problematic?
**Answer:**
- **Mutable** — kabhi bhi badal sakta hai (bugs ka adda)
- Months **0-based** (January = 0) 😵, years 1900 se count
- `Date` ka naam Date hai par time bhi rakhta hai — confusing
- Thread-safe nahi; `SimpleDateFormat` bhi thread-safe nahi tha

## Q2. Which classes does the new API provide?
**Answer:**

| Class | Kya store karta hai | Example |
|-------|--------------------|---------|
| `LocalDate` | Sirf date | 2026-08-05 |
| `LocalTime` | Sirf time | 18:30 |
| `LocalDateTime` | Date + time | 2026-08-05T18:30 |
| `ZonedDateTime` | Date + time + zone | with Asia/Kolkata |
| `Period` | Date-based gap (years/months/days) | P2Y3M |
| `Duration` | Time-based gap (hours/min/sec) | PT5H30M |

## Q3. Common operations with LocalDate.
```java
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(2005, Month.JANUARY, 15);

LocalDate next = today.plusDays(10);
LocalDate prev = today.minusMonths(1);

boolean before = birthday.isBefore(today);
int day = today.getDayOfMonth();
```
Sab methods **new object return** karte hain — `LocalDate` immutable hai. `today.plusDays(10)` likhna akela kaafi nahi; assign karna mat bhoolo! ⭐

## Q4. How do you format and parse dates?
**Answer:** `DateTimeFormatter` — thread-safe hai (unlike SimpleDateFormat).

```java
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

String text = LocalDate.now().format(fmt);        // format
LocalDate date = LocalDate.parse("05-08-2026", fmt); // parse
```
Pattern letters: `dd` day, `MM` month, `yyyy` year, `HH` 24-hour, `mm` minutes.

## Q5. Period vs Duration?
**Answer:**
```java
Period p = Period.between(dob, LocalDate.now()); // years, months, days
Duration d = Duration.between(startTime, endTime); // hours, minutes, seconds, nanos
```
**Trick:** `LocalDate` pe `Duration` use karoge to `UnsupportedTemporalTypeException` — Duration ko time chahiye!

## Q6. How do time zones work?
**Answer:**
```java
ZonedDateTime india = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
ZonedDateTime london = india.withZoneSameInstant(ZoneId.of("Europe/London"));
```
`withZoneSameInstant` = same moment, different local representation. Interviews me timezone bug stories bahut poochhi jati hain — `Instant` store karo (UTC), display ke time zone lagao.

## Q7. How to get a timestamp for DB storage?
**Answer:** `Instant.now()` — UTC machine timestamp (nanosecond precision). DB me hamesha `Instant`/UTC store karo, kabhi local time nahi.