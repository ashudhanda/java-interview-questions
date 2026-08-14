# 77 — Spring Boot Interview Questions 🍃

Java backend = Spring Boot. Ye topic core annotations, DI, aur layer structure cover karta hai — fresher interviews ka sabse common set.

## Q1. Spring vs Spring Boot — farak kya hai?
**Answer:**

| | Spring | Spring Boot |
|---|---|---|
| Configuration | XML/Java config haath se | **Auto-configuration** ✅ |
| Server | Tomcat alag se deploy | **Embedded Tomcat** ✅ |
| Dependencies | Har ek manually | **Starter POMs** ✅ |
| Run kaise | WAR deploy | `java -jar app.jar` ✅ |

👉 **Ek line:** Spring Boot = Spring + sensible defaults + embedded server. Boilerplate gayab.

## Q2. Dependency Injection kya hai?
**Answer:** Objects tum **mat banao** — Spring banake dega.

```java
// ❌ Tight coupling — khud bana rahe
class OrderService {
    private OrderRepository repo = new OrderRepository();
}

// ✅ DI — Spring inject karega
@Service
class OrderService {
    private final OrderRepository repo;

    OrderService(OrderRepository repo) {   // constructor injection
        this.repo = repo;
    }
}
```

**Teen tareeke:**
1. **Constructor injection** ✅ (recommended — testing aasan, immutable)
2. Setter injection
3. `@Autowired` field injection ❌ (avoid — testing mushkil)

👉 **Testing me fayda:** mock repository pass kar do constructor se. Field injection me ye mushkil hai.

## Q3. Core annotations — roz wale
**Answer:**

| Annotation | Kaam |
|---|---|
| `@SpringBootApplication` | Main class pe — 3 annotations ka combo |
| `@Component` | General bean banao |
| `@Service` | Business logic wali class |
| `@Repository` | Database layer |
| `@RestController` | REST API endpoint |
| `@Autowired` | Dependency inject karo |
| `@Value` | properties se value lao |
| `@Bean` | Method-level bean definition |

👉 `@Component`, `@Service`, `@Repository` teeno **bean banate hain** — farak sirf **semantic** hai (aur `@Repository` pe exception translation milti hai).

## Q4. `@RestController` vs `@Controller`
**Answer:**

```java
@Controller                        // purana — view (HTML) return karta hai
class PageController {
    @GetMapping("/home")
    String home() { return "home.html"; }    // page ka naam
}

@RestController                    // = @Controller + @ResponseBody
class ApiController {
    @GetMapping("/api/user")
    User getUser() { return new User("Ashu"); }   // JSON return ✅
}
```

👉 `@RestController` = data (JSON) wapas karta hai. `@Controller` = HTML page. REST APIs me hamesha `@RestController`.

## Q5. Request mapping annotations
**Answer:**

```java
@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping                    // GET /users
    List<User> getAll() { ... }

    @GetMapping("/{id}")          // GET /users/42
    User getById(@PathVariable Long id) { ... }

    @GetMapping("/search")        // GET /users/search?name=ashu
    List<User> search(@RequestParam String name) { ... }

    @PostMapping                   // POST /users
    User create(@RequestBody User user) { ... }

    @PutMapping("/{id}")          // PUT /users/42
    User update(@PathVariable Long id, @RequestBody User user) { ... }

    @DeleteMapping("/{id}")       // DELETE /users/42
    void delete(@PathVariable Long id) { ... }
}
```

| Annotation | Data kahan se |
|---|---|
| `@PathVariable` | URL path se — `/users/42` |
| `@RequestParam` | Query string se — `?name=ashu` |
| `@RequestBody` | JSON body se |

## Q6. Bean scopes
**Answer:**

| Scope | Matlab |
|---|---|
| **singleton** (default) | Poori app me **ek hi** instance |
| **prototype** | Har baar **naya** instance |
| request | Har HTTP request pe naya (web only) |
| session | Har user session pe naya (web only) |

```java
@Component
@Scope("prototype")    // har injection pe naya object
class ShoppingCart { }
```

⚠️ **Singleton beans thread-safe nahi hote by default** — mutable state mat rakho unme. Stateless rakho.

## Q7. Layered architecture — standard structure
**Answer:**

```
Controller  →  Service  →  Repository  →  Database
  (REST)      (logic)      (DB calls)
```

```java
@RestController
@RequestMapping("/orders")
class OrderController {
    private final OrderService service;          // sirf service se baat
}

@Service
class OrderService {
    private final OrderRepository repo;          // business logic yahan
    // validation, calculation, rules
}

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    // sirf database operations
}
```

👉 **Rule:** Controller me business logic **nahi**, Service me HTTP ka gyaan **nahi**, Repository me business rules **nahi**. Har layer ka ek kaam.

## Q8. `application.properties` / `application.yml`
**Answer:**

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.jpa.hibernate.ddl-auto=update

# Custom value
app.name=MyApp
```

```java
@Value("${app.name}")
private String appName;
```

⚠️ **Passwords kabhi commit mat karo** — environment variables use karo: `spring.datasource.password=${DB_PASSWORD}`

## Q9. `@Transactional` kya karta hai?
**Answer:**

```java
@Transactional
void transferMoney(Long from, Long to, double amount) {
    debit(from, amount);
    credit(to, amount);
    // Beech me exception → DONO rollback. Koi half state nahi.
}
```

👉 **ACID ka A (Atomicity)** — ya to poora hoga, ya kuch nahi.

⚠️ **Common gotcha:** same class ke method se call karne pe `@Transactional` **kaam nahi karta** (proxy bypass ho jaata hai). Doosri class se call karo.

## Q10. Actuator aur DevTools
**Answer:**

**Actuator** — app ki health/metrics:
```
GET /actuator/health    → {"status":"UP"}
GET /actuator/metrics   → memory, CPU details
```

**DevTools** — code badlo, app khud restart.

**Validation:**
```java
@PostMapping
User create(@Valid @RequestBody User user) { ... }

class User {
    @NotBlank String name;
    @Email String email;
    @Min(18) int age;
}
```

## Q11. Exception handling — global
**Answer:**

```java
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404)
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(400)
            .body(new ErrorResponse("Validation failed"));
    }
}
```

👉 Ek hi jagah saare exceptions handle — har controller me try-catch nahi likhna padta.

## Q12. Common interview questions
**Answer:**

**"Spring Boot app kaise start hoti hai?"**
`@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`. Main class ka package scan hota hai, beans bante hain, embedded Tomcat start hota hai.

**"IoC container kya hai?"**
Spring ka dimaag — beans banata hai, inject karta hai, lifecycle manage karta hai. `ApplicationContext` hi IoC container hai.

**"Bean lifecycle?"**
Instantiate → DI → `@PostConstruct` → ready → `@PreDestroy` → destroy.

---

> 💡 **Interview tip:** Spring poochhein to **layered architecture** zaroor bolo — Controller/Service/Repository ka separation. Aur constructor injection prefer karo — bolo *"field injection avoid karta hoon kyunki constructor se testing me mock inject karna aasan hai aur fields final rehte hain."* Ye ek line dikhati hai tumne real code likha hai, tutorial copy nahi kiya.
