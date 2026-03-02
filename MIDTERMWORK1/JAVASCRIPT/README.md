# Executive Sales Summary Dashboard - Node.js Implementation

## Overview
This project is a Node.js implementation of the Sales Summary Dashboard. It acts as a hybrid application: it uses a Node.js command-line interface to satisfy strict file validation requirements using the `fs` and `readline` modules, and then launches a local web server to display a formatted, executive-style HTML/CSS GUI dashboard.

## Analytics Performed
The script reads a provided CSV file and calculates the following:
- Total number of records
- Total sales / revenue
- Average sales per transaction
- Highest single transaction
- Lowest single transaction

## Prerequisites
- **Node.js** must be installed on your system.
- The `vgchartz-2024.csv` dataset must be available on your local machine.

## How to Run
1. Open your terminal or command prompt.
2. Navigate to the `JAVASCRIPT` directory containing these files.
3. Execute the program by running:
   \`\`\`bash
   node index.js
   \`\`\`
4. When prompted in the terminal, enter the **full file path** to your CSV dataset (e.g., `C:\Users\Student\Downloads\vgchartz-2024.csv`).
5. The program will validate the file's existence and format. If invalid, it will loop and ask again.
6. Upon successful validation, the Node.js server will start. 
7. Open your web browser and navigate to `http://localhost:3000` to view the formatted analytics report.

## Project Structure
- `index.js`: Main application runner. Handles terminal I/O, file path validation using `fs.existsSync()`, and hosts the local web server.
- `script.js`: The `DataAnalyzer` class module containing the data parsing and mathematical computations using `try-catch` error handling.
- `index.html`: The frontend user interface.
- `style.css`: Styling for the executive dashboard layout.