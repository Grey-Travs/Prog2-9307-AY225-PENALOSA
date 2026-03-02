# PROGRAMMING 2 – MACHINE PROBLEM 
**University of Perpetual Help System DALTA – Molino Campus**
**BS Computer Science – Data Science**

## Project Overview
This repository contains the midterm machine problem solving a data analytics scenario. The objective is to act as a junior data analyst and create a quick executive sales summary from a given CSV dataset. 

The project is implemented in two distinct programming languages/environments based on the provided requirements:
1. **Java** (Desktop GUI Implementation)
2. **JavaScript / Node.js** (Web Hybrid Implementation)

## Folder Structure
\`\`\`text
MIDTERMWORK1/
│
├── JAVA/
│   ├── Main.java             # Java GUI and file validation logic
│   └── VideoGameRecord.java  # Modular data record class
|   └── README.md             # JAVA-specific documentation
│
├── JAVASCRIPT/
│   ├── index.js              # Node.js runner, readline I/O, and server
│   ├── script.js             # Analytics and data processing logic
│   ├── index.html            # Web dashboard interface
│   ├── style.css             # Dashboard styling
│   └── README.md             # JS-specific documentation
│
├── Database/
│   └── vgchartz-2024.csv     # The provided dataset
│
└── README.md                 # This main documentation file
\`\`\`

## Core Program Flow & Requirements
Both implementations strictly follow this execution flow:
1. **Dynamic Path Input:** The program asks the user to input the full file path at runtime. File paths and dataset values are never hardcoded.
2. **Validation Loop:** The program checks if the file exists, is readable, and is in the correct CSV format. If invalid, an error is displayed and the user is prompted again.
3. **Data Processing:** Parses the dataset into memory using proper file handling (`BufferedReader`/`Scanner` for Java, `fs` for Node.js).
4. **Analytics Output:** Computes total records, total sales, average sales, and the highest/lowest single transactions, displaying them in a formatted, readable UI.
5. **Robustness:** Proper modular design and `try-catch` error handling are implemented throughout.

## Execution Instructions
* **For Java:** Compile and run `Main.java`. A graphical window will appear asking for the file path.
* **For JavaScript:** Navigate to the `JAVASCRIPT` folder in your terminal, run `node index.js`, provide the path when prompted, and open `http://localhost:3000` in your web browser.