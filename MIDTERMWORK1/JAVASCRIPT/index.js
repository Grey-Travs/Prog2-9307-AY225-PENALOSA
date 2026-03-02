const fs = require('fs');
const readline = require('readline');
const http = require('http');
const path = require('path');
const DataAnalyzer = require('./script.js');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let activeFilePath = "";

function askFilePath() {
    console.clear(); // Clears the terminal for a clean look
    console.log("============================================================");
    console.log("           📊 EXECUTIVE SALES SUMMARY DASHBOARD           ");
    console.log("============================================================");
    console.log(" Welcome! This tool processes your CSV sales dataset and");
    console.log(" generates an interactive web dashboard with key analytics.");
    console.log("");
    console.log(" INSTRUCTIONS:");
    console.log(" Please enter the FULL file path to your dataset.");
    console.log(" Ensure the file is a valid .csv format.");
    console.log("");
    console.log(" EXAMPLES OF VALID PATHS:");
    console.log(" - Windows: C:\\Users\\Name\\Downloads\\vgchartz-2024.csv");
    console.log(" - Mac/Linux: /Users/Name/Downloads/vgchartz-2024.csv");
    console.log("============================================================\n");

    rl.question("👉 Enter dataset file path: ", function(inputPath) {
        const cleanPath = inputPath.trim().replace(/^["']|["']$/g, '');

        if (fs.existsSync(cleanPath) && cleanPath.toLowerCase().endsWith('.csv')) {
            console.log("\n[SUCCESS] File located and verified! Analyzing data...");
            activeFilePath = cleanPath;
            rl.close();
            startServer(); 
        } else {
            console.log("\n[ERROR] We couldn't find a valid CSV file at that location.");
            console.log("        Please double-check the spelling and try again.");
            
            // Wait 2 seconds so they can read the error before looping
            setTimeout(askFilePath, 2500); 
        }
    });
}

function startServer() {
    const server = http.createServer((req, res) => {
        // Serve the HTML GUI
        if (req.url === '/') {
            fs.readFile(path.join(__dirname, 'index.html'), (err, content) => {
                res.writeHead(200, { 'Content-Type': 'text/html' });
                res.end(content);
            });
        } 
        // Serve the CSS
        else if (req.url === '/style.css') {
            fs.readFile(path.join(__dirname, 'style.css'), (err, content) => {
                res.writeHead(200, { 'Content-Type': 'text/css' });
                res.end(content);
            });
        } 
        // API endpoint that sends our calculations to the GUI
        else if (req.url === '/api/data') {
            const results = DataAnalyzer.processFile(activeFilePath);
            res.writeHead(200, { 'Content-Type': 'application/json' });
            res.end(JSON.stringify(results));
        } else {
            res.writeHead(404);
            res.end("Not found");
        }
    });

    server.listen(3000, () => {
        console.log("\n============================================================");
        console.log(" ✅ ANALYSIS COMPLETE! SERVER IS RUNNING.");
        console.log("============================================================");
        console.log(" To view your formatted analytics report, please follow these steps:");
        console.log(" 1. Open your favorite web browser (Chrome, Edge, Safari).");
        console.log(" 2. Type the following address into the URL bar and press Enter:");
        console.log("\n       👉 http://localhost:3000 👈\n");
        console.log(" (Note: Keep this terminal window open while viewing the dashboard.)");
        console.log(" Press Ctrl+C in this terminal to close the program when finished.");
        console.log("============================================================\n");
    });
}

// Start the terminal prompt loop
askFilePath();