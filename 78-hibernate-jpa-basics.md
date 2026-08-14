# 78 — Hibernate & JPA Basics 🗃️

Java + database = JDBC (topic 14) se zyada aaj kal **JPA/Hibernate** use hota hai. SQL likhna kam, objects se kaam zyada.

## Q1. JPA vs Hibernate — farak kya hai?
**Answer:**

- **JPA (Jakarta Persistence API)** = **specification/interface** — sirf rules batata hai
- **Hibernate** = **implementation** — asli kaam karta hai

👉 Jaise `List` interface hai aur `ArrayList` implementation. Spring Data JPA default me Hibernate use karta hai.

```java
// JDBC me itna code:
Connection con = DriverManager.getConnection(...);
PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id=?");
ps.setLong(1, 42);
ResultSet rs = ps.executeQuery();
// ... rs se fields nikalo, object banao ...

// JPA me bas:
User user = userRepository.findById(42).orElseThrow();
```

## Q2. Entity kaise banayein?
**Answer:** Java class = database table.

```java
@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

| Annotation | Kaam |
|---|---|
| `@Entity` | Ye class table hai |
| `@Table` | Table ka naam (default = class name) |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-increment |
| `@Column` | Column customize karo |

## Q3. Repository — SQL likhne ki zaroorat hi nahi
**Answer:**

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Method ka NAAM hi query ban jaata hai!

    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String keyword);
    List<User> findByAgeGreaterThan(int age);
    List<User> findByDeptAndActiveTrue(String dept);
    long countByDept(String dept);
    void deleteByEmail(String email);
}
```

**Built-in methods (free me milte hain):**
```java
repo.findAll();
repo.findById(42L);
repo.save(user);           // insert + update dono
repo.deleteById(42L);
repo.count();
repo.existsById(42L);
```

👉 **Derived query methods:** `findBy` + field + condition — Spring khud SQL banata hai.

## Q4. Custom queries — `@Query`
**Answer:** Complex queries ke liye:

```java
@Query("SELECT u FROM User u WHERE u.dept = :dept AND u.salary > :min")
List<User> findRichByDept(@Param("dept") String dept, @Param("min") double min);

// Native SQL bhi chala sakte ho
@Query(value = "SELECT * FROM users WHERE created_at > NOW() - INTERVAL 7 DAY",
       nativeQuery = true)
List<User> findRecentUsers();
```

👉 **JPQL** objects pe chalti hai (`FROM User`), **native** tables pe (`FROM users`). Pehle JPQL try karo — database-independent hoti hai.

## Q5. Relationships — mapping
**Answer:**

```java
@Entity
class Department {
    @Id Long id;

    @OneToMany(mappedBy = "dept")
    private List<Employee> employees;
}

@Entity
class Employee {
    @Id Long id;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department dept;

    @ManyToMany
    @JoinTable(name = "emp_project",
        joinColumns = @JoinColumn(name = "emp_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;
}
```

| Mapping | Example |
|---|---|
| `@OneToOne` | User ↔ Profile |
| `@OneToMany` / `@ManyToOne` | Department → Employees |
| `@ManyToMany` | Students ↔ Courses |

## Q6. Lazy vs Eager loading — THE question
**Answer:**

```java
@ManyToOne(fetch = FetchType.LAZY)     // department tabhi lao jab access karo
private Department dept;
```

| | LAZY | EAGER |
|---|---|---|
| Kab load | Access karne pe | Parent ke saath hi |
| Queries | 1 + N (problem!) | 1 badi JOIN |
| Default | `@OneToMany` pe | `@ManyToOne` pe |
| Best practice | ✅ LAZY rakho | ❌ EAGER se N+1 |

⚠️ **N+1 problem:** 100 employees lao, phir har ek ka dept access karo → **1 + 100 = 101 queries!** Fix: `JOIN FETCH` use karo.

```java
@Query("SELECT e FROM Employee e JOIN FETCH e.dept")
List<Employee> findAllWithDept();     // 1 query, sab aa gaya
```

## Q7. `save()` — insert ya update?
**Answer:** Spring decide karta hai:

```java
User u = new User();
u.setName("Ashu");
repo.save(u);          // id null hai → INSERT

User existing = repo.findById(42L).get();
existing.setName("Ashu Updated");
repo.save(existing);   // id hai → UPDATE
```

👉 ID hai → update, nahi → insert. Simple.

## Q8. Entity lifecycle states
**Answer:**

```
new User()         → TRANSIENT (JPA ko nahi pata)
repo.save(u)       → PERSISTENT (managed — changes auto-track)
session close      → DETACHED (ab track nahi hota)
repo.delete(u)     → REMOVED
```

👉 **Persistent state ka magic:** managed entity ke fields badlo — `@Transactional` method khatam hone pe **khud UPDATE** ho jaata hai, `save()` bulaane ki zaroorat nahi!

```java
@Transactional
void updateName(Long id, String name) {
    User u = repo.findById(id).orElseThrow();
    u.setName(name);           // bas! auto-update hoga
}
```

## Q9. Pagination & Sorting
**Answer:**

```java
// Page 2, 10 items per page, salary se sorted
Pageable pageable = PageRequest.of(1, 10, Sort.by("salary").descending());
Page<User> page = repo.findAll(pageable);

page.getContent();          // List<User>
page.getTotalPages();       // kitne pages
page.getTotalElements();    // total records
page.hasNext();             // agla page hai?
```

👉 API me `?page=1&size=10&sort=salary,desc` — Spring automatically Pageable me convert kar deta hai.

## Q10. `ddl-auto` — production me kya rakhein?
**Answer:**

```properties
spring.jpa.hibernate.ddl-auto=update
```

| Value | Kaam | Kab |
|---|---|---|
| `create` | Table drop + recreate | kabhi nahi ⚠️ |
| `update` | Naye columns add | **development** |
| `validate` | Sirf check karo | **production** ✅ |
| `none` | Kuch mat karo | production (Flyway ke saath) |

⚠️ **Production me `update` mat rakho** — schema migrations ke liye **Flyway/Liquibase** use karo (versioned SQL files).

## Q11. Common gotchas
**Answer:**

```java
// ❌ equals/hashCode me lazy field use kiya
// → LazyInitializationException ya poora graph load

// ❌ toString() me relationship daali
// → infinite recursion (Emp → Dept → Emp → ...)

// ❌ Open Session In View on rakha (default!)
spring.jpa.open-in-view=false    // production me OFF karo

// ❌ List<User> findAll() pe 10 lakh rows
// → Pageable use karo hamesha
```

---

> 💡 **Interview tip:** JPA poochhein to teen cheezein bol do — (1) *"derived query methods se simple CRUD free me milta hai"*, (2) *"N+1 problem se bachne ke liye JOIN FETCH ya EntityGraph use karta hoon"*, (3) *"production me ddl-auto=validate aur migrations Flyway se."* N+1 jaanna hi junior/senior ka farak hai.
