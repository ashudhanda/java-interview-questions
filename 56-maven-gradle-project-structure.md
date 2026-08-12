# 56 — Maven, Gradle & Project Structure 📦

College me sab single file me code likhte hain. Company me project structure, build tool aur dependency management — ye teen cheezein roz kaam aati hain. Interview me bhi poochi jaati hain.

## Q1. Build tool ki zaroorat kya hai?
**Answer:** Manually kaam karo to ye sab karna padega:

- Har library ki JAR **khud download** karo
- Us library ki **dependencies** bhi download karo (aur unki bhi!)
- Classpath **haath se** set karo
- Compile, test, package — alag-alag commands
- Version conflict khud solve karo

👉 Build tool ye sab **ek command** me karta hai. `mvn package` — bas.

**Do main tools:** **Maven** (XML based) aur **Gradle** (Groovy/Kotlin based).

## Q2. Standard Maven project structure
**Answer:** Ye layout **convention** hai — har Java project me yahi milega:

```
my-project/
├── pom.xml                    ← config file
├── src/
│   ├── main/
│   │   ├── java/              ← asli code
│   │   │   └── com/app/Main.java
│   │   └── resources/         ← config, properties, static files
│   │       └── application.properties
│   └── test/
│       ├── java/              ← test code
│       └── resources/         ← test config
└── target/                    ← build output (git me mat daalo!)
```

👉 **"Convention over configuration"** — Maven ko batane ki zaroorat nahi ki code kahan hai. Standard jagah pe rakho, khud dhoondh lega.

⚠️ `target/` folder **`.gitignore` me daalo** — wo generated hai, commit nahi karna.

## Q3. `pom.xml` me kya hota hai?
**Answer:**

```xml
<project>
    <modelVersion>4.0.0</modelVersion>

    <!-- Project ki pehchan — GAV coordinates -->
    <groupId>com.ashu</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

👉 **GAV** = **G**roupId + **A**rtifactId + **V**ersion. Ye teen cheezein milke koi bhi library **uniquely** identify karti hain.

## Q4. Dependency scopes kya hote hain?
**Answer:** Batata hai library **kab** available hogi:

| Scope | Compile time | Runtime | Final JAR me | Example |
|---|---|---|---|---|
| `compile` (default) | ✅ | ✅ | ✅ | normal libraries |
| `provided` | ✅ | ❌ | ❌ | Servlet API (server deta hai) |
| `runtime` | ❌ | ✅ | ✅ | JDBC driver |
| `test` | ✅ (sirf test) | ✅ (sirf test) | ❌ | JUnit, Mockito |

⚠️ **JUnit pe `<scope>test</scope>` lagana mat bhoolna** — warna test library production JAR me chali jaayegi. Ye common galti hai.

## Q5. Maven lifecycle phases
**Answer:** Order me chalte hain — baad wala phase pehle waale sab chala deta hai:

```
validate → compile → test → package → verify → install → deploy
```

| Command | Kya karta hai |
|---|---|
| `mvn compile` | Code compile |
| `mvn test` | Tests chalao |
| `mvn package` | JAR/WAR banao |
| `mvn install` | Local repo (`~/.m2`) me daalo |
| `mvn clean` | `target/` folder saaf karo |
| `mvn clean install` | Saaf karke poora build ⭐ |

👉 **`mvn package` khud hi compile aur test chala deta hai** — alag se karne ki zaroorat nahi. Ye lifecycle ka concept samajhna zaroori hai.

⚠️ Tests fail hon to build **ruk jaata** hai. Skip karna ho: `mvn package -DskipTests` (par ye aadat mat banao).

## Q6. Transitive dependency kya hai?
**Answer:** Tumhari library ki **apni** dependencies bhi automatically aa jaati hain.

```
Tumne A maanga
  └─ A ko B chahiye      ← automatic aa gaya
       └─ B ko C chahiye  ← ye bhi aa gaya
