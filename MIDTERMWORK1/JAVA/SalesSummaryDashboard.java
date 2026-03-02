import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executive Sales Summary Dashboard
 * Handles dynamic file input, validation, and analytics processing.
 * * @author Travis Peñalosa
 */
public class SalesSummaryDashboard extends JFrame {

    private JTextField pathInput;
    private JButton generateButton;
    private JTextArea reportArea;

    public SalesSummaryDashboard() {
        // Initialize Window
        setTitle("Executive Sales Summary Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel: File Input
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        inputPanel.add(new JLabel("Dataset Path:"), BorderLayout.WEST);
        
        // Input Wrapper to hold both the text field and the hint label
        JPanel inputWrapper = new JPanel(new BorderLayout());
        
        pathInput = new JTextField();
        pathInput.setToolTipText("Enter full absolute path to the .csv file");
        inputWrapper.add(pathInput, BorderLayout.CENTER);
        
        // The requested UI hint
        JLabel hintLabel = new JLabel("Example format: C:\\Users\\Student\\Downloads\\vgchartz-2024.csv");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(Color.GRAY);
        inputWrapper.add(hintLabel, BorderLayout.SOUTH);
        
        inputPanel.add(inputWrapper, BorderLayout.CENTER);
        
        generateButton = new JButton("Generate Report");
        inputPanel.add(generateButton, BorderLayout.EAST);

        // Center Panel: Executive Report Output
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Analytics Output"));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Action Listener for processing
        generateButton.addActionListener(e -> attemptDataProcessing());
    }

    /**
     * Handles file validation and manages the error "loop" conceptually for a GUI.
     * If invalid, it stops and forces the user to try again.
     */
    private void attemptDataProcessing() {
        String path = pathInput.getText().trim();
        File file = new File(path);

        // Validation Requirement: Exists, is a file, readable, and is a CSV.
        if (!file.exists() || !file.isFile() || !file.canRead() || !path.toLowerCase().endsWith(".csv")) {
            JOptionPane.showMessageDialog(this,
                    "CRITICAL ERROR: Invalid file path.\nEnsure the file exists, is accessible, and is formatted as a .csv.",
                    "File Validation Failed",
                    JOptionPane.ERROR_MESSAGE);
            reportArea.setText(""); 
            return; // Halt and wait for user to input a valid path
        }

        reportArea.setText("Validating dataset...\n");
        reportArea.append("File found. Compiling executive analytics...\n\n");
        generateButton.setEnabled(false); // Prevent multiple clicks

        // Background threading to prevent GUI freeze on large datasets
        SwingWorker<List<VideoGameRecord>, Void> worker = new SwingWorker<List<VideoGameRecord>, Void>() {
            @Override
            protected List<VideoGameRecord> doInBackground() {
                return parseDataset(file);
            }

            @Override
            protected void done() {
                try {
                    List<VideoGameRecord> data = get();
                    if (!data.isEmpty()) {
                        printExecutiveSummary(data);
                    } else {
                        reportArea.append("ERROR: The dataset is empty or unreadable.\n");
                    }
                } catch (Exception ex) {
                    reportArea.append("ERROR: Processing failed - " + ex.getMessage() + "\n");
                } finally {
                    generateButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    /**
     * Reads the dataset using BufferedReader and proper error handling.
     */
    private List<VideoGameRecord> parseDataset(File file) {
        List<VideoGameRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip CSV headers
                    continue;
                }

                // High-performance manual parsing to safely ignore commas inside quotes
                String[] columns = fastCsvSplit(line);
                
                if (columns.length >= 12) {
                    try {
                        String title = columns[1].trim();
                        String console = columns[2].trim();
                        String publisher = columns[4].trim();
                        
                        // Parse sales data, default to 0.0 if blank
                        double sales = columns[7].isEmpty() ? 0.0 : Double.parseDouble(columns[7]);

                        records.add(new VideoGameRecord(title, console, publisher, sales));
                    } catch (NumberFormatException e) {
                        // Silently handle malformed numerical data in specific rows
                        continue; 
                    }
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, 
                "I/O Error while reading the file: " + e.getMessage(), 
                "Read Error", JOptionPane.ERROR_MESSAGE));
        }
        return records;
    }

    /**
     * High-speed CSV parser for processing thousands of lines efficiently.
     */
    private String[] fastCsvSplit(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (char c : line.toCharArray()) {
            if (c == '\"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else current.append(c);
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    /**
     * Computes required metrics and outputs the formatted UI report.
     */
    private void printExecutiveSummary(List<VideoGameRecord> records) {
        double totalRevenue = 0;
        double maxSales = Double.MIN_VALUE;
        double minSales = Double.MAX_VALUE;
        
        VideoGameRecord highestTransaction = null;
        VideoGameRecord lowestTransaction = null;

        for (VideoGameRecord record : records) {
            double sales = record.getTotalSales();
            totalRevenue += sales;

            if (sales > maxSales) {
                maxSales = sales;
                highestTransaction = record;
            }
            if (sales < minSales) {
                minSales = sales;
                lowestTransaction = record;
            }
        }

        double averageSales = records.isEmpty() ? 0 : totalRevenue / records.size();

        // Constructing the Executive Report
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================\n");
        sb.append("                  GLOBAL SALES EXECUTIVE SUMMARY                      \n");
        sb.append("======================================================================\n\n");
        
        sb.append(String.format("%-35s %,d\n", "Total Records Processed:", records.size()));
        sb.append(String.format("%-35s $%,.2f Million\n", "Total Global Revenue:", totalRevenue));
        sb.append(String.format("%-35s $%,.2f Million\n\n", "Average Sales per Title:", averageSales));
        
        sb.append("----------------------------------------------------------------------\n");
        sb.append("                       TRANSACTION EXTREMES                           \n");
        sb.append("----------------------------------------------------------------------\n\n");
        
        if (highestTransaction != null) {
            sb.append("HIGHEST SINGLE PERFORMER:\n");
            sb.append(String.format("  Title     : %s\n", highestTransaction.getTitle()));
            sb.append(String.format("  Platform  : %s\n", highestTransaction.getConsole()));
            sb.append(String.format("  Revenue   : $%,.2f Million\n\n", highestTransaction.getTotalSales()));
        }

        if (lowestTransaction != null) {
            sb.append("LOWEST SINGLE PERFORMER:\n");
            sb.append(String.format("  Title     : %s\n", lowestTransaction.getTitle()));
            sb.append(String.format("  Platform  : %s\n", lowestTransaction.getConsole()));
            sb.append(String.format("  Revenue   : $%,.2f Million\n", lowestTransaction.getTotalSales()));
        }
        
        sb.append("\n======================================================================");

        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0); // Scroll to top
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SalesSummaryDashboard().setVisible(true);
        });
    }
}