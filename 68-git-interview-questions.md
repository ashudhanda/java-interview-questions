# 68 — Git & GitHub Interview Questions 🌿

Git har roz use hota hai, aur interview me bhi poocha jaata hai — specially `merge` vs `rebase` aur "galti ho gayi to undo kaise karein".

## Q1. Git kya hai? GitHub se kya farak?
**Answer:**

- **Git** = version control **tool** — tumhare computer pe, history track karta hai
- **GitHub** = **website** — Git repos ko cloud pe host karti hai (GitLab, Bitbucket bhi hain)

👉 Git bina GitHub ke chal sakta hai (local repo), par GitHub bina Git ke nahi.

## Q2. Daily workflow — basic commands
**Answer:**

```bash
git clone <url>          # repo download karo
git status               # kya change hua hai
git add .                # saare changes stage karo
git commit -m "msg"      # snapshot banao
git push                 # GitHub pe bhejo
git pull                 # latest changes laao
```

👉 **`add` → staging area, `commit` → local history, `push` → remote.** Teen alag steps hain — ye samajhna zaroori hai.

## Q3. `git fetch` vs `git pull`
**Answer:**

| | `git fetch` | `git pull` |
|---|---|---|
| Kya karta hai | Sirf **download** karta hai | Download + **merge** |
| Code badalta hai? | ❌ Nahi | ✅ Haan |
| Safe hai? | ✅ Bilkul | Conflicts aa sakte hain |

👉 `git pull` = `git fetch` + `git merge`. Pehle `fetch` karke dekho kya aaya hai, phir merge karo — zyada control.

## Q4. Branch kaise banayein?
**Answer:**

```bash
git branch feature-login        # banao
git checkout feature-login      # switch karo

# Ya ek command me (modern)
git checkout -b feature-login   # banao + switch
git switch -c feature-login     # sabse naya syntax
```

👉 **Feature branch workflow:** `main` pe kabhi directly kaam mat karo. Naya feature → nayi branch → kaam karo → PR banao → merge.

## Q5. `merge` vs `rebase` — THE question
**Answer:** Sabse zyada poocha jaata hai:

**Merge:**
```
main:     A---B---C-------M
               \         /
feature:        D---E---
```
History me **merge commit** `M` banta hai. Poori history dikhti hai.

**Rebase:**
```
main:     A---B---C
                   \
feature:            D'---E'
```
Tumhare commits **C ke upar** dobara likhe jaate hain. History **ek seedhi line**.

| | Merge | Rebase |
|---|---|---|
| History | Poori, messy ho sakti | **Clean, linear** ✅ |
| Merge commit | Banta hai | Nahi banta |
| Conflicts | Ek baar solve | **Har commit pe** solve karna pad sakta |
| Shared branch pe | ✅ Safe | ❌ **KABHI NAHI** |

⚠️ **Golden rule:** **`git rebase` kabhi shared/public branch pe mat karo** — history rewrite hota hai, dusron ka kaam toot jaayega.

👉 **Common practice:** apni feature branch pe `rebase main` karo (clean history), phir PR merge karo.

## Q6. Merge conflict kaise solve karein?
**Answer:** Jab do log **same file ki same lines** badal dein:

```
<<<<<<< HEAD
String greeting = "Hello";
=======
String greeting = "Hi";
>>>>>>> feature
```

**Steps:**
1. File kholo, `<<<<<<<`, `=======`, `>>>>>>>` markers dekho
2. Dono changes dekho, sahi wala rakho (ya dono combine karo)
3. Markers hatao
4. `git add file.java` → `git commit`

👉 IDE (VS Code / IntelliJ) me conflict editor hota hai — "Accept Current / Accept Incoming / Accept Both" buttons se ho jaata hai.

## Q7. Galti ho gayi — undo kaise karein?
**Answer:** Situation ke hisaab se:

```bash
# File me changes wapas (abhi commit nahi kiya)
git restore file.java

# Staging se hatao (commit nahi, sirf unstage)
git restore --staged file.java

# Last commit ka MESSAGE badlo
git commit --amend -m "naya message"

# Last commit hatao, changes rakho (unstaged)
git reset --soft HEAD~1

# Last commit hatao, changes staged rakho
git reset --mixed HEAD~1

# Last commit + changes dono hatao — DANGEROUS
git reset --hard HEAD~1

# Push ho chuka hai — revert karo (naya commit banake undo)
git revert HEAD
```

⚠️ **`reset --hard` vs `revert` ka farak zaroori hai:**
- `reset --hard` = history **mita deta hai** (shared branch pe kabhi nahi)
- `revert` = **naya commit** banake undo karta hai (safe, shared pe bhi chalega) ✅

## Q8. `.gitignore` kaise kaam karta hai?
**Answer:**

```gitignore
*.log              # saari .log files
target/            # target folder
node_modules/      # poora folder
.env               # specific file
**/temp/**         # kahin bhi temp folder
```

⚠️ **Already committed file ignore nahi hogi** — pehle untrack karo:
```bash
git rm --cached file.env
git commit -m "stop tracking .env"
```

## Q9. `git stash` kya hai?
**Answer:** Aadha-adhoora kaam **side me rakho**, baad me wapas laao.

```bash
git stash                    # changes chhupa do
git stash pop                # wapas laao + stash se hatao
git stash list               # saare stashes dekho
git stash apply stash@{1}    # specific wala laao
```

👉 **Use case:** feature pe kaam kar rahe ho, urgent bug aa gaya — `stash` karo, bug fix karo, `stash pop` karo, kaam continue.

## Q10. `git cherry-pick` kya hai?
**Answer:** Kisi doosri branch ka **ek specific commit** apni branch pe laao:

```bash
git log feature-x --oneline    # commit SHA dekho
git checkout main
git cherry-pick a1b2c3d        # sirf wo commit yahan laao
```

👉 **Use case:** poori branch abhi merge nahi karni, par uska ek bugfix abhi chahiye.

## Q11. Pull Request (PR) kaise kaam karta hai?
**Answer:**

1. Feature branch banao, commits karo, push karo
2. GitHub pe **"New Pull Request"** — feature → main
3. Teammates **review** karte hain — comments, changes maangte hain
4. Approve hone pe **merge** (ya squash merge)
5. Branch delete kar do

**Achha PR kaise likhein:**
- Title chhota aur clear
- Description me: **kya** kiya, **kyun** kiya, kaise test kiya
- Chhote PRs — 200-400 lines max. 2000 lines ka PR koi review nahi karta

## Q12. Common interview questions
**Answer:**

**"HEAD kya hai?"** — Current commit ka pointer. `detached HEAD` = kisi branch pe nahi, seedha commit pe ho.

**"`origin` kya hai?"** — Remote repo ka default naam (GitHub wala).

**"Fork vs Clone?"** — Fork = GitHub pe **tumhari copy** (doosre ke repo ka). Clone = computer pe download.

**"Squash merge kya hai?"** — Branch ke saare commits **ek commit** me daba ke merge — history clean rehti hai.

**"`git log` se kya milta hai?"** — Commit history: SHA, author, date, message. `git log --oneline --graph` se tree dikhta hai.

---

> 💡 **Interview tip:** Git question me **kabhi koi command rat ke mat bolo** — bolo *"maine real project me ye workflow use kiya: feature branch → commits → PR → review → squash merge"*. Aur `merge` vs `rebase` me ye line: *"rebase clean history deta hai par shared branches pe kabhi nahi karna chahiye kyunki history rewrite ho jaati hai"* — ye bolte hi interviewer samajh jaata hai tumne practically kaam kiya hai.
