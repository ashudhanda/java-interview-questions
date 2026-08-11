# 41 — NIO.2 Modern File Handling 📁

Topic 12 me purana `java.io` hai. Ye topic modern `java.nio.file` pe hai — jo aaj real projects me use hota hai. Interviewer ko `Files` aur `Path` bolna `FileInputStream` se kaafi better lagta hai.

## Q1. `File` (purana) vs `Path` (naya) — farak kya hai?
**Answer:** `java.io.File` Java 1.0 ka hai aur uske problems the. Java 7 me `java.nio.file` (NIO.2) aaya.

| | `java.io.File` | `java.nio.file.Path` + `Files` |
|---|---|---|
| Error info | Sirf `false` return karta hai | **Detailed exception** batata hai kyun fail hua |
| Symbolic links | Support nahi | ✅ Support |
| Metadata (permissions, owner) | Bahut limited | ✅ Poora |
| Directory walk | Manual recursion | `Files.walk()` |
| Large directory | Poori list memory me | Stream me — memory efficient |

👉 **Killer example:** purane API me `file.delete()` `false` de deta tha — par kyun? File thi hi nahi? Permission nahi thi? Locked thi? Pata hi nahi chalta. `Files.delete()` exact exception phenkta hai.

## Q2. `Path` kaise banate hain?
**Answer:**

```java
Path p1 = Path.of("data", "users.txt");      // Java 11+ — preferred
Path p2 = Paths.get("data/users.txt");        // purana tareeka, same kaam

p1.getFileName();      // users.txt
p1.getParent();        // data
p1.toAbsolutePath();   // /home/ashu/data/users.txt
p1.resolve("sub");     // data/users.txt/sub — path jodna
p1.normalize();        // "a/../b" → "b"
```

⚠️ Path banane se file **exist nahi ho jaati** — `Path` sirf ek location ka representation hai.

## Q3. File read/write ka sabse simple tareeka?
**Answer:** Ek line me ho jaata hai:

```java
// Poori file padho
String content = Files.readString(path);              // Java 11+
List<String> lines = Files.readAllLines(path);

// Likho
Files.writeString(path, "hello");                     // Java 11+
Files.write(path, List.of("line1", "line2"));

// Append karo (overwrite nahi)
Files.writeString(path, "more", StandardOpenOption.APPEND);
```

⚠️ `readString()` / `readAllLines()` **poori file memory me** load karte hain. 2GB ki file pe `OutOfMemoryError` aayega — uske liye agla question dekho.

## Q4. Badi file kaise padhein bina memory bharray?
**Answer:** `Files.lines()` — ye ek **lazy stream** deta hai, ek time pe ek line memory me.

```java
try (Stream<String> lines = Files.lines(path)) {
    long errorCount = lines.filter(l -> l.contains("ERROR")).count();
}
```

⚠️ **`Files.lines()` ko hamesha try-with-resources me rakho!** Ye file handle khula rakhta hai. Band nahi kiya to resource leak hoga — ye ek accha interview catch hai.

## Q5. File exist karti hai ya nahi — kaise check karein?
**Answer:**

```java
Files.exists(path);        // hai
Files.notExists(path);     // nahi hai
Files.isRegularFile(path);
Files.isDirectory(path);
Files.isReadable(path);
Files.size(path);          // bytes me
```

⚠️ **`exists()` aur `notExists()` dono `false` ho sakte hain!** Agar permission hi nahi hai to JVM decide nahi kar paata — isliye teen states hain, do nahi.

⚠️ **TOCTOU race condition:** `exists()` check karke phir read karna galat pattern hai — beech me file delete ho sakti hai. Behtar: seedha operation karo aur exception handle karo.

## Q6. File copy, move, delete?
**Answer:**

```java
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);

Files.delete(path);              // na mile to NoSuchFileException
Files.deleteIfExists(path);      // na mile to bas false, exception nahi
```

Useful copy options:
- `REPLACE_EXISTING` — target already ho to overwrite
- `COPY_ATTRIBUTES` — timestamps waghera bhi copy
- `ATOMIC_MOVE` — move ya to poora hoga ya bilkul nahi (aadha nahi)

## Q7. Directory ke andar saari files kaise list karein?
**Answer:** Teen tareeke, teeno ka use alag:

```java
// 1. Sirf ek level (flat)
try (Stream<Path> s = Files.list(dir)) {
    s.forEach(System.out::println);
}

// 2. Recursive — saare subfolders bhi
try (Stream<Path> s = Files.walk(dir)) {
    s.filter(Files::isRegularFile)
     .filter(p -> p.toString().endsWith(".java"))
     .forEach(System.out::println);
}

// 3. Pattern se dhoondho
try (Stream<Path> s = Files.find(dir, 5,
        (p, attr) -> attr.isRegularFile() && attr.size() > 1_000_000)) {
    s.forEach(System.out::println);   // 1MB se badi files
}
```

👉 Teeno stream return karte hain — **teeno ko try-with-resources chahiye.**

## Q8. Directory banane ka sahi tareeka?
**Answer:**

```java
Files.createDirectory(path);     // parent na ho to FAIL
Files.createDirectories(path);   // saare parents bhi bana dega ✅
```

👉 `createDirectories()` **idempotent** hai — already ho to exception nahi deta. Isliye zyadatar yahi use hota hai.

## Q9. Temp file kaise banate hain?
**Answer:**

```java
Path temp = Files.createTempFile("upload-", ".tmp");
Path tempDir = Files.createTempDirectory("work-");

temp.toFile().deleteOnExit();   // JVM band hone pe delete
```

Security ke liye ye better hai than khud random naam banana — OS proper permissions set karta hai.

## Q10. File attributes kaise padhein?
**Answer:**

```java
BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

attrs.size();
attrs.creationTime();
attrs.lastModifiedTime();
attrs.isDirectory();
```

👉 **Performance point:** `Files.size()`, `Files.isDirectory()` alag-alag call karoge to har baar disk hit hogi. `readAttributes()` **ek hi call** me sab de deta hai. Hazaaron files pe ye bada farak banata hai.

## Q11. `WatchService` kya hai?
**Answer:** Folder me koi change ho to **notification** milti hai — baar-baar polling karne ki zaroorat nahi.

```java
WatchService watcher = FileSystems.getDefault().newWatchService();
dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                      StandardWatchEventKinds.ENTRY_MODIFY);

WatchKey key = watcher.take();       // change hone tak block
for (WatchEvent<?> event : key.pollEvents()) {
    System.out.println(event.kind() + ": " + event.context());
}
key.reset();   // reset karna zaroori, warna aage events nahi milenge
```

Hot-reload aur file-sync tools isi pe bane hote hain.

---

> 💡 **Interview me ye bolo:** *"Main `java.io.File` ki jagah NIO.2 (`Path` + `Files`) use karta hoon, kyunki purana API fail hone pe sirf `false` deta tha — reason nahi. Aur `Files.lines()` / `Files.walk()` ko hamesha try-with-resources me rakhta hoon warna file handle leak ho jaata hai."*
