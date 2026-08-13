# 67 — Mockito & Test Doubles 🎭

Topic 15 me JUnit tha. Ab **Mockito** — jab class ka test karna ho par uske dependencies (database, API) ko **fake** karna ho.

## Q1. Mock kyun chahiye?
**Answer:** Problem ye hai:

```java
class OrderService {
    private PaymentGateway payment;      // asli payment — paise katenge!
    private OrderRepository repo;        // asli database chahiye!
    private EmailService email;          // asli email chala jaayega!

    void placeOrder(Order order) {
        payment.charge(order.getTotal());
        repo.save(order);
        email.send(order.getEmail(), "Confirmed!");
    }
}
```

Test me **asli** payment, database, email nahi chahiye. Chahiye **fake versions** jo humari marzi se behave karein. Wo hi **mock** hai.

👉 **Unit test ka rule:** sirf **ek class** test karo, baaki sab fake.

## Q2. Test Doubles ke types
**Answer:** 5 types hote hain — interview me farak poochte hain:

| Type | Kya hai |
|---|---|
| **Dummy** | Bas parameter bharne ke liye, use nahi hota |
| **Fake** | Kaam karta hai par simplified (in-memory DB) |
| **Stub** | Fixed answers deta hai ("hamesha true return karo") |
| **Mock** | Stub + **yaad rakhta hai** kya calls aaye (verify kar sakte ho) |
| **Spy** | Asli object, par kuch methods mock kar sakte ho |

👉 Roz ke kaam me **mock** aur **stub** hi zyada use hote hain. Mockito dono bana deta hai.

## Q3. Basic Mockito test
**Answer:**

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    PaymentGateway payment;          // fake payment

    @Mock
    OrderRepository repo;            // fake database

    @InjectMocks
    OrderService orderService;       // asli class, fake dependencies inject

    @Test
    void placeOrder_chargesPayment() {
        // Arrange
        Order order = new Order("a@b.com", 500);

        // Act
        orderService.placeOrder(order);

        // Assert — verify call hua ya nahi
        verify(payment).charge(500);
        verify(repo).save(order);
    }
}
```

**Annotations:**
- `@Mock` → fake object banao
- `@InjectMocks` → asli class banao, mocks ko andar daal do

## Q4. `when().thenReturn()` — stubbing
**Answer:** Fake ka behavior control karo:

```java
@Test
void getUser_notFound() {
    // Jab ye call aaye, to ye return karo
    when(repo.findById("99")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> userService.getUser("99"));
}
```

**Common patterns:**
```java
when(repo.findById(anyString())).thenReturn(Optional.of(user));
when(payment.charge(anyDouble())).thenThrow(new PaymentFailedException());
when(service.getCount()).thenReturn(1, 2, 3);    // 1st call: 1, 2nd: 2, 3rd: 3
when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));  // jo aaya wahi wapas
```

👉 **`anyString()`, `any()`, `anyInt()`** = argument matchers — "koi bhi value aaye to ye karo".

## Q5. `verify()` — calls check karo
**Answer:** Return value nahi, **behavior** test karo:

```java
verify(payment).charge(500);                    // exactly 1 baar
verify(payment, times(2)).charge(anyDouble());  // 2 baar
verify(payment, never()).refund(anyDouble());   // kabhi nahi
verifyNoInteractions(email);                    // email gayi hi nahi
verifyNoMoreInteractions(payment);              // aur koi call nahi hua

// Order me hue?
InOrder order = inOrder(payment, email);
order.verify(payment).charge(500);              // pehle payment
order.verify(email).send(any(), any());         // phir email
```

👉 **Test ka logic:** "agar payment fail ho jaaye to email **nahi** jaani chahiye" — `verify(email, never())` se test karo.

## Q6. `@Mock` vs `@Spy`
**Answer:**

```java
@Mock
List<String> mockList;         // poora fake — add() kuch nahi karega

@Spy
List<String> spyList = new ArrayList<>();   // ASLI list — sab kaam karega
```

```java
mockList.add("hello");
mockList.size();               // 0! (fake hai, kuch store nahi hua)

spyList.add("hello");
spyList.size();                // 1 (asli hai)

// Spy me kuch methods fake kar sakte ho
doReturn(100).when(spyList).size();
spyList.size();                // 100 (sirf size fake kiya)
```

⚠️ **Spy me `when()` mat use karo** — asli method chal jaata hai. `doReturn().when()` use karo. Ye classic gotcha hai.

## Q7. `assertThrows()` — exception test
**Answer:**

```java
@Test
void divide_byZero_throws() {
    ArithmeticException ex = assertThrows(
        ArithmeticException.class,
        () -> calculator.divide(10, 0)
    );
    assertEquals("/ by zero", ex.getMessage());
}
```

👉 Purana tareeka `@Test(expected = ...)` ab mat use karo — `assertThrows` zyada control deta hai (message bhi check kar sakte ho).

## Q8. Argument Captor
**Answer:** Method ko **kya pass hua** wo pakad ke check karo:

```java
ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
verify(repo).save(captor.capture());

Order saved = captor.getValue();
assertEquals("a@b.com", saved.getEmail());
assertEquals(500, saved.getTotal());
```

👉 Jab method **khud object banata** hai (tum pass nahi karte), tab capture zaroori hai.

## Q9. Static methods mock kaise karein?
**Answer:** Mockito 3.4+ se possible:

```java
try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
    mocked.when(LocalDateTime::now).thenReturn(fixedTime);

    // ab LocalDateTime.now() hamesha fixedTime dega
    service.process();
}
```

⚠️ **Static mocking zaroorat = code smell.** Agar static methods baar-baar mock karne pad rahe hain to design me problem hai — dependency injection use karo.

## Q10. Achhe unit test ki properties
**Answer:**

| Property | Matlab |
|---|---|
| **Fast** | Milliseconds me chale — DB/API nahi |
| **Isolated** | Doosre test pe depend nahi |
| **Repeatable** | Har baar same result — `new Date()` mat use karo directly |
| **Self-validating** | Pass/fail khud bata de |
| **Timely** | Code ke saath likho, baad me nahi |

⚠️ **Test me kya mat karo:**
- `Thread.sleep()` — flaky tests bante hain
- Random data — kabhi pass kabhi fail
- Ek test me 5 cheezein — fail hua to pata nahi kya toota
- Private methods directly test — public ke through test karo

## Q11. TDD kya hai?
**Answer:** Test-Driven Development — **pehle test, phir code**:

```
1. RED    → failing test likho (feature abhi hai hi nahi)
2. GREEN  → minimum code likho jo test pass kare
3. REFACTOR → code saaf karo, tests green rakhte hue
```

👉 **Faayda:** design pehle sochna padta hai, coverage automatically 100% ke kareeb, regression se darr nahi lagta.

---

> 💡 **Interview tip:** testing question me ye line bolna: *"Main unit tests me sirf ek class test karta hoon, baaki dependencies Mockito se mock karta hoon — taaki test fast rahe aur real database ya API pe depend na kare. Aur `verify` se check karta hoon ki sahi sequence me calls hue."* Freshers me se bahut kam log `verify` jaante hain — ye use karna turant alag karega.
