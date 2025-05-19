# 🎯 Java Bucket List CLI Application

Welcome to the **Bucket List CLI App** – a fully featured command-line interface (CLI) Java application designed for managing your personal or professional bucket list. Whether you want to explore the world, learn new skills, or track your life goals, this app helps you organize and track your progress.

---

## 📚 Features

- ✅ Add new bucket list items with detailed info (title, description, category, priority, deadlines)
- 🧾 View all items
- 🖍️ Update existing item titles
- ✅ Mark items as completed
- 🗑️ Remove items
- 🔍 Filter items by category
- 📊 Sort items by priority
- 📦 Choose between file-based or Azure SQL storage

---

## 🛠️ Technologies

- Java 17+
- File I/O (text storage with delimiters)
- JDBC with Azure SQL
- Maven for dependency management (optional)
- IntelliJ IDEA / VS Code (recommended)

---

## 🏗️ Project Structure

```
BucketList/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── pluralsight/
                    ├── Main.java
                    ├── BucketItem.java
                    ├── BucketItemManager.java
                    ├── FileBucketManager.java
                    └── DatabaseManager.java
bucketlist.txt            # File-based data (used by FileBucketManager)
README.md                 # This file
```

---

## ✨ BucketItem Fields

Each bucket item includes:

| Field         | Type        | Description                                  |
|---------------|-------------|----------------------------------------------|
| `id`          | int         | Auto-generated identifier                    |
| `title`       | String      | The name of the goal                         |
| `description` | String      | A short explanation or context               |
| `isDone`      | boolean     | Whether the item is completed                |
| `createdDate` | LocalDate   | When the item was added                      |
| `targetDate`  | LocalDate   | Desired completion date                      |
| `completedDate` | LocalDate | Auto-filled when marked as done              |
| `category`    | String      | Topic area (e.g., Travel, Career)            |
| `priority`    | int         | 1 (high) to 5 (low)                          |
| `notes`       | String      | Additional comments                          |
| `imageUrl`    | String      | Link to visual (optional)                    |

---

## ▶️ Getting Started

### ✅ Prerequisites

- Java JDK 17+
- Git (optional)
- [Microsoft JDBC Driver for SQL Server](https://learn.microsoft.com/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server)
- (Optional) Azure SQL Database

---

### 📥 Clone the Repository

```bash
git clone https://github.com/your-username/bucket-list-cli.git
cd bucket-list-cli
```

---

### 🧪 Compile & Run (without Maven)

```bash
javac -cp ".;lib/*" -d out src/main/java/com/pluralsight/*.java
java -cp ".;out;lib/*" com.pluralsight.Main
```

---

## 💾 Storage Options

### 1. File-Based

- Uses `bucketlist.txt`
- Stored with `|` delimiter in format:

```
Title|isDone|Description|TargetDate|Category|Priority
```

### 2. Azure SQL Database

#### Required Table

```sql
CREATE TABLE BucketItems (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255),
    description NVARCHAR(1000),
    isDone BIT DEFAULT 0,
    createdDate DATE NOT NULL DEFAULT GETDATE(),
    targetDate DATE,
    completedDate DATE,
    category NVARCHAR(100),
    priority INT,
    notes NVARCHAR(MAX),
    imageUrl NVARCHAR(1000)
);
```

#### Example JDBC Connection String:

```java
jdbc:sqlserver://skills4it.database.windows.net:1433;
database=yearup;
user=bucket_user;
password=SuperSecurePassword123!;
encrypt=true;
trustServerCertificate=false;
loginTimeout=30;
```

---

## 🧪 Example Usage

```
🎯 Welcome to your Bucket List App!
Choose storage option:
1. File-based storage
2. Azure SQL Database

📋 Menu:
1. View all items
2. Add a new item
3. Mark item as done
4. Remove item
5. Update item title
6. Filter by category
7. Sort by priority
8. Exit
```

---

## 🧪 Example File Format

```
Go bungee jumping|false|Leap from bridge|2025-09-01|Adventure|1
Learn Java|false|Finish Java course|2025-10-01|Skill|2
```

---

## 🧠 Learning Objectives

- Apply core Java programming skills
- Understand abstraction and interfaces
- Practice file I/O and error handling
- Use JDBC and SQL for persistent data
- Build structured CLI programs

---

## 🧰 Future Improvements

- JSON or SQLite storage option
- Web interface (Spring Boot or Vaadin)
- Reminder notifications via email
- Achievements and goal streaks
- Export as PDF

---

## 👥 Author

**Remsey Mailjard**  
[💼 www.remsey.nl](https://www.remsey.nl)  
[🔗 LinkedIn](https://linkedin.com/in/remseymailjard)

---

## 📄 License

MIT License. Use freely and improve. Sharing is caring!

---