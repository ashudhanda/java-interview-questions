# 15 — Unit Testing with JUnit 🧪

## Q1. What is unit testing?
**Answer:** Testing the smallest piece of code (a method/class) in isolation to verify it behaves correctly. Catches bugs early and makes refactoring safe.

## Q2. What does a basic JUnit 5 test look like?
**Answer:**
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void addsTwoNumbers() {
        assertEquals(5, new Calculator().add(2, 3));
    }
}
```

## Q3. Which annotations are most used in JUnit 5?
**Answer:**
- `@Test` — marks a test method
- `@BeforeEach` / `@AfterEach` — run before/after every test
- `@BeforeAll` / `@AfterAll` — once per class (static)
- `@Disabled` — skip a test
- `@ParameterizedTest` — same test, multiple inputs

## Q4. Common assertions?
**Answer:** `assertEquals`, `assertTrue`, `assertFalse`, `assertNull`, `assertNotNull`, `assertThrows`:
```java
assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
```

## Q5. What makes a good unit test?
**Answer:** F.I.R.S.T — **F**ast, **I**ndependent (order doesn't matter), **R**epeatable, **S**elf-validating (pass/fail, no manual check), **T**imely.

## Q6. What is mocking and why is it needed?
**Answer:** Replacing real dependencies (DB, network) with fake controllable objects so you test *only* your unit. Popular library: Mockito.
```java
UserRepo repo = mock(UserRepo.class);
when(repo.findName(1)).thenReturn("Ashu");
```

## Q7. Test naming convention?
**Answer:** Describe behavior, not implementation — `returnsZeroWhenListIsEmpty()` is better than `test1()`.