```

**Dekhne ke liye:**
```bash
mvn dependency:tree
```

⚠️ **Version conflict** — do libraries alag-alag version maangein to? Maven **"nearest wins"** rule use karta hai — jo dependency tree me **kam gehri** hai wo jeet jaati hai.

Manually force karna ho:
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.x</groupId>
            <artifactId>lib</artifactId>
            <version>2.0</version>       <!-- ye version hi use hoga -->
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Q7. Maven vs Gradle
**Answer:**

| | Maven | Gradle |
|---|---|---|
| Config file | `pom.xml` (XML) | `build.gradle` (Groovy/Kotlin) |
| Likhne me | Verbose ❌ | **Chhota** ✅ |
| Speed | Dheema | **Tez** ✅ (incremental + cache) |
| Seekhna | **Aasan** ✅ | Thoda mushkil |
| Flexibility | Kam (convention strict) | **Zyada** ✅ |
| Kahan common | Enterprise, Spring | Android ⭐, naye projects |

**Same dependency — dono me:**
```xml
<!-- Maven -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
</dependency>
```
```groovy
// Gradle — ek line
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
```

👉 **Android me Gradle hi hai** — choice nahi hai. Backend/Spring me Maven zyada common hai.

## Q8. JAR vs WAR vs FAT JAR
**Answer:**

| Type | Kya hai | Kahan chalta hai |
|---|---|---|
| **JAR** | Java ARchive — compiled classes | `java -jar app.jar` |
| **WAR** | Web ARchive — web app | Tomcat/server ke andar |
| **Fat JAR** | JAR + **saari dependencies** andar | Kahin bhi, akela ✅ |

👉 **Spring Boot fat JAR banata hai** — isliye alag se Tomcat install karne ki zaroorat nahi, sab andar hi hota hai. Isi wajah se Docker/cloud deployment aasan ho gaya.

## Q9. Package naming convention kya hai?
**Answer:** **Ulta domain naam** use karo:

```java
package com.ashudhanda.myapp.service;
//      ↑ domain ulta      ↑ project  ↑ layer
```

**Layers ke hisaab se organize karo:**
```
com.ashudhanda.myapp/
├── controller/     ← request handle karta hai
├── service/        ← business logic
├── repository/     ← database access
├── model/          ← data classes
├── config/         ← configuration
└── exception/      ← custom exceptions
```

⚠️ **Sab kuch default package me mat daalo** (bina `package` statement ke). Wo classes kisi aur package se import hi nahi ho sakti — ye Java ki restriction hai.

## Q10. `.gitignore` me Java project ke liye kya daalein?
**Answer:**

```gitignore
# Build output
target/
build/
out/
*.class
*.jar

# IDE
.idea/
*.iml
.vscode/
.settings/
.classpath
.project

# Logs & OS
*.log
.DS_Store

# Sensitive — SABSE ZAROORI
*.env
application-local.properties
```

⚠️ **Kabhi commit mat karo:** API keys, passwords, `.env` files. Ek baar Git history me chala gaya to file delete karne se bhi history me rehta hai — poori history rewrite karni padti hai.

👉 Isliye `application.properties` me placeholder rakho aur asli values **environment variables** se lo.

## Q11. Maven ke kaam ke commands
**Answer:**

```bash
mvn clean install          # poora build ⭐
mvn clean package -DskipTests   # tests skip karke JAR
mvn dependency:tree        # dependencies ka tree dekho
mvn dependency:analyze     # unused/missing dependencies
mvn versions:display-dependency-updates   # naye versions available?
mvn test -Dtest=UserServiceTest           # sirf ek test class
```

👉 **`~/.m2/repository`** — yahan saari downloaded libraries cache hoti hain. Kabhi corrupt ho jaaye to us folder ko delete kar do, Maven dobara download kar lega.

## Q12. Semantic versioning kya hai?
**Answer:** `MAJOR.MINOR.PATCH` — jaise `2.5.1`

| Part | Kab badhta hai |
|---|---|
| **MAJOR** | Breaking change — purana code toot jaayega |
| **MINOR** | Naya feature, par purana code chalta rahega |
| **PATCH** | Sirf bug fix |

👉 Isliye `1.5.2` → `1.6.0` upgrade **safe** hai, par `1.x` → `2.0` me **code badalna** pad sakta hai.

**`SNAPSHOT`** = under development version (`1.0-SNAPSHOT`). Maven ise baar-baar re-download karta hai kyunki wo badalti rehti hai.

---

> 💡 **Interview tip:** agar tumne college project me build tool use nahi kiya, to ab kar lo — ek chhota Maven project bana ke JUnit add karo aur `mvn clean install` chalao. *"Maven use kiya hai, `pom.xml` me dependencies manage ki hain, aur `mvn dependency:tree` se version conflict debug kiya hai"* — ye ek line fresher ko baaki bheed se alag kar deti hai.
